/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.remotefetch.core.impl.handlers.repository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.wso2.carbon.identity.remotefetch.common.ConfigurationFileStream;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Wrapper class of JGit implementations.
 */
public class GitRepositoryManager implements RepositoryManager {

    private static final Log log = LogFactory.getLog(GitRepositoryManager.class);

    private String uri = "";
    private String branch = "";
    private String name = "";
    private File repoPath;
    private File fileRoot;
    private Repository repo;
    private Git git;
    private CredentialsProvider credentialsProvider;

    public GitRepositoryManager(String name, String uri, String branch, File directory, File workingDir,
                                CredentialsProvider credentialsProvider) {

        this.name = name;
        this.branch = branch;
        this.uri = uri;
        this.repoPath = new File(workingDir, this.name);
        this.fileRoot = directory;
        this.credentialsProvider = credentialsProvider;

        // Check if repository path exists, if so load as local repository
        try {
            if (this.repoPath.exists() && this.repoPath.isDirectory()) {
                this.repo = this.getLocalRepository();
                this.git = new Git(this.repo);
            }
        } catch (IOException e) {
            log.info("IOException setting local repository, will be cloned");
        }
    }

    private Repository cloneRepository() throws GitAPIException {

        CloneCommand cloneRequest = Git.cloneRepository()
                .setURI(this.uri)
                .setDirectory(this.repoPath)
                .setBranchesToClone(Arrays.asList(branch))
                .setBranch(this.branch)
                .setCredentialsProvider(this.credentialsProvider);
        return cloneRequest.call().getRepository();
    }

    private Repository getLocalRepository() throws IOException {

        FileRepositoryBuilder localBuilder = new FileRepositoryBuilder();
        return localBuilder.findGitDir(this.repoPath).build();
    }

    private void pullRepository() throws GitAPIException {

        PullCommand pullRequest = this.git.pull();
        try {
            pullRequest.setCredentialsProvider(this.credentialsProvider);
            pullRequest.call();
        } catch (JGitInternalException e) {
            log.error("Unable to pull git repository: " + sanitize(this.uri), e);
        }
    }

    private RevCommit getLastCommit(File path) throws GitAPIException {

        List<RevCommit> revCommits = new ArrayList<>();
        Iterable<RevCommit> logIterater = git.log().addPath(path.getPath()).call();
        logIterater.forEach(revCommits::add);
        return revCommits.get(0);
    }

