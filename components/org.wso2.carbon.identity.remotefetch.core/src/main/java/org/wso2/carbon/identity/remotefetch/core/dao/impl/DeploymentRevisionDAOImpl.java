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

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.TransactionException;
import org.wso2.carbon.identity.remotefetch.common.DeploymentRevision;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.constants.SQLConstants;
import org.wso2.carbon.identity.remotefetch.core.dao.DeploymentRevisionDAO;
import org.wso2.carbon.identity.remotefetch.core.util.JdbcUtils;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * DAO of deployment revision.
 */
public class DeploymentRevisionDAOImpl implements DeploymentRevisionDAO {

    /**
     * @param deploymentRevision
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public void createDeploymentRevision(DeploymentRevision deploymentRevision) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            jdbcTemplate.withTransaction(template ->
                    template.executeInsert(SQLConstants.CREATE_REVISION, preparedStatement -> {
                        preparedStatement.setString(1, deploymentRevision.getDeploymentRevisionId());
                        preparedStatement.setString(2, deploymentRevision.getConfigId());
                        preparedStatement.setString(3, deploymentRevision.getFile().getPath());
                        preparedStatement.setString(4, deploymentRevision.getFileHash());
                        preparedStatement.setString(5, deploymentRevision.getItemName());
                        preparedStatement.setString(6, deploymentRevision.getErrorMessage());
                        if (deploymentRevision.getDeployedDate() != null) {
                            preparedStatement.setTimestamp(7,
                                    new Timestamp(deploymentRevision.getDeployedDate().getTime()));
                        } else {
                            preparedStatement.setTimestamp(7, null);
                        }
                        if (deploymentRevision.getDeploymentStatus() != null) {
                            preparedStatement.setString(8, deploymentRevision.getDeploymentStatus().name());
                        } else {
                            preparedStatement.setString(8, null);
                        }
                        if (deploymentRevision.getLastSyncedDate() != null) {
                            preparedStatement.setTimestamp(9,
                                    new Timestamp(deploymentRevision.getLastSyncedDate().getTime()));
                        } else {
                            preparedStatement.setTimestamp(9, null);
                        }
                    }, deploymentRevision, false)
            );
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error creating new DeploymentRevision " +
                    deploymentRevision.getItemName(), e);
        }
    }

    /**
     * @param remoteFetchConfigurationId
     * @param itemName
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public DeploymentRevision getDeploymentRevision(String remoteFetchConfigurationId, String itemName)
            throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        DeploymentRevision deploymentRevision;
        try {
            deploymentRevision = jdbcTemplate.withTransaction(template -> {
                return jdbcTemplate.fetchSingleRecord(SQLConstants.GET_REVISION_BY_UNIQUE,
                        (resultSet, rowNumber) -> {

                            DeploymentRevision revisionObj = new DeploymentRevision(
                                    resultSet.getString(2),
                                    new File(resultSet.getString(3))
                            );
                            revisionObj.setDeploymentRevisionId(resultSet.getString(1));
                            revisionObj.setFileHash(resultSet.getString(4));
                            revisionObj.setDeployedDate(new Date(resultSet.getTimestamp(5).getTime()));
                            revisionObj.setDeploymentStatus(DeploymentRevision.
                                    DeploymentStatus.valueOf(resultSet.getString(6)));
                            revisionObj.setItemName(resultSet.getString(7));
                            revisionObj.setErrorMessage(resultSet.getString(8));
                            if (resultSet.getTimestamp(9) != null) {
                                revisionObj.setLastSyncedDate(new Date(resultSet.getTimestamp(9).getTime()));
                            } else {
                                revisionObj.setLastSyncedDate(null);
                            }

                            return revisionObj;

                        }, preparedStatement -> {
                            preparedStatement.setString(1, remoteFetchConfigurationId);
                            preparedStatement.setString(2, itemName);
                        });
            });
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error reading DeploymentRevision id " +
                    remoteFetchConfigurationId + " from database", e);
        }

        return deploymentRevision;
    }

    /**
     * @param deploymentRevision
     * @throws RemoteFetchCoreException
     */
    @Override
    public void updateDeploymentRevision(DeploymentRevision deploymentRevision) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.UPDATE_REVISION, preparedStatement -> {
                    preparedStatement.setString(1, deploymentRevision.getConfigId());
                    preparedStatement.setString(2, deploymentRevision.getFile().getPath());
                    preparedStatement.setString(3, deploymentRevision.getFileHash());
                    if (deploymentRevision.getDeployedDate() != null) {
                        preparedStatement.setTimestamp(4,
                                new Timestamp(deploymentRevision.getDeployedDate().getTime()));
                    } else {
                        preparedStatement.setTimestamp(4, null);
                    }
                    preparedStatement.setString(5, deploymentRevision.getDeploymentStatus().name());
                    preparedStatement.setString(6, deploymentRevision.getItemName());
                    preparedStatement.setString(7, deploymentRevision.getErrorMessage());
                    if (deploymentRevision.getLastSyncedDate() != null) {
                        preparedStatement.setTimestamp(8,
                                new Timestamp(deploymentRevision.getLastSyncedDate().getTime()));
                    } else {
                        preparedStatement.setTimestamp(8, null);
                    }
                    preparedStatement.setString(9, deploymentRevision.getDeploymentRevisionId());

                });
                return null;
            });
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error updating DeploymentRevision of id "
                    + deploymentRevision.getDeploymentRevisionId(), e);
        }

    }

    /**
     * @param deploymentRevisionId
     * @throws RemoteFetchCoreException
     */
    @Override
    public void deleteDeploymentRevision(String deploymentRevisionId) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.DELETE_REVISION, preparedStatement -> {
                    preparedStatement.setString(1, deploymentRevisionId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error Deleting DeploymentRevision by id " + deploymentRevisionId, e);
        }
    }

    /**
     * @param remoteFetchConfigurationId
     * @return
     * @throws RemoteFetchCoreException
     */
    @Override
    public List<DeploymentRevision> getDeploymentRevisionsByConfigurationId(
            String remoteFetchConfigurationId) throws RemoteFetchCoreException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.withTransaction(template ->
                    template.executeQuery(SQLConstants.GET_REVISIONS_BY_CONFIG, ((resultSet, rowNumber) -> {
                        DeploymentRevision deploymentRevision = new DeploymentRevision(
                                resultSet.getString(2),
                                new File(resultSet.getString(3))
                        );
                        deploymentRevision.setDeploymentRevisionId(resultSet.getString(1));
                        deploymentRevision.setFileHash(resultSet.getString(4));
                        deploymentRevision.setDeployedDate(new Date(resultSet.getTimestamp(5).getTime()));
                        deploymentRevision.setDeploymentStatus(DeploymentRevision.
                                DeploymentStatus.valueOf(resultSet.getString(6)));
                        deploymentRevision.setItemName(resultSet.getString(7));
                        deploymentRevision.setErrorMessage(resultSet.getString(8));
                        if (resultSet.getTimestamp(9) != null) {
                            deploymentRevision.setLastSyncedDate(new Date(resultSet.getTimestamp(9).getTime()));
                        } else {
                            deploymentRevision.setLastSyncedDate(null);
                        }
                        return deploymentRevision;
                    }), preparedStatement -> {
                        preparedStatement.setString(1, remoteFetchConfigurationId);
                    })
            );
        } catch (TransactionException e) {
            throw new RemoteFetchCoreException("Error reading DeploymentRevisions from database for configuration id " +
                    remoteFetchConfigurationId, e);
        }
    }
}
