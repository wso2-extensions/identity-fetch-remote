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

import org.wso2.carbon.identity.remotefetch.common.ui.UIField;
import org.wso2.carbon.identity.remotefetch.core.ui.dto.RemoteFetchComponentDTO;
import org.wso2.carbon.identity.remotefetch.core.ui.internal.RemotefetchCoreUIComponentDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteFetchRegistryClient {

    public static List<RemoteFetchComponentDTO> getRepositoryManagers() {

        return RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().
                getRepositoryManagerComponentList().stream().map(repositoryManagerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.REPOSITORY_MANAGER,
                        repositoryManagerComponent.getIdentifier(),
                        repositoryManagerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<RemoteFetchComponentDTO> getActionListener() {

        return RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().
                getActionListenerComponentList().stream().map(actionListenerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.ACTION_LISTENER,
                        actionListenerComponent.getIdentifier(),
                        actionListenerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<RemoteFetchComponentDTO> getConfigDeployer() {

        return RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().
                getConfigDeployerComponentList().stream().map(configDeployerComponent ->
                new RemoteFetchComponentDTO(
                        RemoteFetchComponentDTO.COMPONENT_TYPE.CONFIG_DEPLOYER,
                        configDeployerComponent.getIdentifier(),
                        configDeployerComponent.getName()
                )
        ).collect(Collectors.toList());
    }

    public static List<UIField> getUIFields(String type, String identifier){
        switch (type){
            case "repo_manager":
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getRepositoryManagerComponent(identifier).getUIFields();
            case "action_listener":
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getActionListenerComponent(identifier).getUIFields();
            case "config_deployer":
                return RemotefetchCoreUIComponentDataHolder.getInstance()
                        .getComponentRegistry().getConfigDeployerComponent(identifier).getUIFields();
            default:
                return new ArrayList();
        }
    }

    public static Map<String,Map<String,List<UIField>>> getAllComponentUIFields(){
        Map<String,Map<String,List<UIField>>> allComponentFields = new HashMap<>();
        allComponentFields.put("repositoryManager", new HashMap<>());
        allComponentFields.put("actionListener", new HashMap<>());
        allComponentFields.put("configDeployer", new HashMap<>());

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getRepositoryManagerComponentList()
                .forEach(repositoryManagerComponent -> allComponentFields.get("repositoryManager")
                        .put(repositoryManagerComponent.getIdentifier(),repositoryManagerComponent.getUIFields()));

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getActionListenerComponentList()
                .forEach(actionListenerComponent -> allComponentFields.get("actionListener")
                        .put(actionListenerComponent.getIdentifier(),actionListenerComponent.getUIFields()));

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getConfigDeployerComponentList()
                .forEach(actionListenerComponent -> allComponentFields.get("configDeployer")
                        .put(actionListenerComponent.getIdentifier(),actionListenerComponent.getUIFields()));

        return allComponentFields;
    }

}
