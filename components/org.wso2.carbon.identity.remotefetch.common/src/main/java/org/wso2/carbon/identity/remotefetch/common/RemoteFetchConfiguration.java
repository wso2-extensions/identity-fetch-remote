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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Holds the logical representation of remoteFetchConfiguration data to instantiate a RemoteFetch.
 * Used by Client while Insert and update new remoteFetchConfiguration.
 * Used by DAO to map configuration Db row data.
 * Used auto pull schedulers at runtime.
 */
public class RemoteFetchConfiguration {

    private int remoteFetchConfigurationId = -1;
    private int tenantId = 0;
    private String remoteFetchName = "";
    private boolean isEnabled;
    private String repositoryManagerType = "";
    private String actionListenerType = "";
    private String configurationDeployerType = "";
    private String userName = "";
    private Map<String, String> repositoryManagerAttributes = new HashMap<>();
    private Map<String, String> actionListenerAttributes = new HashMap<>();
    private Map<String, String> configurationDeployerAttributes = new HashMap<>();

    public RemoteFetchConfiguration(int remoteFetchConfigurationId, int tenantId,
                                    boolean isEnabled, String userName,
                                    String repositoryManagerType, String actionListenerType,
                                    String configurationDeployerType, String remoteFetchName) {

        this.remoteFetchConfigurationId = remoteFetchConfigurationId;
        this.tenantId = tenantId;
        this.remoteFetchName = remoteFetchName;
        this.isEnabled = isEnabled;
        this.repositoryManagerType = repositoryManagerType;
        this.actionListenerType = actionListenerType;
        this.configurationDeployerType = configurationDeployerType;
        this.userName = userName;
    }

    /**Tenant Id is used derived from carbon context while creation.
     * removing this tenant Id attribute  from this class produce malfunction because auto pull threads may not
     * have carbon context but it needs to deploy configuration.
     * @return tenantId
     */
    public int getTenantId() {

        return tenantId;
    }

    /**Human readable name given by user while creation.
     * set remoteFetchName
     *
     * @param remoteFetchName remoteFetchName
     */
    public void setRemoteFetchName(String remoteFetchName) {

        this.remoteFetchName = remoteFetchName;
    }

    /**
     * get remoteFetchName
     *
     * @return remoteFetchName
     */
    public String getRemoteFetchName() {

        return remoteFetchName;
    }

    /**set tenantId using carbon context.
     * @param tenantId
     */
    public void setTenantId(int tenantId) {

        this.tenantId = tenantId;
    }

    /**boolean parameter used to represent that auto pull is enabled for this remoteFetchConfiguration or not.
     * @return isEnabled
     */
    public boolean isEnabled() {

        return isEnabled;
    }

    /**set isEnabled.
     * @param isEnabled isEnabled
     */
    public void setEnabled(boolean isEnabled) {

        this.isEnabled = isEnabled;
    }

    /**This represents the user who created this remoteFetch configuration, set from session.
     * @return
     */
    public String getUserName() {

        return userName;
    }

    /**set username.
     * @param userName userName
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /** unique ID to represent this configuration.
     * @return
     */
    public int getRemoteFetchConfigurationId() {

        return remoteFetchConfigurationId;
    }

