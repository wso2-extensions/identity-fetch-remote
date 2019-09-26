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

package org.wso2.carbon.identity.remotefetch.core.ui.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerComponent;
import org.wso2.carbon.identity.remotefetch.common.ui.UIField;
import org.wso2.carbon.identity.remotefetch.core.ui.dto.RemoteFetchComponentDTO;
import org.wso2.carbon.identity.remotefetch.core.ui.internal.RemotefetchCoreUIComponentDataHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteFetchRegistryClient {

    private static Log log = LogFactory.getLog(RemoteFetchRegistryClient.class);
    private static final String REPO_MANAGER = "repo_manager";
    private static final String ACTION_LISTENER = "action_listener";
    private static final String CONFIG_DEPLOYER = "config_deployer";

    private static final String FIELD_REPOSITORY_MANAGER = "repositoryManager";
    private static final String FIELD_ACTION_LISTENER = "actionListener";
    private static final String FIELD_CONFIG_DEPLOYER = "configDeployer";

    public static List<RemoteFetchComponentDTO> getRepositoryManagers() {

        RemoteFetchComponentRegistry remoteFetchComponentRegistry =
                RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry();
        if (remoteFetchComponentRegistry == null) {
            log.error("Remote Repository component is not activated properly. RemoteFetchComponentRegistry is null");
            return Collections.emptyList();
        }
        if (remoteFetchComponentRegistry.getRepositoryManagerComponentList() == null) {
            log.error("Remote Repository component is not activated properly. RepositoryManagerComponentList is null");
            return Collections.emptyList();
        }
        return remoteFetchComponentRegistry.
                getRepositoryManagerComponentList().stream().map(repositoryManagerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.REPOSITORY_MANAGER,
                        repositoryManagerComponent.getIdentifier(),
                        repositoryManagerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<RemoteFetchComponentDTO> getActionListener() {

        RemoteFetchComponentRegistry remoteFetchComponentRegistry =
                RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry();
        if (remoteFetchComponentRegistry == null) {
            log.error("Remote Repository component is not activated properly. RemoteFetchComponentRegistry is null");
            return Collections.emptyList();
        }
        if (remoteFetchComponentRegistry.getActionListenerComponentList() == null) {
            log.error("Remote Repository component is not activated properly. ActionListenerComponentList is null");
            return Collections.emptyList();
        }

        return remoteFetchComponentRegistry.
                getActionListenerComponentList().stream().map(actionListenerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.ACTION_LISTENER,
                        actionListenerComponent.getIdentifier(),
                        actionListenerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<RemoteFetchComponentDTO> getConfigDeployer() {

        RemoteFetchComponentRegistry remoteFetchComponentRegistry =
                RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry();
        if (remoteFetchComponentRegistry == null) {
            log.error("Remote Repository component is not activated properly. RemoteFetchComponentRegistry is null");
            return Collections.emptyList();
        }
        if (remoteFetchComponentRegistry.getActionListenerComponentList() == null) {
            log.error("Remote Repository component is not activated properly. ConfigDeployerComponentList is null");
            return Collections.emptyList();
        }
        return remoteFetchComponentRegistry.
                getConfigDeployerComponentList().stream().map(configDeployerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.CONFIG_DEPLOYER,
                        configDeployerComponent.getIdentifier(),
                        configDeployerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<UIField> getUIFields(String type, String identifier) {

        switch (type) {
            case REPO_MANAGER:
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getRepositoryManagerComponent(identifier).getUIFields();
            case ACTION_LISTENER:
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getActionListenerComponent(identifier).getUIFields();
            case CONFIG_DEPLOYER:
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getConfigDeployerComponent(identifier).getUIFields();
            default:
                return new ArrayList();
        }
    }

    public static Map<String, Map<String, List<UIField>>> getAllComponentUIFields() {

        Map<String, Map<String, List<UIField>>> allComponentFields = new HashMap<>();
        allComponentFields.put(FIELD_REPOSITORY_MANAGER, new HashMap<>());
        allComponentFields.put(FIELD_ACTION_LISTENER, new HashMap<>());
        allComponentFields.put(FIELD_CONFIG_DEPLOYER, new HashMap<>());

        RemoteFetchComponentRegistry remoteFetchComponentRegistry =
                RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry();
        if (remoteFetchComponentRegistry == null) {
            log.error("Remote Repository component is not activated properly. RemoteFetchComponentRegistry is null");
            return allComponentFields;
        }

        List<RepositoryManagerComponent> repositoryManagerComponentList = remoteFetchComponentRegistry.
                getRepositoryManagerComponentList();

        if (repositoryManagerComponentList == null) {
            log.error("Remote Repository component is not activated properly. RepositoryManagerComponentList is null");
            return allComponentFields;
        }

        repositoryManagerComponentList
                .forEach(repositoryManagerComponent -> allComponentFields.get(FIELD_REPOSITORY_MANAGER)
                        .put(repositoryManagerComponent.getIdentifier(), repositoryManagerComponent.getUIFields()));

        repositoryManagerComponentList
                .forEach(actionListenerComponent -> allComponentFields.get(FIELD_ACTION_LISTENER)
                        .put(actionListenerComponent.getIdentifier(), actionListenerComponent.getUIFields()));

        repositoryManagerComponentList
                .forEach(actionListenerComponent -> allComponentFields.get(FIELD_CONFIG_DEPLOYER)
                        .put(actionListenerComponent.getIdentifier(), actionListenerComponent.getUIFields()));

        return allComponentFields;
    }

}
