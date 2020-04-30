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
import org.wso2.carbon.identity.remotefetch.core.internal.RemoteFetchServiceComponentHolder;

/**
 * This class provide immediate task to be executed for trigger call of given remote fetch configuration.
 * It retrieves relevant action lister component, deployer component and repository manager component using given
 * remote fetch configuration object from componant registry.
 * It builds action listener, deployer and repository manager using relevant builders
 * It runs corresponding action listener iteration. *
 */
public class RemoteFetchConfigurationImmediateTask implements Runnable {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationImmediateTask.class);

    private RemoteFetchComponentRegistry componentRegistry = RemoteFetchServiceComponentHolder.getInstance()
            .getRemoteFetchComponentRegistry();

    private RemoteFetchConfiguration remoteFetchConfiguration;

    public RemoteFetchConfigurationImmediateTask(RemoteFetchConfiguration remoteFetchConfiguration) {

        this.remoteFetchConfiguration = remoteFetchConfiguration;
    }

    /**
     * This method is used to retrieve relevant components and build lister using given remote fetch configuration.
     * @return ActionListener
     * @throws RemoteFetchCoreException
     */
    private ActionListener buildListener() throws RemoteFetchCoreException {

        RepositoryManager repositoryManager;
        ActionListener actionListener;
        ConfigDeployer configDeployer;

        // Get an instance of RepositoryManager from registry.
        RepositoryManagerComponent repositoryManagerComponent = this.componentRegistry
                .getRepositoryManagerComponent(this.remoteFetchConfiguration.getRepositoryManagerType());

        if (repositoryManagerComponent != null) {
            try {
                RepositoryManagerBuilder repositoryManagerBuilder = repositoryManagerComponent
                        .getRepositoryManagerBuilder();

                repositoryManager = repositoryManagerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration)
                        .addRemoteFetchCoreConfig(RemoteFetchServiceComponentHolder.getInstance()
                                .getFetchCoreConfiguration())
                        .build();

            } catch (RepositoryManagerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                        .getRepositoryManagerType()
                        + " RepositoryManager", e);
            }
        } else {
            throw new RemoteFetchCoreException("RepositoryManager " + this.remoteFetchConfiguration
                    .getRepositoryManagerType()
                    + " is not registered in RemoteFetchComponentRegistry");

        }

        // Get an instance of ConfigDeployer from registry.
        ConfigDeployerComponent configDeployerComponent = this.componentRegistry
                .getConfigDeployerComponent(this.remoteFetchConfiguration.getConfigurationDeployerType());

        if (configDeployerComponent != null) {
            try {
                ConfigDeployerBuilder configDeployerBuilder = configDeployerComponent.getConfigDeployerBuilder();
                configDeployer = configDeployerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration).build();

            } catch (ConfigDeployerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                        .getConfigurationDeployerType()
                        + " ConfigDeployer object", e);
            }
        } else {
            throw new RemoteFetchCoreException("ConfigurationDeployer " + this.remoteFetchConfiguration
                    .getConfigurationDeployerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }

        // Get an instance of ActionListener from registry.
        ActionListenerComponent actionListenerComponent = this.componentRegistry
                .getActionListenerComponent(this.remoteFetchConfiguration
                        .getActionListenerType());

        if (actionListenerComponent != null) {
            try {
                ActionListenerBuilder actionListenerBuilder = this.componentRegistry
                        .getActionListenerComponent(this.remoteFetchConfiguration.getActionListenerType())
                        .getActionListenerBuilder();

                actionListener = actionListenerBuilder.addRemoteFetchConfig(this.remoteFetchConfiguration)
                        .addConfigDeployer(configDeployer).addRepositoryConnector(repositoryManager).build();

            } catch (ActionListenerBuilderException e) {
                throw new RemoteFetchCoreException("Unable to build " + this.remoteFetchConfiguration
                        .getActionListenerType()
                        + " ActionListener object", e);
            }
        } else {
            throw new RemoteFetchCoreException("ActionListener " + this.remoteFetchConfiguration
                    .getActionListenerType()
                    + " is not registered in RemoteFetchComponentRegistry");
        }

        return actionListener;
    }
    @Override
    public void run() {

        try {
            this.buildListener().iteration();
        } catch (RemoteFetchCoreException e) {
            log.error("Unable to trigger RemoteFetchConfigurations", e);

        }
    }
}
