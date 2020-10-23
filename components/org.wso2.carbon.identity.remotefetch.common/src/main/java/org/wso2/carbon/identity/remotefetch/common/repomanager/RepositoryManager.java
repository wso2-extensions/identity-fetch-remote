/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.remotefetch.common.repomanager;

import org.wso2.carbon.identity.remotefetch.common.ConfigurationFileStream;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Interface to define a repository manager, that communicates with a remote repository and handles it locally.
 */
public interface RepositoryManager {

    /**
     * Method to Check for updates on the remote repository and fetch to local.
     *
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void fetchRepository() throws RemoteFetchCoreException;

    /**
     * Returns an InputStream for the specified path from local repository.
     *
     * @param location Configuration File location
     * @return ConfigurationFileStream
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    ConfigurationFileStream getFile(File location) throws RemoteFetchCoreException;

    /**
     * Returns the last modified date of the local file.
     *
     * @param location Configuration File location
     * @return Date
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    Date getLastModified(File location) throws RemoteFetchCoreException;

    /**
     * Gets an unique identifier for file state.
     *
     * @param location Configuration File location
     * @return File Hash
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    String getRevisionHash(File location) throws RemoteFetchCoreException;

    /**
     * List files from local repository.
     *
     * @return List of configuration files.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    List<File> listFiles() throws RemoteFetchCoreException;
}
