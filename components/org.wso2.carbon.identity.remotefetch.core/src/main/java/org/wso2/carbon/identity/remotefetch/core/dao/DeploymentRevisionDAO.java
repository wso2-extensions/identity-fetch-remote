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

package org.wso2.carbon.identity.remotefetch.core.dao;

import org.wso2.carbon.identity.remotefetch.common.DeploymentRevision;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.util.List;

/**
 * Interface used to access the data layer to store/update DeploymentRevisions.
 */
public interface DeploymentRevisionDAO {

    /**
     * Create deployment revision data fro the DeploymentRevision Object.
     *
     * @param deploymentRevision DeploymentRevision
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void createDeploymentRevision(DeploymentRevision deploymentRevision) throws RemoteFetchCoreException;

    /**
     * Get deployment revision object using remote configuration id and deployment revision name (application name).
     *
     * @param remoteFetchConfigurationId remoteFetchConfigurationId
     * @param itemName                   Application name
     * @return DeploymentRevision
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    DeploymentRevision getDeploymentRevision(String remoteFetchConfigurationId, String itemName)
            throws RemoteFetchCoreException;

    /**
     * Update deployment revision data fro the DeploymentRevision Object.
     *
     * @param deploymentRevision DeploymentRevision
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void updateDeploymentRevision(DeploymentRevision deploymentRevision) throws RemoteFetchCoreException;

    /**
     * Delete deployment revision data corresponding to deploymentRevisionId.
     *
     * @param deploymentRevisionId deploymentRevisionId
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void deleteDeploymentRevision(String deploymentRevisionId) throws RemoteFetchCoreException;

    /**
     * Get list of deployment revisions corresponding to remoteFetchConfigurationId.
     *
     * @param remoteFetchConfigurationId remoteFetchConfigurationId
     * @return list of deployment revisions
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    List<DeploymentRevision> getDeploymentRevisionsByConfigurationId(String remoteFetchConfigurationId)
            throws RemoteFetchCoreException;

}
