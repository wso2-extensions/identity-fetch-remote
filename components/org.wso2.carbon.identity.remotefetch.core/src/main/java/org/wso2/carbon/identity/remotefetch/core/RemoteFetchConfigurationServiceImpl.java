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
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.ValidationReport;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListener;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilder;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilderException;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerBuilder;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerBuilderException;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerComponent;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerBuilder;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerBuilderException;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerComponent;
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
    private RemoteFetchComponentRegistry componentRegistry;
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
    public RemoteFetchConfiguration getRemoteFetchConfiguration(String fetchConfigurationId, String tenantDomain)
            throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
        return this.fetchConfigurationDAO.getRemoteFetchConfiguration(fetchConfigurationId, tenantId);
    }

    /**
     * @param tenantDomain TenantDomain.
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationList(String tenantDomain)
            throws RemoteFetchCoreException {

        return this.fetchConfigurationDAO.getBasicRemoteFetchConfigurationsByTenant(tenantDomain);
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
    public void deleteRemoteFetchConfiguration(String fetchConfigurationId, String tenantDomain)
            throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
        this.fetchConfigurationDAO.deleteRemoteFetchConfiguration(fetchConfigurationId, tenantId);
    }

    @Override
    public String triggerRemoteFetchConfiguration(String id, String tenantDomain) throws RemoteFetchCoreException {

        RemoteFetchConfiguration fetchConfiguration = getRemoteFetchConfiguration(id, tenantDomain);
        buildListener(fetchConfiguration).iteration();
        return fetchConfiguration.getRemoteFetchConfigurationId();

    }

    /**
     * Builds ActionListener object from RemoteFetchConfiguration.
     * Component registry holds relevant components and can be retrieve using remote fetch configuration attributes.
     * Components used get relevant builders and builders build required listener, manager and deployer
     * using given remote fetch configuration.
     * @param fetchConfig
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildListener(RemoteFetchConfiguration fetchConfig) throws RemoteFetchCoreException {

        componentRegistry = RemoteFetchServiceComponentHolder
                .getInstance().getRemoteFetchComponentRegistry();

        RepositoryManager repositoryManager;
        ActionListener actionListener;
        ConfigDeployer configDeployer;

        // Get an instance of RepositoryManager from registry.
        RepositoryManagerComponent repositoryManagerComponent = this.componentRegistry
                .getRepositoryManagerComponent(fetchConfig.getRepositoryManagerType());

        if (repositoryManagerComponent != null) {
            try {
                RepositoryManagerBuilder repositoryManagerBuilder = repositoryManagerComponent
                        .getRepositoryManagerBuilder();

                repositoryManager = repositoryManagerBuilder.addRemoteFetchConfig(fetchConfig)
                        .addRemoteFetchCoreConfig(RemoteFetchServiceComponentHolder.getInstance()
                                .getFetchCoreConfiguration())
                        .build();

            } catch (RepositoryManagerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + fetchConfig.getRepositoryManagerType()
                        + " RepositoryManager", e);
            }
        } else {
            throw new RemoteFetchCoreException("RepositoryManager " + fetchConfig.getRepositoryManagerType()
                    + " is not registered in RemoteFetchComponentRegistry");

        }

        // Get an instance of ConfigDeployer from registry.
        ConfigDeployerComponent configDeployerComponent = this.componentRegistry
                .getConfigDeployerComponent(fetchConfig.getConfigurationDeployerType());

        if (configDeployerComponent != null) {
            try {
                ConfigDeployerBuilder configDeployerBuilder = configDeployerComponent.getConfigDeployerBuilder();
                configDeployer = configDeployerBuilder.addRemoteFetchConfig(fetchConfig).build();

            } catch (ConfigDeployerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + fetchConfig.getConfigurationDeployerType()
                        + " ConfigDeployer object", e);
            }
        } else {
            throw new RemoteFetchCoreException("ConfigurationDeployer " + fetchConfig.getConfigurationDeployerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }

        // Get an instance of ActionListener from registry.
        ActionListenerComponent actionListenerComponent = this.componentRegistry
                .getActionListenerComponent(fetchConfig.getActionListenerType());

        if (actionListenerComponent != null) {
            try {
                ActionListenerBuilder actionListenerBuilder = this.componentRegistry
                        .getActionListenerComponent(fetchConfig.getActionListenerType())
                        .getActionListenerBuilder();

                actionListener = actionListenerBuilder.addRemoteFetchConfig(fetchConfig)
                        .addConfigDeployer(configDeployer).addRepositoryConnector(repositoryManager).build();

            } catch (ActionListenerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + fetchConfig.getActionListenerType()
                        + " ActionListener object", e);
            }
        } else {
            throw new RemoteFetchCoreException("ActionListener " + fetchConfig.getActionListenerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }

        return actionListener;
    }
}
