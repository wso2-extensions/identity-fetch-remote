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

import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.util.List;

/**
 * Interface used to access the data layer to store/update RemoteFetch configurations.
 */
public interface RemoteFetchConfigurationDAO {

    /**
     * Insert new remote fetch configuration data row into database.
     * @param configuration remote fetch configuration.
     * @return
     * @throws RemoteFetchCoreException
     */
    void createRemoteFetchConfiguration(RemoteFetchConfiguration configuration) throws RemoteFetchCoreException;

    /**
     * Get remote fetch configuration by given resource id and tenant id.
     * @param configurationId
     * @param tenantId
     * @return
     * @throws RemoteFetchCoreException
     */
    RemoteFetchConfiguration getRemoteFetchConfiguration(String configurationId, int tenantId)
            throws RemoteFetchCoreException;

    /**
     * Update given remote fetch configuration.
     * @param configuration
     * @throws RemoteFetchCoreException
     */
    void updateRemoteFetchConfiguration(RemoteFetchConfiguration configuration) throws RemoteFetchCoreException;

    /**
     * Delete given remote fetch configuration from database.
     * @param configurationId
     * @param tenantId TenantDomain.
     * @throws RemoteFetchCoreException
     */
    void deleteRemoteFetchConfiguration(String configurationId, int tenantId) throws RemoteFetchCoreException;

    /**
     * Get all enabled remote fetch configurations regardless tenant.
     * This implementation was used by auto sync mechanism.
     * @return
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getAllEnabledPollingRemoteFetchConfigurations() throws RemoteFetchCoreException;

    /**
     * Get all remote fetch configuration for particular tenant.
     * @param tenantId
     * @return
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getRemoteFetchConfigurationsByTenant(int tenantId)
            throws RemoteFetchCoreException;

    /**
     * Get all remote fetch configuration which have Web Hook as action listener for particular tenant.
     * @param tenantId
     * @return
     * @throws RemoteFetchCoreException
     */
    List<RemoteFetchConfiguration> getWebHookRemoteFetchConfigurationsByTenant(int tenantId)
            throws RemoteFetchCoreException;

    /**
     * Get all basic remote fetch configuration for particular tenant.
     * @param tenantDomain
     * @param limit
     * @param offset
     * @return
     * @throws RemoteFetchCoreException
     */
    List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationsByTenant(String tenantDomain, int limit,
                                                                                  int offset)
            throws RemoteFetchCoreException;

}
