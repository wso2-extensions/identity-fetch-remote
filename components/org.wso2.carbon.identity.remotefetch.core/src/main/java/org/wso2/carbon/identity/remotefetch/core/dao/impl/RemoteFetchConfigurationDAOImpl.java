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

package org.wso2.carbon.identity.remotefetch.core.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.database.utils.jdbc.exceptions.TransactionException;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.constants.SQLConstants;
import org.wso2.carbon.identity.remotefetch.core.dao.RemoteFetchConfigurationDAO;
import org.wso2.carbon.identity.remotefetch.core.util.JdbcUtils;
import org.wso2.carbon.identity.remotefetch.core.util.RemoteFetchConfigurationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ATTRIBUTE_ACTION_LISTENER;
import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ATTRIBUTE_CONFIG_DEPLOYER;
import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ATTRIBUTE_REPOSITORY_MANAGER;
import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.FACTOR_INDENT;

/**
 * This class accesses IDN_REMOTE_FETCH_CONFIG table to store/update and delete Remote Fetch configurations.
 * TODO : Implement Name preparedstatement
 */
public class RemoteFetchConfigurationDAOImpl implements RemoteFetchConfigurationDAO {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationDAOImpl.class);

    /**
     * @param configuration
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public void createRemoteFetchConfiguration(RemoteFetchConfiguration configuration) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {

                    jdbcTemplate.executeInsert(SQLConstants.CREATE_CONFIG,
                            preparedStatement -> {
                                preparedStatement.setString(1, configuration.getRemoteFetchConfigurationId());
                                preparedStatement.setInt(2, configuration.getTenantId());
                                preparedStatement.setString(3, (configuration.isEnabled() ? "1" : "0"));
                                preparedStatement.setString(4, configuration.getRepositoryManagerType());
                                preparedStatement.setString(5, configuration.getActionListenerType());
                                preparedStatement.setString(6, configuration.getConfigurationDeployerType());

                                //Encode object attributes to JSON
                                JSONObject attributesBundle = this.makeAttributeBundle(configuration);

                                preparedStatement.setString(7, attributesBundle.toString(FACTOR_INDENT));
                                preparedStatement.setString(8, configuration.getRemoteFetchName());
                                preparedStatement.setString(9, configuration.getRemoteResourceURI());
                                }
                            , configuration, false)
            ;
        } catch (DataAccessException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage.
                    ERROR_CODE_ADD_RF_CONFIG, configuration.getRemoteFetchName(), e);
        }
    }

    /**
     * @param configurationId
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public RemoteFetchConfiguration getRemoteFetchConfiguration(String configurationId, int tenantId)
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    jdbcTemplate.fetchSingleRecord(SQLConstants.GET_CONFIG,
                            (resultSet, i) -> this.resultSetToConfiguration(resultSet),
                            preparedStatement -> {
                        preparedStatement.setString(1, configurationId);
                        preparedStatement.setInt(2, tenantId);
                            })
            );
        } catch (TransactionException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage
                    .ERROR_CODE_RETRIEVE_RF_CONFIG, configurationId, e);
        }
    }

    /**
     * @param configuration
     * @throws RemoteFetchCoreException
     */
    @Override
    public void updateRemoteFetchConfiguration(RemoteFetchConfiguration configuration) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            jdbcTemplate.withTransaction(template -> {
                jdbcTemplate.executeUpdate(SQLConstants.UPDATE_CONFIG,
                        preparedStatement -> {
                            this.configurationToPreparedStatement(preparedStatement, configuration);
                            preparedStatement.setString(9, configuration.getRemoteFetchConfigurationId());

                        }

                );
                return null;
            });
        } catch (TransactionException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage
                    .ERROR_CODE_UPDATE_RF_CONFIG, configuration.getRemoteFetchName(), e);
        }
    }

    /**
     * @param configurationId
     * @param tenantId TenantId.
     * @throws RemoteFetchCoreException
     */
    @Override
    public void deleteRemoteFetchConfiguration(String configurationId, int tenantId)
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.DELETE_CONFIG,
                        preparedStatement -> {
                            preparedStatement.setString(1, configurationId);
                            preparedStatement.setInt(2, tenantId);
                        }
                );
                return null;
            });
        } catch (TransactionException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage.
                    ERROR_CODE_DELETE_RF_CONFIG, configurationId, e);
        }
    }

    /**
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getAllEnabledPoolingRemoteFetchConfigurations()
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    template.executeQuery(SQLConstants.LIST_ENABLED_CONFIGS,
                            ((resultSet, i) -> this.resultSetToConfiguration(resultSet)))
            );
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error listing RemoteFetchConfigurations from database", e);
        }
    }

    /**
     * @param tenantId
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getRemoteFetchConfigurationsByTenant(int tenantId)
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    template.executeQuery(SQLConstants.LIST_CONFIGS_BY_TENANT,
                            ((resultSet, i) -> this.resultSetToConfiguration(resultSet)),
                            preparedStatement -> preparedStatement.setInt(1, tenantId))
            );
        } catch (TransactionException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage.
                    ERROR_CODE_RETRIEVE_RF_CONFIGS, e);
        }
    }

    @Override
    public List<RemoteFetchConfiguration> getWebHookRemoteFetchConfigurationsByTenant(int tenantId)
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    template.executeQuery(SQLConstants.LIST_WEB_HOOK_FETCH_CONFIGS_BY_TENANT,
                            ((resultSet, i) -> this.resultSetToConfiguration(resultSet)),
                            preparedStatement -> preparedStatement.setInt(1, tenantId))
            );
        } catch (TransactionException e) {
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage.
                    ERROR_CODE_RETRIEVE_RF_CONFIGS, e);
        }
    }

    /**
     * @param tenantDomain
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationsByTenant(String tenantDomain,
                                                                                         int limit,
                                                                                         int offset)
            throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection(false)) {
            String databaseProductName = dbConnection.getMetaData().getDatabaseProductName();
            if (databaseProductName.contains(SQLConstants.DB_MYSQL) ||
                    databaseProductName.contains(SQLConstants.DB_H2)) {

                return jdbcTemplate.withTransaction(template ->
                        template.executeQuery(SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_MYSQL,
                                (resultSet, i) -> this.resultSetToBasicConfiguration(resultSet),
                                preparedStatement -> {
                                    preparedStatement.setInt(1, tenantId);
                                    preparedStatement.setInt(2, offset);
                                    preparedStatement.setInt(3, limit);
                                })
                );
            } else if (databaseProductName.contains(SQLConstants.DB_ORACLE)) {
                String sql = SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_ORACLE_LIMIT_HEAD +
                        SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_ORACLE +
                        SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_ORACLE_LIMIT_TAIL;

                return jdbcTemplate.withTransaction(template ->
                        template.executeQuery(sql,
                                (resultSet, i) -> this.resultSetToBasicConfiguration(resultSet),
                                preparedStatement -> {
                                    preparedStatement.setInt(1, tenantId);
                                    preparedStatement.setInt(2, offset + limit);
                                    preparedStatement.setInt(3, offset);
                                })
                );

            } else if (databaseProductName.contains(SQLConstants.DB_MSSQL)) {

                return jdbcTemplate.withTransaction(template ->
                        template.executeQuery(SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_MSSQL,
                                (resultSet, i) -> this.resultSetToBasicConfiguration(resultSet),
                                preparedStatement -> {
                                    preparedStatement.setInt(1, tenantId);
                                    preparedStatement.setInt(2, offset);
                                    preparedStatement.setInt(3, limit);
                                })
                );

            } else if (databaseProductName.contains(SQLConstants.DB_POSTGRESQL)
                    || databaseProductName.contains(SQLConstants.DB_DB2)) {

                return jdbcTemplate.withTransaction(template ->
                        template.executeQuery(SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT_POSTGRES_DB2,
                                (resultSet, i) -> this.resultSetToBasicConfiguration(resultSet),
                                preparedStatement -> {
                                    preparedStatement.setInt(1, tenantId);
                                    preparedStatement.setInt(2, limit);
                                    preparedStatement.setInt(3, offset);
                                })
                );
            } else {
                log.error("Error while loading Remote fetch configuration from DB: Database driver could not " +
                        "be identified or not supported.");
                String message = "Error while loading Remote fetch configuration from DB: Database driver " +
                        "could not be identified or not supported.";
                throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage
                        .ERROR_CODE_CONNECTING_DATABASE, message);
            }
        } catch (TransactionException | SQLException e) {
            String message = "Error occurred while retrieving Remote Fetch Configuration for tenant: " +
                    tenantDomain;
            throw RemoteFetchConfigurationUtils.handleServerException(RemoteFetchConstants.ErrorMessage.
                    ERROR_CODE_RETRIEVE_RF_CONFIGS, message, e);
        }
    }

    private Map<String, String> attributeToMap(JSONObject attributes) {

        Map<String, String> attrMap = new HashMap<>();
        attributes.keySet().forEach((Object key) -> {
            if (key.getClass() == String.class) {
                attrMap.put((String) key, attributes.getString((String) key));
            }
        });
        return attrMap;
    }

    private RemoteFetchConfiguration resultSetToConfiguration(ResultSet resultSet) throws SQLException {

        RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration();
        remoteFetchConfiguration.setRemoteFetchConfigurationId(resultSet.getString(1));
        remoteFetchConfiguration.setTenantId(resultSet.getInt(2));
        remoteFetchConfiguration.setEnabled(resultSet.getString(3).equals("1"));
        remoteFetchConfiguration.setRepositoryManagerType(resultSet.getString(4));
        remoteFetchConfiguration.setActionListenerType(resultSet.getString(5));
        remoteFetchConfiguration.setConfigurationDeployerType(resultSet.getString(6));
        remoteFetchConfiguration.setRemoteFetchName(resultSet.getString(7));
        remoteFetchConfiguration.setRemoteResourceURI(resultSet.getString(8));
        JSONObject attributesBundle = new JSONObject(resultSet.getString(9));
        this.mapAttributes(remoteFetchConfiguration, attributesBundle);
        return remoteFetchConfiguration;
    }

    private BasicRemoteFetchConfiguration resultSetToBasicConfiguration(ResultSet resultSet) throws SQLException {

        BasicRemoteFetchConfiguration basicRemoteFetchConfiguration = new BasicRemoteFetchConfiguration();
        basicRemoteFetchConfiguration.setId(resultSet.getString(1));
        basicRemoteFetchConfiguration.setEnabled(resultSet.getString(2).equals("1"));
        basicRemoteFetchConfiguration.setRepositoryManagerType(resultSet.getString(3));
        basicRemoteFetchConfiguration.setActionListenerType(resultSet.getString(4));
        basicRemoteFetchConfiguration.setConfigurationDeployerType(resultSet.getString(5));
        basicRemoteFetchConfiguration.setRemoteFetchName(resultSet.getString(6));
        basicRemoteFetchConfiguration.setSuccessfulDeployments(resultSet.getInt(7));
        basicRemoteFetchConfiguration.setFailedDeployments(resultSet.getInt(8));
        Timestamp lastDeployed = resultSet.getTimestamp(9);
        if (lastDeployed != null) {
            basicRemoteFetchConfiguration.setLastDeployed(new Date(lastDeployed.getTime()));
        }
        return basicRemoteFetchConfiguration;
    }

    private void configurationToPreparedStatement(PreparedStatement preparedStatement,
                                                  RemoteFetchConfiguration configuration) throws SQLException {

        preparedStatement.setInt(1, configuration.getTenantId());
        preparedStatement.setString(2, (configuration.isEnabled() ? "1" : "0"));
        preparedStatement.setString(3, configuration.getRepositoryManagerType());
        preparedStatement.setString(4, configuration.getActionListenerType());
        preparedStatement.setString(5, configuration.getConfigurationDeployerType());

        //Encode object attributes to JSON
        JSONObject attributesBundle = this.makeAttributeBundle(configuration);

        preparedStatement.setString(6, attributesBundle.toString(FACTOR_INDENT));
        preparedStatement.setString(7, configuration.getRemoteFetchName());
        preparedStatement.setString(8, configuration.getRemoteResourceURI());

    }

    private JSONObject makeAttributeBundle(RemoteFetchConfiguration configuration) {

        JSONObject attributesBundle = new JSONObject();
        attributesBundle.put(ATTRIBUTE_REPOSITORY_MANAGER, configuration.getRepositoryManagerAttributes());
        attributesBundle.put(ATTRIBUTE_ACTION_LISTENER, configuration.getActionListenerAttributes());
        attributesBundle.put(ATTRIBUTE_CONFIG_DEPLOYER, configuration.getConfigurationDeployerAttributes());
        return attributesBundle;
    }

    private void mapAttributes(RemoteFetchConfiguration remoteFetchConfiguration, JSONObject attributesBundle) {

        remoteFetchConfiguration.setRepositoryManagerAttributes(
                this.attributeToMap(attributesBundle.getJSONObject(ATTRIBUTE_REPOSITORY_MANAGER))
        );
        remoteFetchConfiguration.setActionListenerAttributes(
                this.attributeToMap(attributesBundle.getJSONObject(ATTRIBUTE_ACTION_LISTENER))
        );
        remoteFetchConfiguration.setConfigurationDeployerAttributes(
                this.attributeToMap(attributesBundle.getJSONObject(ATTRIBUTE_CONFIG_DEPLOYER))
        );

    }
}
