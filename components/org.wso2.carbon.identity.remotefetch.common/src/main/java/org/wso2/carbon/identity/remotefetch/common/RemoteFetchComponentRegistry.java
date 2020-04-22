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

import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerComponent;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerComponent;

import java.util.List;

/**
 * Interface for registry that allows to register RemoteFetch components.
 * This registry holds active remoteFetch Components which used to create relevant builders.
 * This registry is used by auto pull threads at runtime to get active component to build listeners.
 * This registry is used by clients to list basic configurations in listView.
 *
 */
public interface RemoteFetchComponentRegistry {

    /**
     * Register repositoryManager Component into registry.
     * @param repositoryManagerComponent repositoryManagerComponent
     */
    void registerRepositoryManager(RepositoryManagerComponent repositoryManagerComponent);

    /**
     * Register ConfigDeployer Component into registry.
     * @param configDeployerComponent configDeployerComponent
     */
    void registerConfigDeployer(ConfigDeployerComponent configDeployerComponent);

    /**
     * Register ActionListener Component into registry.
     * @param actionListenerComponent
     */
    void registerActionListener(ActionListenerComponent actionListenerComponent);

    /**
     * deRegister RepositoryManager by identifier.
     * @param identifier unique identifier
     */
    void deRegisterRepositoryManager(String identifier);

    /**
     * deRegister ConfigDeployer by identifier.
     * @param identifier unique identifier
     */
    void deRegisterConfigDeployer(String identifier);

    /**
     * deRegister ActionListener by identifier.
     * @param identifier unique identifier
     */
    void deRegisterActionListener(String identifier);

    /**
     * get RepositoryManagerComponent by identifier.
     * @param identifier identifier
     * @return RepositoryManagerComponent
     */
    RepositoryManagerComponent getRepositoryManagerComponent(String identifier);

    /**
     * get ConfigDeployerComponent by identifier.
     * @param identifier identifier
     * @return ConfigDeployerComponent
     */
    ConfigDeployerComponent getConfigDeployerComponent(String identifier);

    /**
     * get ActionListenerComponent  by identifier.
     * @param identifier identifier
     * @return ActionListenerComponent
     */
    ActionListenerComponent getActionListenerComponent(String identifier);

    /**
     * @return list all RepositoryManagerComponents.
     */
    List<RepositoryManagerComponent> getRepositoryManagerComponentList();

    /**
     * @return list all ConfigDeployerComponents.
     */
    List<ConfigDeployerComponent> getConfigDeployerComponentList();

    /**
     * @return list all ActionListenerComponents.
     */
    List<ActionListenerComponent> getActionListenerComponentList();
}
