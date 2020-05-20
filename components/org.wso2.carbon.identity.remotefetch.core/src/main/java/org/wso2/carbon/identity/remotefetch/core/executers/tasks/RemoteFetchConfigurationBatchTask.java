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

package org.wso2.carbon.identity.remotefetch.core.executers.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
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
import org.wso2.carbon.identity.remotefetch.core.internal.RemoteFetchServiceComponentHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves RemoteFetchConfigurations and builds ActionListeners to be executed.
 */
public class RemoteFetchConfigurationBatchTask implements Runnable {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationBatchTask.class);

    private RemoteFetchConfigurationService fetchConfigurationService;

    private Map<String, RemoteFetchConfiguration> remoteFetchConfigurationMap = new HashMap<>();
    private Map<String, ActionListener> actionListenerMap = new HashMap<>();
    private RemoteFetchComponentRegistry componentRegistry;

    public RemoteFetchConfigurationBatchTask() {

        this.componentRegistry = RemoteFetchServiceComponentHolder.getInstance().getRemoteFetchComponentRegistry();
        this.fetchConfigurationService = RemoteFetchServiceComponentHolder.getInstance()
                .getRemoteFetchConfigurationService();
    }

    /**
     * Builds ActionListener object from RemoteFetchConfiguration.
     *
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildListener(RemoteFetchConfiguration remoteFetchConfiguration)
                                         throws RemoteFetchCoreException {

        RepositoryManager repositoryManager = buildRepositoryManager(remoteFetchConfiguration);
        ConfigDeployer configDeployer = buildConfigDeployer(remoteFetchConfiguration);

        return buildActionListener(configDeployer, repositoryManager, remoteFetchConfiguration);
    }

    /**
     * This method is used to build repository manager for particular remote fetch configuration.
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private RepositoryManager buildRepositoryManager(RemoteFetchConfiguration remoteFetchConfiguration)
                                                     throws RemoteFetchCoreException {

        RepositoryManagerComponent repositoryManagerComponent =
                getRepositoryManagerComponentFromRegistry(remoteFetchConfiguration);
        if (repositoryManagerComponent != null) {

            return buildRepositoryManagerFromComponent(repositoryManagerComponent, remoteFetchConfiguration);

        } else {
            throw new RemoteFetchCoreException("RepositoryManager " + remoteFetchConfiguration
                    .getRepositoryManagerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve RepositoryManagerComponent from registry for particular remoteFetchConfiguration.
     * @param remoteFetchConfiguration
     * @return
     */
    private RepositoryManagerComponent getRepositoryManagerComponentFromRegistry
                (RemoteFetchConfiguration remoteFetchConfiguration) {

        return this.componentRegistry
                .getRepositoryManagerComponent(remoteFetchConfiguration.getRepositoryManagerType());
    }

    /**
     * This method is used to build repository manager from RepositoryManagerBuilder.
     * @param repositoryManagerComponent
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private RepositoryManager buildRepositoryManagerFromComponent(RepositoryManagerComponent repositoryManagerComponent,
                                                                  RemoteFetchConfiguration remoteFetchConfiguration)
            throws RemoteFetchCoreException {

        try {
            RepositoryManagerBuilder repositoryManagerBuilder = repositoryManagerComponent
                    .getRepositoryManagerBuilder();

            return repositoryManagerBuilder.addRemoteFetchConfig(remoteFetchConfiguration)
                    .addRemoteFetchCoreConfig(RemoteFetchServiceComponentHolder.getInstance()
                            .getFetchCoreConfiguration())
                    .build();

        } catch (RepositoryManagerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + remoteFetchConfiguration
                    .getRepositoryManagerType()
                    + " RepositoryManager", e);
        }
    }

    /**
     * This method is used to build ConfigDeployer for particular remote fetch configuration.
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private ConfigDeployer buildConfigDeployer(RemoteFetchConfiguration remoteFetchConfiguration)
            throws RemoteFetchCoreException {

        // Get an instance of ConfigDeployer from registry.
        ConfigDeployerComponent configDeployerComponent =
                getConfigDeployerComponentFromRegistry(remoteFetchConfiguration);

        if (configDeployerComponent != null) {

            return buildConfigDeployerFromComponent(configDeployerComponent, remoteFetchConfiguration);

        } else {
            throw new RemoteFetchCoreException("ConfigurationDeployer " + remoteFetchConfiguration
                    .getConfigurationDeployerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve ConfigDeployerComponent from registry for particular remoteFetchConfiguration.
     * @param remoteFetchConfiguration
     * @return
     */
    private ConfigDeployerComponent getConfigDeployerComponentFromRegistry
                                    (RemoteFetchConfiguration remoteFetchConfiguration) {

        return this.componentRegistry
                .getConfigDeployerComponent(remoteFetchConfiguration.getConfigurationDeployerType());

    }

    /**
     * This method is used to build ConfigDeployer from ConfigDeployerBuilder.
     * @param configDeployerComponent
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private ConfigDeployer buildConfigDeployerFromComponent(ConfigDeployerComponent configDeployerComponent,
                                                            RemoteFetchConfiguration remoteFetchConfiguration)
            throws RemoteFetchCoreException {

        try {
            ConfigDeployerBuilder configDeployerBuilder = configDeployerComponent.getConfigDeployerBuilder();
            return configDeployerBuilder.addRemoteFetchConfig(remoteFetchConfiguration).build();

        } catch (ConfigDeployerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + remoteFetchConfiguration
                    .getConfigurationDeployerType()
                    + " ConfigDeployer object", e);
        }
    }

    /**
     * This method is used create action listener from given ConfigDeployer and RepositoryManager.
     * @param configDeployer
     * @param repositoryManager
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildActionListener(ConfigDeployer configDeployer, RepositoryManager repositoryManager,
                                               RemoteFetchConfiguration remoteFetchConfiguration)
            throws RemoteFetchCoreException {

        ActionListenerComponent actionListenerComponent =
                getActionListenerComponentFromRegistry(remoteFetchConfiguration);
        if (actionListenerComponent != null) {

            return buildActionListenerFromComponent(actionListenerComponent,
                    configDeployer, repositoryManager, remoteFetchConfiguration);

        } else {
            throw new RemoteFetchCoreException("ActionListener " + remoteFetchConfiguration
                    .getActionListenerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve ActionListenerComponent from registry for particular remoteFetchConfiguration.
     * @param remoteFetchConfiguration
     * @return
     */
    private ActionListenerComponent getActionListenerComponentFromRegistry
                                (RemoteFetchConfiguration remoteFetchConfiguration) {

        return this.componentRegistry.getActionListenerComponent(remoteFetchConfiguration
                .getActionListenerType());
    }

    /**
     * This method is used to build ActionListener from ActionListenerFromComponent.
     * @param actionListenerComponent
     * @param configDeployer
     * @param repositoryManager
     * @param remoteFetchConfiguration
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildActionListenerFromComponent(ActionListenerComponent actionListenerComponent,
                                                            ConfigDeployer configDeployer,
                                                            RepositoryManager repositoryManager,
                                                            RemoteFetchConfiguration remoteFetchConfiguration)
                                                            throws RemoteFetchCoreException {

        try {
            ActionListenerBuilder actionListenerBuilder = actionListenerComponent.getActionListenerBuilder();

            return actionListenerBuilder.addRemoteFetchConfig(remoteFetchConfiguration)
                    .addConfigDeployer(configDeployer).addRepositoryConnector(repositoryManager).build();

        } catch (ActionListenerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + remoteFetchConfiguration
                    .getActionListenerType()
                    + " ActionListener object", e);
        }
    }

    /**
     * Load RemoteFetch Configurations from database and builds ActionListeners or re-builds if updated.
     */
    private void loadListeners() {

        try {
            this.fetchConfigurationService.getEnabledPollingRemoteFetchConfigurationList()
                    .forEach((RemoteFetchConfiguration config) -> {
                String configurationId = config.getRemoteFetchConfigurationId();
                // Check if RemoteFetchConfig already exists in Map.
                if (this.remoteFetchConfigurationMap.containsKey(configurationId)) {
                    // Update RemoteFetchConfiguration if local and new config is different.
                    if (!remoteFetchConfigurationMap.get(configurationId).equals(config)) {
                        try {
                            this.actionListenerMap.put(configurationId, this.buildListener(config));
                        } catch (RemoteFetchCoreException e) {
                            log.error("Exception re-building ActionListener " + config.getActionListenerType(), e);
                        }
                    }
                } else {
                    this.remoteFetchConfigurationMap.put(configurationId, config);
                    try {
                        this.actionListenerMap.put(configurationId, this.buildListener(config));
                    } catch (RemoteFetchCoreException e) {
                        log.error("Exception building ActionListener " + config.getActionListenerType(), e);
                    }
                }
            });
        } catch (RemoteFetchCoreException e) {
            log.error("Unable to list RemoteFetchConfigurations", e);
        }
    }

    @Override
    public void run() {

        loadListeners();
        for (ActionListener actionListener : this.actionListenerMap.values()) {
            actionListener.execute();
        }
    }

    /**
     * Remove entries from hash maps while delete remote fetch configuration.
     * @param id
     */
    public void deleteRemoteFetchConfiguration(String id) {

        this.actionListenerMap.remove(id);
        this.remoteFetchConfigurationMap.remove(id);
    }
}
