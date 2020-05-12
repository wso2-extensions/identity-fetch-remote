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

    /**
     * Default Constructor used by DAO.
     */
    public BasicRemoteFetchConfiguration() {
    }

    /**
     * Following constructor is used only by Test classes.
     * @param id
     * @param isEnabled
     * @param repositoryManagerType
     * @param actionListenerType
     * @param configurationDeployerType
     * @param remoteFetchName
     * @param successfulDeployments
     * @param failedDeployments
     */
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
     * Get id.
     * @return Id
     */
    public String getId() {

        return id;
    }

    /**
     *Get remoteFetchName.
     * @return remoteFetName.
     */
    public String getRemoteFetchName() {

        return remoteFetchName;
    }

    /**
     * Get isEnabled which is used to point particular remoteFetchConfig is enabled or not.
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
     * Get number of successful deployments for list view.
     * @return successfulDeployments
     */
    public int getSuccessfulDeployments() {

        return successfulDeployments;
    }

    /**
     * Get number of failed deployments for listview.
     * @return failedDeployments
     */
    public int getFailedDeployments() {

        return failedDeployments;
    }

    /**
     * Get last deployed time for listview.
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
     * Set last deployed time after successful trigger.
     * @param lastDeployed
     */
    public void setLastDeployed(Date lastDeployed) {

        if (lastDeployed == null) {
            this.lastDeployed = null;
        } else {
            this.lastDeployed = new Date(lastDeployed.getTime());
        }
    }

    /**
     * Set Unique Id for basic remote fetch configuration.
     * @param id
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Set enabled attribute for basic remote fetch configuration.
     * This attribute eligible corresponding fettch configuration to trigger periodically.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {

        isEnabled = enabled;
    }

    /**
     * Set readable name for remote fetch configuration.
     * @param remoteFetchName
     */
    public void setRemoteFetchName(String remoteFetchName) {

        this.remoteFetchName = remoteFetchName;
    }

    /**
     * Set Repository Manager Type.
     * @see org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager
     * @param repositoryManagerType
     */
    public void setRepositoryManagerType(String repositoryManagerType) {

        this.repositoryManagerType = repositoryManagerType;
    }

    /**
     * Set Action Listener Type.
     * @see org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListener
     * @param actionListenerType
     */
    public void setActionListenerType(String actionListenerType) {

        this.actionListenerType = actionListenerType;
    }

    /**
     * Set Config Deployer type.
     * @see org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer
     * @param configurationDeployerType
     */
    public void setConfigurationDeployerType(String configurationDeployerType) {

        this.configurationDeployerType = configurationDeployerType;
    }

    /**
     * Set number of successful deployment deployed in last trigger.
     * @param successfulDeployments
     */
    public void setSuccessfulDeployments(int successfulDeployments) {

        this.successfulDeployments = successfulDeployments;
    }

    /**
     * Set number of failed deployment deployed in last trigger.
     * @param failedDeployments
     */
    public void setFailedDeployments(int failedDeployments) {

        this.failedDeployments = failedDeployments;
    }
}
