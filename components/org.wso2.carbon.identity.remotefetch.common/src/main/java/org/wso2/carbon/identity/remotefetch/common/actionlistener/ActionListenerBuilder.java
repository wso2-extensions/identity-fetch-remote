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

package org.wso2.carbon.identity.remotefetch.common.actionlistener;

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager;

/**
 * Builds a ActionListener from a RemoteFetchConfiguration file.
 */
public abstract class ActionListenerBuilder {

    private RemoteFetchConfiguration fetchConfig;
    private RepositoryManager repoConnector;
    private ConfigDeployer configDeployer;

    /**
     * Set RemoteFetchConfiguration attribute.
     *
     * @param fetchConfig RemoteFetchConfiguration
     * @return ActionListenerBuilder Caller itself
     */
    public ActionListenerBuilder addRemoteFetchConfig(RemoteFetchConfiguration fetchConfig) {

        this.fetchConfig = fetchConfig;
        return this;
    }

    /**
     * Set Repository Connector which contains repository configurations.
     *
     * @param repoConnector RepositoryManager
     * @return ActionListenerBuilder Caller itself
     */
    public ActionListenerBuilder addRepositoryConnector(RepositoryManager repoConnector) {

        this.repoConnector = repoConnector;
        return this;
    }

    /**
     * Set Configuration deployer which contains deployment configurations.
     *
     * @param configDeployer ConfigDeployer
     * @return ActionListenerBuilder Caller itself
     */
    public ActionListenerBuilder addConfigDeployer(ConfigDeployer configDeployer) {

        this.configDeployer = configDeployer;
        return this;
    }

    /**
     * Get RemoteFetchConfiguration (getter method).
     *
     * @return RemoteFetchConfiguration
     * @see RemoteFetchConfiguration
     */
    public RemoteFetchConfiguration getFetchConfig() {

        return fetchConfig;
    }

    /**
     * Get RepositoryManager (getter method).
     *
     * @return RepositoryManager
     * @see RepositoryManager
     */
    public RepositoryManager getRepoConnector() {

        return repoConnector;
    }

    /**
     * Get Configuration deployer (getter method).
     *
     * @return ConfigDeployer
     * @see ConfigDeployer
     */
    public ConfigDeployer getConfigDeployer() {

        return configDeployer;
    }

    /**
     * Build and return a new ActionListener with the set configuration.
     *
     * @return ActionListener
     * @throws ActionListenerBuilderException ActionListenerBuilderException
     */
    public abstract ActionListener build() throws ActionListenerBuilderException;
}
