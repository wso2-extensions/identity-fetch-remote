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

package org.wso2.carbon.identity.remotefetch.core;

/**
 * Definitions of few constants shared across with other components from this component.
 * You may not instantiate this class directly. However you can access the constants declared as static.
 */
public class RemoteFetchConstants {

    private RemoteFetchConstants() {
    }

    public static final String ATTRIBUTE_REPOSITORY_MANAGER = "repositoryManagerAttributes";
    public static final String ATTRIBUTE_ACTION_LISTENER = "actionListenerAttributes";
    public static final String ATTRIBUTE_CONFIG_DEPLOYER = "configurationDeployerAttributes";
    public static final int FACTOR_INDENT = 4;

    // Pagination constants.
    public static final int DEFAULT_MAXIMUM_ITEMS_PRE_PAGE = 30;
    public static final int DEFAULT_ITEMS_PRE_PAGE = 15;
    public static final String MAXIMUM_ITEMS_PRE_PAGE_PROPERTY = "MaximumItemsPerPage";
    public static final String DEFAULT_ITEMS_PRE_PAGE_PROPERTY = "DefaultItemsPerPage";

    /**
     * Grouping of constants related to database table names.
     */
    public static class RemoteFetchTableConstants {

        private RemoteFetchTableConstants() {
        }

        public static final String ID = "ID";
        public static final String TENANT_ID = "TENANT_ID";
        public static final String IS_ENABLED = "IS_ENABLED";
        public static final String USER_NAME = "USER_NAME";
        public static final String REPO_MANAGER_TYPE = "REPO_MANAGER_TYPE";
        public static final String ACTION_LISTENER_TYPE = "ACTION_LISTENER_TYPE";
        public static final String CONFIG_DEPLOYER_TYPE = "CONFIG_DEPLOYER_TYPE";
        public static final String REMOTE_FETCH_NAME = "REMOTE_FETCH_NAME";
        public static final String ATTRIBUTES_JSON = "ATTRIBUTES_JSON";

        public static final String CONFIG_ID = "CONFIG_ID";
        public static final String FILE_PATH = "FILE_PATH";
        public static final String FILE_HASH = "FILE_HASH";
        public static final String DEPLOYED_DATE = "DEPLOYED_DATE";
        public static final String DEPLOYMENT_STATUS = "DEPLOYMENT_STATUS";
        public static final String ITEM_NAME = "ITEM_NAME";

    }
}
