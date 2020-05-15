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

package org.wso2.carbon.identity.remotefetch.core.executers.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
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

/**
 * This class provide immediate task to be executed for trigger call of given remote fetch configuration.
 * It retrieves relevant action lister component, deployer component and repository manager component using given
 * remote fetch configuration object from componant registry.
 * It builds action listener, deployer and repository manager using relevant builders
 * It runs corresponding action listener iteration. *
 */
public class RemoteFetchConfigurationImmediateTask implements Runnable {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationImmediateTask.class);

    private RemoteFetchComponentRegistry componentRegistry;

    private RemoteFetchConfiguration remoteFetchConfiguration;
    private String id;
    private RemoteFetchConfigurationDAO remoteFetchConfigurationDAO;

    public RemoteFetchConfigurationImmediateTask(RemoteFetchConfiguration remoteFetchConfiguration) {

        this.remoteFetchConfiguration = remoteFetchConfiguration;
        this.componentRegistry = RemoteFetchServiceComponentHolder.getInstance()
                .getRemoteFetchComponentRegistry();
        this.id = RemoteFetchConfigurationUtils.generateUniqueID();
        this.remoteFetchConfigurationDAO = new RemoteFetchConfigurationDAOImpl();
    }

    /**
     * This method is used to return task id.
     * @return
     */
    public String getId() {

        return id;
    }

    /**
     * This method is used create action listener for particular remote fetch configuration.
     * @return ActionListener
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildListener() throws RemoteFetchCoreException {

        RepositoryManager repositoryManager = buildRepositoryManager();
        ConfigDeployer configDeployer = buildConfigDeployer();

        return buildActionListener(configDeployer, repositoryManager);
    }

    /**
     * This method is used to build repository manager for particular remote fetch configuration.
     * @return
     * @throws RemoteFetchCoreException
     */
    private RepositoryManager buildRepositoryManager() throws RemoteFetchCoreException {

        RepositoryManagerComponent repositoryManagerComponent = getRepositoryManagerComponentFromRegistry();
        if (repositoryManagerComponent != null) {

            return buildRepositoryManagerFromComponent(repositoryManagerComponent);

        } else {
            throw new RemoteFetchCoreException("RepositoryManager " + this.remoteFetchConfiguration
                    .getRepositoryManagerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve RepositoryManagerComponent from registry for particular remoteFetchConfiguration.
     * @return
     */
    private RepositoryManagerComponent getRepositoryManagerComponentFromRegistry() {

        return this.componentRegistry
                .getRepositoryManagerComponent(this.remoteFetchConfiguration.getRepositoryManagerType());
    }

    /**
     * This method is used to build repository manager from RepositoryManagerBuilder.
     * @param repositoryManagerComponent
     * @return
     * @throws RemoteFetchCoreException
     */
    private RepositoryManager buildRepositoryManagerFromComponent(RepositoryManagerComponent repositoryManagerComponent)
            throws RemoteFetchCoreException {

        try {
            RepositoryManagerBuilder repositoryManagerBuilder = repositoryManagerComponent
                    .getRepositoryManagerBuilder();

            return repositoryManagerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration)
                    .addRemoteFetchCoreConfig(RemoteFetchServiceComponentHolder.getInstance()
                            .getFetchCoreConfiguration())
                    .build();

        } catch (RepositoryManagerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                    .getRepositoryManagerType()
                    + " RepositoryManager", e);
        }
    }

    /**
     * This method is used to build ConfigDeployer for particular remote fetch configuration.
     * @return
     * @throws RemoteFetchCoreException
     */
    private ConfigDeployer buildConfigDeployer() throws RemoteFetchCoreException {

        // Get an instance of ConfigDeployer from registry.
        ConfigDeployerComponent configDeployerComponent = getConfigDeployerComponentFromRegistry();

        if (configDeployerComponent != null) {

            return buildConfigDeployerFromComponent(configDeployerComponent);

        } else {
            throw new RemoteFetchCoreException("ConfigurationDeployer " + this.remoteFetchConfiguration
                    .getConfigurationDeployerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve ConfigDeployerComponent from registry for particular remoteFetchConfiguration.
     * @return
     */
    private ConfigDeployerComponent getConfigDeployerComponentFromRegistry() {

        return this.componentRegistry
                .getConfigDeployerComponent(this.remoteFetchConfiguration.getConfigurationDeployerType());
    }

    /**
     * This method is used to build ConfigDeployer from ConfigDeployerBuilder.
     * @param configDeployerComponent
     * @return
     * @throws RemoteFetchCoreException
     */
    private ConfigDeployer buildConfigDeployerFromComponent(ConfigDeployerComponent configDeployerComponent)
            throws RemoteFetchCoreException {

        try {
            ConfigDeployerBuilder configDeployerBuilder = configDeployerComponent.getConfigDeployerBuilder();
            return configDeployerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration).build();

        } catch (ConfigDeployerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                    .getConfigurationDeployerType()
                    + " ConfigDeployer object", e);
        }
    }

    /**
     * This method is used create action listener from given ConfigDeployer and RepositoryManager.
     * @param configDeployer
     * @param repositoryManager
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildActionListener(ConfigDeployer configDeployer, RepositoryManager repositoryManager)
                                               throws RemoteFetchCoreException {

        ActionListenerComponent actionListenerComponent = getActionListenerComponentFromRegistry();
        if (actionListenerComponent != null) {

            return buildActionListenerFromComponent(actionListenerComponent,
                    configDeployer, repositoryManager);

        } else {
            throw new RemoteFetchCoreException("ActionListener " + this.remoteFetchConfiguration
                    .getActionListenerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }
    }

    /**
     * This method is used to retrieve ActionListenerComponent from registry for particular remoteFetchConfiguration.
     * @return
     */
    private ActionListenerComponent getActionListenerComponentFromRegistry() {

        return this.componentRegistry.getActionListenerComponent(this.remoteFetchConfiguration
                        .getActionListenerType());
    }

    /**
     * This method is used to build ActionListener from ActionListenerFromComponent.
     * @param actionListenerComponent
     * @param configDeployer
     * @param repositoryManager
     * @return
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildActionListenerFromComponent(ActionListenerComponent actionListenerComponent,
                                                            ConfigDeployer configDeployer,
                                                            RepositoryManager repositoryManager)
                                                throws RemoteFetchCoreException {

        try {
            ActionListenerBuilder actionListenerBuilder = actionListenerComponent.getActionListenerBuilder();

            return actionListenerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration)
                    .addConfigDeployer(configDeployer).addRepositoryConnector(repositoryManager).build();

        } catch (ActionListenerBuilderException e) {
            throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                    .getActionListenerType()
                    + " ActionListener object", e);
        }
    }

    @Override
    public void run() {

        try {
            this.buildListener().iteration();
            remoteFetchConfiguration.setTriggerId(this.id);
            this.remoteFetchConfigurationDAO.updateRemoteFetchConfigurationTriggerId(remoteFetchConfiguration);
        } catch (RemoteFetchCoreException e) {
            log.error("Unable to trigger RemoteFetchConfigurations", e);
        }
    }
}
