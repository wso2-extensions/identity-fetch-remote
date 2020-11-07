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

package org.wso2.carbon.identity.remotefetch.common;

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

    public static final String IDENTIFIER_GIT_REPOSITORY_MANAGER_COMPONENT = "GIT";
    public static final String IDENTIFIER_POLLING_ACTION_LISTENER_COMPONENT = "POLLING";
    public static final String IDENTIFIER_WEB_HOOK_ACTION_LISTENER_COMPONENT = "WEB_HOOK";
    public static final String IDENTIFIER_SERVICE_PROVIDER_CONFIG_DEPLOYER_COMPONENT = "SP";
    public static final String URL_DELIMITER = "/";
    public static final String TREE = "tree";

    public static final String ID_UI_FIELD_URI = "uri";
    public static final String ID_UI_FIELD_BRANCH = "branch";
    public static final String ID_UI_FIELD_DIRECTORY = "directory";

    // Config constants.
    public static final String REMOTE_FETCH_ENABLED = "RemoteFetch.FetchEnabled";
    public static final String REMOTE_FETCH_WORKING_DIRECTORY = "RemoteFetch.WorkingDirectory";

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

    /**
     * Error message for client and server exceptions.
     */
    public enum ErrorMessage {

        ERROR_CODE_UNEXPECTED("RFE-65001", "Unexpected Error"),
        ERROR_CODE_ADD_RF_CONFIG("RFE-65002", "Error while adding the Remote Fetch Configuration: %s."),
        ERROR_CODE_RETRIEVE_RF_CONFIG("RFE-65003",
                "Error while getting the Remote Fetch Configuration: %s."),
        ERROR_CODE_DELETE_RF_CONFIG("RFE-65004", "Error while deleting Remote Fetch Configuration: %s."),
        ERROR_CODE_UPDATE_RF_CONFIG("RFE-65005", "Error while updating Remote Fetch Configuration: %s."),
        ERROR_CODE_CONNECTING_DATABASE("RFE-65006", "Error while connecting database. %s"),
        ERROR_CODE_RETRIEVE_RF_CONFIGS("RFE-65007", "Error while retrieving " +
                "Remote Fetch Configurations."),

        ERROR_CODE_RF_CONFIG_ALREADY_EXISTS("RFE-60001",
                "Remote Fetch Configuration with the name: %s already exists."),
        ERROR_CODE_RF_CONFIG_DOES_NOT_EXIST("RFE-60002",
                "Remote Fetch Configuration with resource ID: %s does not exists."),
        ERROR_CODE_RF_CONFIG_ADD_REQUEST_INVALID("RFE-60003",
                "Remote Fetch Configuration add request validation failed. %s"),
        ERROR_CODE_RF_CONFIG_GET_REQUEST_INVALID("RFE-60004",
                "Remote Fetch Configuration get request validation failed. %s"),
        ERROR_CODE_RF_CONFIG_DELETE_REQUEST_INVALID("RFE-60005",
                "Remote Fetch Configuration delete request validation failed. %s"),
        ERROR_CODE_RF_CONFIG_UPDATE_REQUEST_INVALID("RFE-60006",
                "Remote Fetch Configuration update request validation failed. %s");

        private final String code;
        private final String message;

        ErrorMessage(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        @Override
        public String toString() {

            return code + ":" + message;
        }
    }
}