    /**set RemoteFetchConfigurationId.
     * @param remoteFetchConfigurationId remoteFetchConfigurationId
     */
    public void setRemoteFetchConfigurationId(int remoteFetchConfigurationId) {

        this.remoteFetchConfigurationId = remoteFetchConfigurationId;
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
     * set RepositoryManagerType
     */
    public void setRepositoryManagerType(String repositoryManagerType) {

        this.repositoryManagerType = repositoryManagerType;
    }

    /**
     * This string refers the type of actionListener eg: "POLLING".
     * {@link org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListener}
     * @return actionListenerType
     */
    public String getActionListenerType() {

        return actionListenerType;
    }

    /**set ActionListenerType
     * @param actionListenerType actionListenerType
     */
    public void setActionListenerType(String actionListenerType) {

        this.actionListenerType = actionListenerType;
    }

    /**
     * This string refers the type of configurationDeployer eg : "SP".
     * {@link org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer}
     */
    public String getConfigurationDeployerType() {

        return configurationDeployerType;
    }

    /**set ConfigurationDeployerType
     * @param configurationDeployerType configurationDeployerType
     */
    public void setConfigurationDeployerType(String configurationDeployerType) {

        this.configurationDeployerType = configurationDeployerType;
    }

    /**RepositoryManagerAttributes Holds Configurations regarding RepositoryManager.
     * @return repositoryManagerAttributes
     */
    public Map<String, String> getRepositoryManagerAttributes() {

        return repositoryManagerAttributes;
    }

    /**set RepositoryManagerAttributes.
     * @param repositoryManagerAttributes repositoryManagerAttributes
     */
    public void setRepositoryManagerAttributes(Map<String, String> repositoryManagerAttributes) {

        this.repositoryManagerAttributes = repositoryManagerAttributes;
    }

    /**ActionListenerAttributes holds configurations regarding Action Listener.
     * @return actionListenerAttributes
     */
    public Map<String, String> getActionListenerAttributes() {

        return actionListenerAttributes;
    }

    /**set ActionListenerAttributes.
     * @param actionListenerAttributes actionListenerAttributes
     */
    public void setActionListenerAttributes(Map<String, String> actionListenerAttributes) {

        this.actionListenerAttributes = actionListenerAttributes;
    }

    /**ConfigurationDeployerAttributes holds configuration regarding ConfigurationDeployer.
     * @return configurationDeployerAttributes
     */
    public Map<String, String> getConfigurationDeployerAttributes() {

        return configurationDeployerAttributes;
    }

    /**set ConfigurationDeployerAttributes.
     * @param configurationDeployerAttributes configurationDeployerAttributes
     */
    public void setConfigurationDeployerAttributes(Map<String, String> configurationDeployerAttributes) {

        this.configurationDeployerAttributes = configurationDeployerAttributes;
    }

    /**
     * Compare calling remotefetchConfiguration with given parameter.
     * @param o object.
     * @return equals or not.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteFetchConfiguration that = (RemoteFetchConfiguration) o;
        return remoteFetchConfigurationId == that.remoteFetchConfigurationId &&
                tenantId == that.tenantId &&
                Objects.equals(remoteFetchName, that.remoteFetchName) &&
                isEnabled == that.isEnabled &&
                Objects.equals(repositoryManagerType, that.repositoryManagerType) &&
                Objects.equals(actionListenerType, that.actionListenerType) &&
                Objects.equals(configurationDeployerType, that.configurationDeployerType) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(repositoryManagerAttributes, that.repositoryManagerAttributes) &&
                Objects.equals(actionListenerAttributes, that.actionListenerAttributes) &&
                Objects.equals(configurationDeployerAttributes, that.configurationDeployerAttributes);
    }

    /**
     * return hashCOde of the object.
     * @return hashCode
     */
    @Override
    public int hashCode() {

        return Objects.hash(remoteFetchConfigurationId, tenantId, isEnabled, repositoryManagerType, actionListenerType,
                configurationDeployerType, userName, remoteFetchName, repositoryManagerAttributes,
                actionListenerAttributes, configurationDeployerAttributes);
    }

    /**
     * To string method.
     * @return toString
     */
    @Override
    public String toString() {

        return "RemoteFetchConfiguration{" +
                "remoteFetchConfigurationId=" + remoteFetchConfigurationId +
                ", tenantId=" + tenantId +
                ", remoteFetchName=" + remoteFetchName +
                ", isEnabled=" + isEnabled +
                ", repositoryManagerType='" + repositoryManagerType + '\'' +
                ", actionListenerType='" + actionListenerType + '\'' +
                ", configurationDeployerType='" + configurationDeployerType + '\'' +
                ", userName='" + userName + '\'' +
                ", repositoryManagerAttributes=" + repositoryManagerAttributes +
                ", actionListenerAttributes=" + actionListenerAttributes +
                ", configurationDeployerAttributes=" + configurationDeployerAttributes +
                '}';
    }
}
