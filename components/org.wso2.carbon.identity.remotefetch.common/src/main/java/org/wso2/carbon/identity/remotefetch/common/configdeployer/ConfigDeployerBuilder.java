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

package org.wso2.carbon.identity.remotefetch.common.configdeployer;

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;

/**
 * Builds a ConfigDeployer from a RemoteFetchConfiguration file.
 */
public abstract class ConfigDeployerBuilder {

    private RemoteFetchConfiguration fetchConfig;

    public ConfigDeployerBuilder() {

    }

    /**
     * Set RemoteFetchConfiguration attribute.
     *
     * @param fetchConfig RemoteFetchConfiguration
     * @return ConfigDeployerBuilder
     */
    public ConfigDeployerBuilder addRemoteFetchConfig(RemoteFetchConfiguration fetchConfig) {

        this.fetchConfig = fetchConfig;
        return this;
    }

    /**
     * Get RemoteFetchConfiguration (getter method).
     *
     * @return RemoteFetchConfiguration
     */
    public RemoteFetchConfiguration getFetchConfig() {

        return fetchConfig;
    }

    /**
     * Build and return a new ConfigDeployer with the set configuration.
     *
     * @return ConfigDeployer
     * @throws ConfigDeployerBuilderException ConfigDeployerBuilderException
     */
    public abstract ConfigDeployer build() throws ConfigDeployerBuilderException;
}
