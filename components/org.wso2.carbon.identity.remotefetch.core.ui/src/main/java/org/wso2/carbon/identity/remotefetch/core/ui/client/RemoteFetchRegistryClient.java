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

    private static final String FIELD_REPOSITORY_MANAGER = "repositoryManager";
    private static final String FIELD_ACTION_LISTENER = "actionListener";
    private static final String FIELD_CONFIG_DEPLOYER = "configDeployer";

    public static Map<String, Map<String, List<UIField>>> getAllComponentUIFields() {

        Map<String, Map<String, List<UIField>>> allComponentFields = new HashMap<>();
        allComponentFields.put("repositoryManager", new HashMap<>());
        allComponentFields.put("actionListener", new HashMap<>());
        allComponentFields.put("configDeployer", new HashMap<>());

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getRepositoryManagerComponentList()
                .forEach(repositoryManagerComponent -> allComponentFields.get(FIELD_REPOSITORY_MANAGER)
                        .put(repositoryManagerComponent.getIdentifier(), repositoryManagerComponent.getUIFields()));

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getActionListenerComponentList()
                .forEach(actionListenerComponent -> allComponentFields.get(FIELD_ACTION_LISTENER)
                        .put(actionListenerComponent.getIdentifier(), actionListenerComponent.getUIFields()));

        RemotefetchCoreUIComponentDataHolder.getInstance().getComponentRegistry().getConfigDeployerComponentList()
                .forEach(actionListenerComponent -> allComponentFields.get(FIELD_CONFIG_DEPLOYER)
                        .put(actionListenerComponent.getIdentifier(), actionListenerComponent.getUIFields()));

        return allComponentFields;
    }

}