    /**
     * Method to Check for updates on the remote repository and fetch to local.
     *
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    @Override
    public void fetchRepository() throws RemoteFetchCoreException {

        if (this.git != null) {
            try {
                this.pullRepository();
            } catch (GitAPIException e) {
                log.error("Unable to pull repository " + sanitize(this.uri) + " from remote", e);
                throw new RemoteFetchCoreException("Unable to pull repository " + sanitize(this.uri) + " from remote",
                        e);
            }
        } else {
            try {
                this.repo = this.cloneRepository();
            } catch (GitAPIException e) {
                log.error("Unable to clone repository " + sanitize(this.uri) + " from remote", e);
                throw new RemoteFetchCoreException("Unable to clone repository  " + sanitize(this.uri) + " from remote",
                        e);
            }
            this.git = new Git(this.repo);
        }
    }

    /**
     * Returns an InputStream for the specified path from local repository.
     *
     * @param location Configuration File location
     * @return ConfigurationFileStream
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    @Override
    public ConfigurationFileStream getFile(File location) throws RemoteFetchCoreException {

        if (!this.isSubDir(this.fileRoot, location)) {
            throw new RemoteFetchCoreException("Requested file doesn't share repository root");
        }

        try (ObjectReader reader = this.repo.newObjectReader()) {
            RevCommit commit = this.getLastCommit(location);
            TreeWalk treewalk = TreeWalk.forPath(this.repo, location.getPath(), commit.getTree());
            return new ConfigurationFileStream(reader.open(treewalk.getObjectId(0)).openStream(), location);
        } catch (GitAPIException e) {
            throw new RemoteFetchCoreException("Unable to get last revision of file", e);
        } catch (NullPointerException e) {
            throw new RemoteFetchCoreException("Unable to resolve tree for file give", e);
        } catch (IOException e) {
            throw new RemoteFetchCoreException("Unable to read file from local", e);
        }
    }

    /**
     * Returns the last modified date of the local file.
     *
     * @param location Configuration File location
     * @return Date
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    @Override
    public Date getLastModified(File location) throws RemoteFetchCoreException {

        try {
            RevCommit commit = getLastCommit(location);
            return new Date((long) commit.getCommitTime() * 1000); //UNIX timestamp to seconds
        } catch (Exception e) {
            throw new RemoteFetchCoreException("Repository I/O exception", e);
        }
    }

    /**
     * Gets an unique identifier for file state.
     *
     * @param location Configuration File location
     * @return File Hash
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    @Override
    public String getRevisionHash(File location) throws RemoteFetchCoreException {

        try {
            RevCommit rc = this.getLastCommit(location);
            return rc.getName();
        } catch (Exception e) {
            throw new RemoteFetchCoreException("Repository I/O exception", e);
        }
    }

    /**
     * List files from local repository.
     *
     * @return List of configuration files.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    @Override
    public List<File> listFiles() throws RemoteFetchCoreException {

        List<File> availableFiles = new ArrayList<>();

        TreeWalk treeWalk = new TreeWalk(this.repo);
        TreeFilter pathFilter = PathFilter.create(this.fileRoot.getPath());

        RevWalk revWalk = new RevWalk(this.repo);
        ObjectId headRef;
        try {
            headRef = this.repo.resolve(Constants.HEAD);
            treeWalk.addTree(revWalk.parseCommit(headRef).getTree());
        } catch (IOException e) {
            throw new RemoteFetchCoreException("Exception parsing last commit", e);
        }

        treeWalk.setRecursive(false);
        treeWalk.setFilter(pathFilter);

        try {
            while (treeWalk.next()) {
                if (treeWalk.isSubtree()) {
                    treeWalk.enterSubtree();
                } else {
                    availableFiles.add(new File(treeWalk.getPathString()));
                }
            }
            return availableFiles;
        } catch (IOException e) {
            throw new RemoteFetchCoreException("Exception on traversing for give path", e);
        }
    }

    private boolean isFileInSubDirectory(File baseDir, File path) {

        if (path == null) {
            return false;
        }
        if (path.equals(baseDir)) {
            return true;
        }
        return isFileInSubDirectory(baseDir, path.getParentFile());
    }

    private String sanitize(String input) {

        if (StringUtils.isBlank(input)) {
            return input;
        }

        return input.replaceAll("(\\r|\\n|%0D|%0A|%0a|%0d)", "");
    }

    private boolean isSubDir(File baseFile, File userFile) {

        Path baseDirPath = Paths.get(baseFile.getAbsolutePath());
        Path userPath = Paths.get(userFile.getPath());

        if (!baseDirPath.isAbsolute()) {
            throw new IllegalArgumentException("Base path must be absolute");
        }

        if (userPath.isAbsolute()) {
            throw new IllegalArgumentException("User path must be relative");
        }

        // Join the two paths together, then normalize so that any ".." elements
        // in the userPath can remove parts of baseDirPath.
        // (e.g. "/foo/bar/baz" + "../attack" -> "/foo/bar/attack")
        final Path resolvedPath = baseDirPath.resolve(userPath).normalize();

        // Make sure the resulting path is still within the required directory.
        // (In the example above, "/foo/bar/attack" is not.)
        if (!resolvedPath.startsWith(baseDirPath)) {
            throw new IllegalArgumentException("User path escapes the base path");
        }
        return true;
    }
}
