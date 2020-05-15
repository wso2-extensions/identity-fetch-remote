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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.remotefetch.core.dao.impl;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.core.dao.TestConstants;
import org.wso2.carbon.identity.remotefetch.core.util.JdbcUtils;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test covering RemoteFetchConfigurationDAOImpl.
 */
@PrepareForTest(JdbcUtils.class)
public class RemoteFetchConfigurationDAOImplTest extends PowerMockTestCase {

    private static final String DB_NAME = "IDN_REMOTE_FETCH_DB";
    private String remoteFetchConfigurationId = "00000000-0000-0000-0000-d29bed62f7bd";

    RemoteFetchConfigurationDAOImpl remoteFetchConfigurationDAO = new RemoteFetchConfigurationDAOImpl();
    RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration(remoteFetchConfigurationId,
            TestConstants.TENANT_ID, false, null, null,
            null, null, null);

    @BeforeClass
    public void setUp() throws Exception {

        DAOTestUtils.initiateH2Base(DB_NAME, DAOTestUtils.getFilePath("permission.sql"));
    }

    @AfterClass
    public void tearDown() throws Exception {

        DAOTestUtils.closeH2Base(DB_NAME);
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testCreateRemoteFetchConfiguration() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            remoteFetchConfigurationDAO.createRemoteFetchConfiguration(createRemoteFetch());
        }
    }

    @Test(priority = 1)
    public void testGetRemoteFetchConfigurationsById() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            RemoteFetchConfiguration getRemoteFetchConfiguration =
                    remoteFetchConfigurationDAO.getRemoteFetchConfiguration(remoteFetchConfigurationId,
                            TestConstants.TENANT_ID);
            assertNotNull(getRemoteFetchConfiguration);
            assertEquals(getRemoteFetchConfiguration.getRemoteFetchName(), "RemoteFetchDemoApp");
        }
    }

    @Test(priority = 2)
    public void testGetRemoteFetchConfigurationsByTenant() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            List<RemoteFetchConfiguration> remoteFetchConfigurationArrayList =
                    remoteFetchConfigurationDAO.getRemoteFetchConfigurationsByTenant(TestConstants.TENANT_ID);
            assertNotNull(remoteFetchConfigurationArrayList);
            assertEquals(remoteFetchConfigurationArrayList.size(), 1);
        }
    }

    @Test(priority = 3)
    public void testUpdateRemoteFetchConfiguration() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            remoteFetchConfigurationDAO.updateRemoteFetchConfiguration(updateConfiguration());
        }
    }

    @Test(priority = 4)
    public void testGetAllEnabledRemoteFetchConfigurations() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            List<RemoteFetchConfiguration> remoteFetchConfigurationArrayList =
                    remoteFetchConfigurationDAO.getAllEnabledRemoteFetchConfigurations();
            assertNotNull(remoteFetchConfigurationArrayList);
            assertEquals(remoteFetchConfigurationArrayList.size(), 1);
        }
    }

    @Test(priority = 5)
    public void testUpdateRemoteFetchConfigurationTriggerId() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            remoteFetchConfigurationDAO.updateRemoteFetchConfigurationTriggerId(updateConfigurationTriggerID());
        }
    }

    @Test(priority = 6)
    public void testDeleteRemoteFetchConfiguration() throws Exception {

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            remoteFetchConfigurationDAO.deleteRemoteFetchConfiguration(remoteFetchConfigurationId,
                    TestConstants.TENANT_ID);
        }
    }

    private RemoteFetchConfiguration createRemoteFetch() {

        remoteFetchConfiguration.setTenantId(TestConstants.TENANT_ID);
        remoteFetchConfiguration.setActionListenerType(TestConstants.ACTION_LISTENER_TYPE);
        remoteFetchConfiguration.setConfigurationDeployerType(TestConstants.CONFIG_DEPLOYER_TYPE);
        remoteFetchConfiguration.setRemoteFetchConfigurationId(remoteFetchConfigurationId);
        remoteFetchConfiguration.setEnabled(true);
        remoteFetchConfiguration.setRepositoryManagerType(TestConstants.REPO_MANAGER_TYPE);
        remoteFetchConfiguration.setRemoteFetchName("RemoteFetchDemoApp");
        remoteFetchConfiguration.setActionListenerAttributes(TestConstants.actionListenerAttributes);
        remoteFetchConfiguration.setConfigurationDeployerAttributes(TestConstants.configurationDeployerAttributes);
        remoteFetchConfiguration.setRepositoryManagerAttributes(TestConstants.repositoryManagerAttributes);
        remoteFetchConfiguration.setTriggerId(TestConstants.TRIGGER_ID);
        return remoteFetchConfiguration;
    }

    private RemoteFetchConfiguration updateConfiguration() {

        remoteFetchConfiguration.setRemoteFetchName("UpdatedRemoteFetchDemoApp");
        return remoteFetchConfiguration;
    }

    private RemoteFetchConfiguration updateConfigurationTriggerID() {
        remoteFetchConfiguration.setTriggerId("11111111-0000-0000-0000-d29bed62f7bd");
        return remoteFetchConfiguration;
    }
}
