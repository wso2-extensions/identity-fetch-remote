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

package org.wso2.carbon.identity.remotefetch.core;

import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.ValidationReport;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.dao.RemoteFetchConfigurationDAO;
import org.wso2.carbon.identity.remotefetch.core.dao.impl.RemoteFetchConfigurationDAOImpl;
import org.wso2.carbon.identity.remotefetch.core.internal.RemoteFetchServiceComponentHolder;
import org.wso2.carbon.identity.remotefetch.core.util.RemoteFetchConfigurationValidator;

import java.util.List;

/**
 * Service to manage RemoteFetchConfigurations.
 */
public class RemoteFetchConfigurationServiceImpl implements RemoteFetchConfigurationService {

    private RemoteFetchConfigurationDAO fetchConfigurationDAO = new RemoteFetchConfigurationDAOImpl();

    /**
     * @param fetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public ValidationReport addRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration)
            throws RemoteFetchCoreException {

        RemoteFetchConfigurationValidator validator =
                new RemoteFetchConfigurationValidator(RemoteFetchServiceComponentHolder.getInstance()
                        .getRemoteFetchComponentRegistry(), fetchConfiguration);

        ValidationReport validationReport = validator.validate();

        if (validationReport.getValidationStatus() == ValidationReport.ValidationStatus.PASSED) {
            this.fetchConfigurationDAO.createRemoteFetchConfiguration(fetchConfiguration);
        }

        return validationReport;
    }

    /**
     * @param fetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public ValidationReport updateRemoteFetchConfiguration(RemoteFetchConfiguration fetchConfiguration)
            throws RemoteFetchCoreException {

        RemoteFetchConfigurationValidator validator =
                new RemoteFetchConfigurationValidator(RemoteFetchServiceComponentHolder.getInstance()
                        .getRemoteFetchComponentRegistry(), fetchConfiguration);

        ValidationReport validationReport = validator.validate();

        if (validationReport.getValidationStatus() == ValidationReport.ValidationStatus.PASSED) {
            this.fetchConfigurationDAO.updateRemoteFetchConfiguration(fetchConfiguration);
        }

        return validationReport;
    }

    /**
     * @param fetchConfigurationId
     * @return Remote Fetch Configuration for id.
     * @throws RemoteFetchCoreException
     */
    @Override
    public RemoteFetchConfiguration getRemoteFetchConfiguration(int fetchConfigurationId)
            throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getRemoteFetchConfiguration(fetchConfigurationId);
    }

    /**
     * @param tenantId
     * @return Remote Fetch Configuration list for tenant id.
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getRemoteFetchConfigurationList(int tenantId)
            throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getRemoteFetchConfigurationsByTenant(tenantId);
    }

    /**
     * @param tenantId
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationList(int tenantId)
            throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getBasicRemoteFetchConfigurationsByTenant(tenantId);
    }

    /**
     * @return All Remote Fetch Configurations.
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getRemoteFetchConfigurationList() throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getAllRemoteFetchConfigurations();
    }

    /**
     * @return All Enabled Remote Fetch Configurations.
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getEnabledRemoteFetchConfigurationList() throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getAllEnabledRemoteFetchConfigurations();
    }

    /**
     * @param fetchConfigurationId
     * @throws RemoteFetchCoreException
     */
    @Override
    public void deleteRemoteFetchConfiguration(int fetchConfigurationId) throws RemoteFetchCoreException {

        this.fetchConfigurationDAO.deleteRemoteFetchConfiguration(fetchConfigurationId);
    }
}
