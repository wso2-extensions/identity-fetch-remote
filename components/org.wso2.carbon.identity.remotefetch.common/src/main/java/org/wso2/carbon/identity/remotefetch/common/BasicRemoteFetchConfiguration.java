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
 * This class to hold data object for listview UI.
 */
public class BasicRemoteFetchConfiguration {

    private String id;
    private boolean isEnabled;
    private String remoteFetchName;
    private String repositoryManagerType;
    private String actionListenerType;
    private String configurationDeployerType;
    private int successfulDeployments;
    private int failedDeployments;
    private Date lastDeployed = null;

    public BasicRemoteFetchConfiguration(String id, boolean isEnabled,
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

    /**
     * get id.
     * @return Id
     */
    public String getId() {

        return id;
    }

    /**
     *get remoteFetchName.
     * @return remoteFetName.
     */
    public String getRemoteFetchName() {

        return remoteFetchName;
    }

    /**
     * get isEnabled which is used to point particular remoteFetchConfig is enabled or not.
     * @return isEnabled.
     */
    public boolean isEnabled() {

        return isEnabled;
    }

    /**
     * This string refers the type of remote repository manager eg : "GIT".
     * {@link org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager}
     * @return repositoryManagerType
     */
    public String getRepositoryManagerType() {

        return repositoryManagerType;
    }

    /**
     * This string refers the type of actionListener eg: "POLLING".
     * {@link org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListener}
     * @return actionListenerType
     */
    public String getActionListenerType() {

        return actionListenerType;
    }

    /**
     * This string refers the type of configurationDeployer eg : "SP".
     * {@link org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer}
     * @return configurationDeployerType
     */
    public String getConfigurationDeployerType() {

        return configurationDeployerType;
    }

    /**
     * get number of successful deployments for list view.
     * @return successfulDeployments
     */
    public int getSuccessfulDeployments() {

        return successfulDeployments;
    }

    /**
     * get number of failed deployments for listview.
     * @return failedDeployments
     */
    public int getFailedDeployments() {

        return failedDeployments;
    }

    /**
     * get last deployed time for listview.
     * @return lastDeployed
     */
    public Date getLastDeployed() {
        if (lastDeployed == null) {
            return null;
        } else {
            return new Date(lastDeployed.getTime());
        }
    }

    /**
     * set last deployed time after successful trigger.
     * @param lastDeployed
     */
    public void setLastDeployed(Date lastDeployed) {
        if (lastDeployed == null) {
            this.lastDeployed = null;
        } else {
            this.lastDeployed = new Date(lastDeployed.getTime());
        }
    }

}
