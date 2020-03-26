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

package org.wso2.carbon.identity.remotefetch.core.ui.dto;

import java.util.Date;

public class RemoteFetchConfigurationRowDTO {

    private int id;
    private boolean isEnabled;
    private String remoteFetchName;
    private String repositoryType;
    private String actionListenerType;
    private String configurationDeployerType;
    private int successfulDeployments;
    private int failedDeployments;
    private Date lastDeployed;

    public RemoteFetchConfigurationRowDTO(int id, boolean isEnabled, String repositoryType,
                                          String actionListenerType, String configarationDeployerType,
                                          String remoteFetchName, int successfulDeployments, int failedDeployments,
                                          Date lastDeployed) {

        this.id = id;
        this.isEnabled = isEnabled;
        this.remoteFetchName = remoteFetchName;
        this.repositoryType = repositoryType;
        this.actionListenerType = actionListenerType;
        this.configurationDeployerType = configarationDeployerType;
        this.successfulDeployments = successfulDeployments;
        this.failedDeployments = failedDeployments;
        this.lastDeployed = lastDeployed;

    }

    public int getId() {

        return id;
    }

    public boolean getIsEnabled() {

        return isEnabled;
    }

    public String getRemoteFetchName() {

        return remoteFetchName;
    }

    public String getRepositoryType() {

        return repositoryType;
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

        return lastDeployed;
    }

    @Override
    public String toString() {

        return "RemoteFetchConfigurationRowDTO{" +
                "id=" + id +
                ", remoteFetchName=" + remoteFetchName +
                ", isEnabled=" + isEnabled +
                ", repositoryType='" + repositoryType + '\'' +
                ", actionListenerType='" + actionListenerType + '\'' +
                ", configurationDeployerType='" + configurationDeployerType + '\'' +
                ", successfulDeployments=" + successfulDeployments +
                ", failedDeployments=" + failedDeployments +
                ", lastDeployed=" + lastDeployed +
                '}';
    }
}
