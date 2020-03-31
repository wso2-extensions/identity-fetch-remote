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

package org.wso2.carbon.identity.remotefetch.common;

import java.util.Date;

/**
 * Holds RemoteFetch Configuration Data and related deployment data.
 */
public class BasicRemoteFetchConfiguration {

    private int id;
    private boolean isEnabled;
    private String remoteFetchName;
    private String repositoryManagerType;
    private String actionListenerType;
    private String configurationDeployerType;
    private int successfulDeployments;
    private int failedDeployments;
    private Date lastDeployed = null;

    public BasicRemoteFetchConfiguration(int id, boolean isEnabled,
                                         String repositoryManagerType,
                                         String actionListenerType, String configurationDeployerType,
                                         String remoteFetchName,
                                         int successfulDeployments, int failedDeployments) {

        this.id = id;
        this.remoteFetchName = remoteFetchName;
        this.isEnabled = isEnabled;
        this.repositoryManagerType = repositoryManagerType;
        this.actionListenerType = actionListenerType;
        this.configurationDeployerType = configurationDeployerType;
        this.successfulDeployments = successfulDeployments;
        this.failedDeployments = failedDeployments;
    }

    public int getId() {

        return id;
    }

    public String getRemoteFetchName() {

        return remoteFetchName;
    }

    public boolean isEnabled() {

        return isEnabled;
    }

    public String getRepositoryManagerType() {

        return repositoryManagerType;
    }

    public String getActionListenerType() {

        return actionListenerType;
    }

    public String getConfigurationDeployerType() {

        return configurationDeployerType;
    }

    public int getSuccessfulDeployments() {

        return successfulDeployments;
    }

    public int getFailedDeployments() {

        return failedDeployments;
    }

    public Date getLastDeployed() {
        if (lastDeployed == null) {
            return null;
        } else {
            return new Date(lastDeployed.getTime());
        }
    }

    public void setLastDeployed(Date lastDeployed) {
        if (lastDeployed == null) {
            this.lastDeployed = null;
        } else {
            this.lastDeployed = new Date(lastDeployed.getTime());
        }
    }

}
