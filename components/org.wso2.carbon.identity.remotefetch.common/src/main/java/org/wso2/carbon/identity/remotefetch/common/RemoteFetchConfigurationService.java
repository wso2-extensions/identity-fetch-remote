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

import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.util.List;
import java.util.OptionalInt;

/**
 * Interface for service that allows management of remote fetch configurations.
 */
public interface RemoteFetchConfigurationService {

    /**
     * This method is used to call by clients to add RemoteFetchConfiguration into database.
     * This method called by clients when user add new RemoteFetchConfiguration via carbon console.
     *
     * @param fetchConfiguration RemoteFetchConfiguration Object.
     * @return ValidationReport ValidationReport says whether params of RemoteFetchConfiguration is valid or not.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    ValidationReport addRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration)
            throws RemoteFetchCoreException;

    /**
     * This method is used to call by clients to update existing RemoteFetchConfiguration.
     * This method called by clients when user edit existing RemoteFetchConfiguration via carbon console.
     *
     * @param fetchConfiguration RemoteFetchConfiguration Object.
     * @return ValidationReport ValidationReport says whether params of RemoteFetchConfiguration is valid or not.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    ValidationReport updateRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration)
            throws RemoteFetchCoreException;

    /**
     * This method is used to call by clients to get existing RemoteFetchConfiguration by Id.
     * This method is called by clients while triggering or edit existing RemoteFetchConfiguration.
     *
     * @param fetchConfigurationId ID.
     * @return Remote Fetch Configuration for id.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    RemoteFetchConfiguration getRemoteFetchConfiguration(String fetchConfigurationId)
            throws RemoteFetchCoreException;

    /**
     * This method is used to call by clients to get list of BasicRemoteFetchConfiguration by tenantID.
     * This method is called by clients in list view.
     *
     * @param limit  limit
     * @param offset offset
     * @return List of BasicRemoteFetchConfiguration
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationList(OptionalInt limit, OptionalInt offset)
            throws RemoteFetchCoreException;

    /**
     * This method is used to call by internal auto pull method to get list of enabled BasicRemoteFetchConfiguration.
     *
     * @return All Enabled Remote Fetch Configurations.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getEnabledPollingRemoteFetchConfigurationList() throws RemoteFetchCoreException;

    /**
     * This method is used to call by clients to delete BasicRemoteFetchConfiguration by ID.
     *
     * @param fetchConfigurationId Id.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void deleteRemoteFetchConfiguration(String fetchConfigurationId)
            throws RemoteFetchCoreException;

    /**
     * This method is used to trigger remote fetch configuration by ID.
     * This method schedule immediate trigger task.
     *
     * @param fetchConfiguration RemoteFetchConfiguration
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void triggerRemoteFetch(RemoteFetchConfiguration fetchConfiguration) throws RemoteFetchCoreException;

    /**
     * This method is used to get deployed revisions by remote fetch configuration.
     * This method is used to provide status of remote fetch configuration.
     *
     * @param fetchConfigurationId fetchConfigurationId
     * @return List of deployment revisions.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    List<DeploymentRevision> getDeploymentRevisions(String fetchConfigurationId) throws RemoteFetchCoreException;

    /**
     * This method is used to handle web hook by calling web hook handler.
     *
     * @param url               url of remote repository.
     * @param branch            branch of remote repository.
     * @param modifiedFileNames Files been modified by given push.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    void handleWebHook(String url, String branch, List<String> modifiedFileNames) throws RemoteFetchCoreException;

}
