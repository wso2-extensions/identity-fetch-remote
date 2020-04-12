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

package org.wso2.carbon.identity.remotefetch.core.dao;

import org.json.JSONObject;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;

import java.util.HashMap;
import java.util.Map;

public class TestConstants {

    public static final String USER_NAME = "admin";
    public static final String REPO_MANAGER_TYPE = "GIT";
    public static final String ACTION_LISTENER_TYPE = "POLLING";
    public static final String CONFIG_DEPLOYER_TYPE = "SP";
    public static final int TENANT_ID = -1234;

    public static Map<String, String> repositoryManagerAttributes = new HashMap<>();
    public static Map<String, String> actionListenerAttributes = new HashMap<>();
    public static Map<String, String> configurationDeployerAttributes = new HashMap<>();

    public static JSONObject getAttributesJson() {

        int remoteFetchConfigurationId = -1;
        RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration(remoteFetchConfigurationId,
                TENANT_ID, false, USER_NAME, null, null,
                null,
                null);

        repositoryManagerAttributes.put("accessToken", "1234");
        repositoryManagerAttributes.put("userName", "IS");
        repositoryManagerAttributes.put("uri", "https://github.com/IS/Test2.git");
        repositoryManagerAttributes.put("branch", "master");
        repositoryManagerAttributes.put("directory", "sp/");
        actionListenerAttributes.put("frequency", "60");
        configurationDeployerAttributes.put("", "");

        remoteFetchConfiguration.setRepositoryManagerAttributes(repositoryManagerAttributes);
        remoteFetchConfiguration.setConfigurationDeployerAttributes(configurationDeployerAttributes);
        remoteFetchConfiguration.setActionListenerAttributes(actionListenerAttributes);

        JSONObject attributesBundle = new JSONObject();
        attributesBundle.put("repositoryManagerAttributes",
                remoteFetchConfiguration.getRepositoryManagerAttributes());
        attributesBundle.put("confgiurationDeployerAttributes",
                remoteFetchConfiguration.getConfigurationDeployerAttributes());
        attributesBundle.put("actionListenerAttributes", remoteFetchConfiguration.getActionListenerAttributes());

        return attributesBundle;
    }
}
