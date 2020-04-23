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

package org.wso2.carbon.identity.remotefetch.core.dao.impl;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

/**
 * DB Utils.
 */
public class DAOTestUtils {

    private static Map<String, BasicDataSource> dataSourceMap = new HashMap<>();

    private static final String CREATE_CONFIG = "INSERT INTO IDN_REMOTE_FETCH_CONFIG (ID, TENANT_ID, IS_ENABLED, " +
            "USER_NAME, REPO_MANAGER_TYPE, ACTION_LISTENER_TYPE, CONFIG_DEPLOYER_TYPE, " +
            " ATTRIBUTES_JSON, REMOTE_FETCH_NAME  ) VALUES (?,?,?,?,?,?,?,?,?)";

    public static void initiateH2Base(String databaseName, String scriptPath) throws Exception {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:h2:mem:test" + databaseName);
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeUpdate("RUNSCRIPT FROM '" + scriptPath + "'");
        }
        dataSourceMap.put(databaseName, dataSource);
    }

    public static void closeH2Base(String databaseName) throws Exception {

        BasicDataSource dataSource = dataSourceMap.get(databaseName);
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static Connection getConnection(String database) throws SQLException {

        if (dataSourceMap.get(database) != null) {
            return dataSourceMap.get(database).getConnection();
        }
        throw new RuntimeException("No datasource initiated for database: " + database);
    }

    public static Connection spyConnection(Connection connection) throws SQLException {

        Connection spy = spy(connection);
        doNothing().when(spy).close();
        return spy;
    }

    public static String getFilePath(String fileName) {

        if (StringUtils.isNotBlank(fileName)) {
            return Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "dbScripts", fileName)
                    .toString();
        }
        throw new IllegalArgumentException("DB Script file name cannot be empty.");
    }

    public static void createFetchConfig(String databaseName, String id, int tenantId, boolean isEnabled,
                                         String userName, String repoManagerType, String actionListenerType,
                                         String configDeployerType, JSONObject attributes, String remoteFetchName)
            throws Exception {

        try (Connection connection = getConnection(databaseName);
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CONFIG)) {
            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, tenantId);
            preparedStatement.setString(3, isEnabled ? "1" : "0");
            preparedStatement.setString(4, userName);
            preparedStatement.setString(5, repoManagerType);
            preparedStatement.setString(6, actionListenerType);
            preparedStatement.setString(7, configDeployerType);
            preparedStatement.setString(8, attributes.toString());
            preparedStatement.setString(9, remoteFetchName);
            preparedStatement.execute();
        }
    }
}
