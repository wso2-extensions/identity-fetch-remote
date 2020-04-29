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

import org.json.JSONObject;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.database.utils.jdbc.exceptions.TransactionException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.constants.SQLConstants;
import org.wso2.carbon.identity.remotefetch.core.dao.RemoteFetchConfigurationDAO;
import org.wso2.carbon.identity.remotefetch.core.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.remotefetch.core.RemoteFetchConstants.ATTRIBUTE_ACTION_LISTENER;
import static org.wso2.carbon.identity.remotefetch.core.RemoteFetchConstants.ATTRIBUTE_CONFIG_DEPLOYER;
import static org.wso2.carbon.identity.remotefetch.core.RemoteFetchConstants.ATTRIBUTE_REPOSITORY_MANAGER;
import static org.wso2.carbon.identity.remotefetch.core.RemoteFetchConstants.FACTOR_INDENT;

/**
 * This class accesses IDN_REMOTE_FETCH_CONFIG table to store/update and delete Remote Fetch configurations.
 * TODO : Implement Name preparedstatement
 */
public class RemoteFetchConfigurationDAOImpl implements RemoteFetchConfigurationDAO {

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
                                }
                            , configuration, false)
            ;
        } catch (DataAccessException e) {
            throw new RemoteFetchCoreException("Error creating new RemoteFetchConfiguration, caused by "
                    + e.getMessage(), e);
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
            throw new RemoteFetchCoreException("Error reading RemoteFetchConfiguration of id " +
                    configurationId + " from database", e);
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
                            preparedStatement.setString(8, configuration.getRemoteFetchConfigurationId());

                        }

                );
                return null;
            });
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error updating RemoteFetchConfiguration of id "
                    + configuration.getRemoteFetchConfigurationId(), e);
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
            throw new RemoteFetchCoreException("Error Deleting DeploymentRevision of id " + configurationId, e);
        }
    }

    /**
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<RemoteFetchConfiguration> getAllEnabledRemoteFetchConfigurations() throws RemoteFetchCoreException {

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
            throw new RemoteFetchCoreException("Error listing RemoteFetchConfigurations from database", e);
        }
    }

    /**
     * @param tenantDomain
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<BasicRemoteFetchConfiguration> getBasicRemoteFetchConfigurationsByTenant(String tenantDomain)
            throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    template.executeQuery(SQLConstants.LIST_BASIC_CONFIGS_BY_TENANT,
                            ((resultSet, i) -> {
                                BasicRemoteFetchConfiguration obj = new BasicRemoteFetchConfiguration(
                                        resultSet.getString(1),
                                        resultSet.getString(2).equals("1"),
                                        resultSet.getString(3),
                                        resultSet.getString(4),
                                        resultSet.getString(5),
                                        resultSet.getString(6),
                                        resultSet.getInt(7),
                                        resultSet.getInt(8));
                                Timestamp lastDeployed = resultSet.getTimestamp(9);
                                if (lastDeployed != null) {
                                    obj.setLastDeployed(new Date(lastDeployed.getTime()));
                                }
                                return obj;
                            })
                            , preparedStatement -> preparedStatement.setInt(1, tenantId))
            );
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error listing BasicRemoteFetchConfigurations from database", e);
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

        RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration(
                resultSet.getString(1),
                resultSet.getInt(2),
                resultSet.getString(3).equals("1"),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7)
        );
        JSONObject attributesBundle = new JSONObject(resultSet.getString(8));
        this.mapAttributes(remoteFetchConfiguration, attributesBundle);
        return remoteFetchConfiguration;
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
