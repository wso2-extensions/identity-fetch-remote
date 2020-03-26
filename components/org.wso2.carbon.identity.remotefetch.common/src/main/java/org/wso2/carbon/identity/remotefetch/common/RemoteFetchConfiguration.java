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

package org.wso2.carbon.identity.remotefetch.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Holds configuration data to instantiate a RemoteFetch
 */
public class RemoteFetchConfiguration implements Serializable {

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

    /**
     * @return
     */
    public int getTenantId() {

        return tenantId;
    }

    /**
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

    /**
     * @param tenantId
     */
    public void setTenantId(int tenantId) {

        this.tenantId = tenantId;
    }

    /**
     * @return
     */
    public boolean isEnabled() {

        return isEnabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(boolean enabled) {

        isEnabled = enabled;
    }

    /**
     * @return
     */
    public String getUserName() {

        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * @return
     */
    public int getRemoteFetchConfigurationId() {

        return remoteFetchConfigurationId;
    }

    /**
     * @param remoteFetchConfigurationId
     */
    public void setRemoteFetchConfigurationId(int remoteFetchConfigurationId) {

        this.remoteFetchConfigurationId = remoteFetchConfigurationId;
    }

    /**
     * @return
     */
    public String getRepositoryManagerType() {

        return repositoryManagerType;
    }

    /**
     * @param repositoryManagerType
     */
    public void setRepositoryManagerType(String repositoryManagerType) {

        this.repositoryManagerType = repositoryManagerType;
    }

    /**
     * @return
     */
    public String getActionListenerType() {

        return actionListenerType;
    }

    /**
     * @param actionListenerType
     */
    public void setActionListenerType(String actionListenerType) {

        this.actionListenerType = actionListenerType;
    }

    /**
     * @return
     */
    public String getConfigurationDeployerType() {

        return configurationDeployerType;
    }

    /**
     * @param configurationDeployerType
     */
    public void setConfigurationDeployerType(String configurationDeployerType) {

        this.configurationDeployerType = configurationDeployerType;
    }

    /**
     * @return
     */
    public Map<String, String> getRepositoryManagerAttributes() {

        return repositoryManagerAttributes;
    }

    /**
     * @param repositoryManagerAttributes
     */
    public void setRepositoryManagerAttributes(Map<String, String> repositoryManagerAttributes) {

        this.repositoryManagerAttributes = repositoryManagerAttributes;
    }

    /**
     * @return
     */
    public Map<String, String> getActionListenerAttributes() {

        return actionListenerAttributes;
    }

    /**
     * @param actionListenerAttributes
     */
    public void setActionListenerAttributes(Map<String, String> actionListenerAttributes) {

        this.actionListenerAttributes = actionListenerAttributes;
    }

    /**
     * @return
     */
    public Map<String, String> getConfigurationDeployerAttributes() {

        return configurationDeployerAttributes;
    }

    /**
     * @param configurationDeployerAttributes
     */
    public void setConfigurationDeployerAttributes(Map<String, String> configurationDeployerAttributes) {

        this.configurationDeployerAttributes = configurationDeployerAttributes;
    }

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
                remoteFetchName == that.remoteFetchName &&
                isEnabled == that.isEnabled &&
                Objects.equals(repositoryManagerType, that.repositoryManagerType) &&
                Objects.equals(actionListenerType, that.actionListenerType) &&
                Objects.equals(configurationDeployerType, that.configurationDeployerType) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(repositoryManagerAttributes, that.repositoryManagerAttributes) &&
                Objects.equals(actionListenerAttributes, that.actionListenerAttributes) &&
                Objects.equals(configurationDeployerAttributes, that.configurationDeployerAttributes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(remoteFetchConfigurationId, tenantId, isEnabled, repositoryManagerType, actionListenerType,
                configurationDeployerType, userName, remoteFetchName, repositoryManagerAttributes,
                actionListenerAttributes, configurationDeployerAttributes);
    }

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
