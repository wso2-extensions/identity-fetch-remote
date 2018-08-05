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

/**
 * Interface for service that allows management of remote fetch configurations.
 */
public interface RemoteFetchConfigurationService {

    /**
     * @param fetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    ValidationReport addRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration) throws RemoteFetchCoreException;

    /**
     * @param fetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    ValidationReport updateRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration) throws RemoteFetchCoreException;

    /**
     * @param fetchConfigurationId
     * @return Remote Fetch Configuration for id
     * @throws RemoteFetchCoreException
     */
    RemoteFetchConfiguration getRemoteFetchConfiguration(int fetchConfigurationId) throws RemoteFetchCoreException;

    /**
     * @param tenant_id
     * @return Remote Fetch Configuration list for tenant id
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getRemoteFetchConfigurationList(int tenant_id) throws RemoteFetchCoreException;

    /**
     * @param tenant_id
     * @return
     * @throws RemoteFetchCoreException
     */
    List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationList(int tenant_id)
            throws RemoteFetchCoreException;

    /**
     * @return All Remote Fetch Configurations
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getRemoteFetchConfigurationList() throws RemoteFetchCoreException;

    /**
     * @return All Enabled Remote Fetch Configurations
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getEnabledRemoteFetchConfigurationList() throws RemoteFetchCoreException;

    /**
     * @param fetchConfigurationId
     * @throws RemoteFetchCoreException
     */
    void deleteRemoteFetchConfiguration(int fetchConfigurationId) throws RemoteFetchCoreException;
}
