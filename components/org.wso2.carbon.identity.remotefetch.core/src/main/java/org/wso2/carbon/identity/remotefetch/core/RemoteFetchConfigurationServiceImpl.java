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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.ValidationReport;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.dao.RemoteFetchConfigurationDAO;
import org.wso2.carbon.identity.remotefetch.core.dao.impl.RemoteFetchConfigurationDAOImpl;
import org.wso2.carbon.identity.remotefetch.core.internal.RemoteFetchServiceComponentHolder;
import org.wso2.carbon.identity.remotefetch.core.util.RemoteFetchConfigurationUtils;
import org.wso2.carbon.identity.remotefetch.core.util.RemoteFetchConfigurationValidator;

import java.util.List;

/**
 * Service to manage RemoteFetchConfigurations.
 */
public class RemoteFetchConfigurationServiceImpl implements RemoteFetchConfigurationService {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationServiceImpl.class);

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
            String remoteConfigurationId = RemoteFetchConfigurationUtils.generateUniqueID();
            if (log.isDebugEnabled()) {
                log.debug("Remote Configuration ID is  generated: " + remoteConfigurationId);
            }
            fetchConfiguration.setRemoteFetchConfigurationId(remoteConfigurationId);
            this.fetchConfigurationDAO.createRemoteFetchConfiguration(fetchConfiguration);
            validationReport.setId(remoteConfigurationId);
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
            validationReport.setId(fetchConfiguration.getRemoteFetchConfigurationId());
        }

        return validationReport;
    }

    /**
     * @param fetchConfigurationId
     * @return Remote Fetch Configuration for id.
     * @throws RemoteFetchCoreException
     */
    @Override
    public RemoteFetchConfiguration getRemoteFetchConfiguration(String fetchConfigurationId)
            throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        return this.fetchConfigurationDAO.getRemoteFetchConfiguration(fetchConfigurationId, tenantId);
    }

    /**
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationList()
            throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getBasicRemoteFetchConfigurationsByTenant
                (CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
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
    public void deleteRemoteFetchConfiguration(String fetchConfigurationId)
            throws RemoteFetchCoreException {

        RemoteFetchServiceComponentHolder.getInstance().getRemoteFetchTaskExecutor()
                .deleteRemoteFetchConfigurationFromBatchTask(fetchConfigurationId);

        int tenantId = IdentityTenantUtil.getTenantId(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        this.fetchConfigurationDAO.deleteRemoteFetchConfiguration(fetchConfigurationId, tenantId);
    }

    /**
     * This method used to get remote fetch configuration for given id and start an Immediate task execution.
     * @param fetchConfigurationId
     * @throws RemoteFetchCoreException
     */
    @Override
    public void triggerRemoteFetch(String fetchConfigurationId) throws RemoteFetchCoreException {

        RemoteFetchConfiguration remoteFetchConfiguration = this.getRemoteFetchConfiguration(fetchConfigurationId);
        if (remoteFetchConfiguration != null) {
            RemoteFetchServiceComponentHolder.getInstance().getRemoteFetchTaskExecutor()
                    .startImmediateTaskExecution(remoteFetchConfiguration);

            if (log.isDebugEnabled()) {
                log.debug("Immediate Task was created and executed for : " + fetchConfigurationId);
            }
        }

    }

}
