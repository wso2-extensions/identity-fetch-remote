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

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerComponent;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Registry for remote fetch component.
 */
public class RemoteFetchComponentRegistryImpl implements RemoteFetchComponentRegistry {

    private HashMap<String, RepositoryManagerComponent> repositoryManagerComponentMap = new HashMap<>();
    private HashMap<String, ConfigDeployerComponent> configDeployerComponentMap = new HashMap<>();
    private HashMap<String, ActionListenerComponent> actionListenerComponentMap = new HashMap<>();

    /**
     * Register RepositoryManagerComponent inorder to retrieve later for UI render purposes.
     *
     * @param repositoryManagerComponent RepositoryManagerComponent
     */
    @Override
    public void registerRepositoryManager(RepositoryManagerComponent repositoryManagerComponent) {

        this.repositoryManagerComponentMap.put(repositoryManagerComponent.getIdentifier(), repositoryManagerComponent);
    }

    /**
     * Register ConfigDeployerComponent inorder to retrieve later for UI render purposes.
     *
     * @param configDeployerComponent ConfigDeployerComponent
     */
    @Override
    public void registerConfigDeployer(ConfigDeployerComponent configDeployerComponent) {

        this.configDeployerComponentMap.put(configDeployerComponent.getIdentifier(), configDeployerComponent);
    }

    /**
     * Register ActionListenerComponent inorder to retrieve later for UI render purposes.
     *
     * @param actionListenerComponent ActionListenerComponent
     */
    @Override
    public void registerActionListener(ActionListenerComponent actionListenerComponent) {

        this.actionListenerComponentMap.put(actionListenerComponent.getIdentifier(), actionListenerComponent);
    }

    /**
     * Remove repository manager from the registry.
     *
     * @param identifier id of the repository manager
     */
    @Override
    public void deRegisterRepositoryManager(String identifier) {

        this.repositoryManagerComponentMap.remove(identifier);
    }

    /**
     * Remove Config deployer from the registry.
     *
     * @param identifier id of the config deployer
     */
    @Override
    public void deRegisterConfigDeployer(String identifier) {

        this.configDeployerComponentMap.remove(identifier);
    }

    /**
     * Remove Action listener from the registry.
     *
     * @param identifier id of the action listener
     */
    @Override
    public void deRegisterActionListener(String identifier) {

        this.actionListenerComponentMap.remove(identifier);
    }

    /**
     * Get registered RepositoryManagerComponent
     *
     * @param identifier id of the repository manager
     * @return RepositoryManagerComponent
     */
    @Override
    public RepositoryManagerComponent getRepositoryManagerComponent(String identifier) {

        return this.repositoryManagerComponentMap.getOrDefault(identifier, null);
    }

    /**
     * Get registered ConfigDeployerComponent
     *
     * @param identifier id of the ConfigDeployer
     * @return ConfigDeployerComponent
     */
    @Override
    public ConfigDeployerComponent getConfigDeployerComponent(String identifier) {

        return this.configDeployerComponentMap.getOrDefault(identifier, null);
    }

    /**
     * Get registered ActionListenerComponent
     *
     * @param identifier id of the ActionListenerComponent
     * @return ActionListenerComponent
     */
    @Override
    public ActionListenerComponent getActionListenerComponent(String identifier) {

        return this.actionListenerComponentMap.getOrDefault(identifier, null);
    }

    /**
     * Get all RepositoryManagerComponents.
     *
     * @return list all RepositoryManagerComponents
     */
    @Override
    public List<RepositoryManagerComponent> getRepositoryManagerComponentList() {

        return new ArrayList(this.repositoryManagerComponentMap.values());
    }

    /**
     * Get all ConfigDeployerComponents.
     *
     * @return list all ConfigDeployerComponents
     */
    @Override
    public List<ConfigDeployerComponent> getConfigDeployerComponentList() {

        return new ArrayList(this.configDeployerComponentMap.values());
    }

    /**
     * Get all ActionListenerComponents.
     *
     * @return list all ActionListenerComponents
     */
    @Override
    public List<ActionListenerComponent> getActionListenerComponentList() {

        return new ArrayList(this.actionListenerComponentMap.values());
    }
}
