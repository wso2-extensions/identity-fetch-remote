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

package org.wso2.carbon.identity.remotefetch.core.impl.handlers.action;

import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.identity.remotefetch.common.DeploymentRevision;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilder;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilderException;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager;
import org.wso2.carbon.identity.remotefetch.core.dao.impl.DAOTestUtils;
import org.wso2.carbon.identity.remotefetch.core.dao.impl.DeploymentRevisionDAOImpl;
import org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.polling.PollingActionListenerBuilder;
import org.wso2.carbon.identity.remotefetch.core.util.JdbcUtils;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.sql.DataSource;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.AssertJUnit.assertNotNull;
import static org.wso2.carbon.identity.remotefetch.core.dao.TestConstants.DEPLOYMENT_REVISION_ID;
import static org.wso2.carbon.identity.remotefetch.core.dao.TestConstants.REMOTE_FETCH_CONFIGURATION_ID;

/**
 * Unit test covering PollingActionListenerBuilder.
 */
@PrepareForTest({DeploymentRevisionDAOImpl.class, JdbcUtils.class})
public class PollingActionListenerBuilderTest extends PowerMockTestCase {

    private static final String DB_NAME = "IDN_REMOTE_FETCH_DB";

    @Mock
    DeploymentRevisionDAOImpl deploymentRevisionDAO;

    @Mock
    RepositoryManager repoConnector;

    @Mock
    ConfigDeployer configDeployer;

    Map<String, String> actionListenerAttributesMap = new HashMap<>();
    RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration
            (REMOTE_FETCH_CONFIGURATION_ID,
                    -1234,
                    true, "GIT", "POLLING",
                    "SP", "RemoteFetchTest",
                    "https://github.com/IS/Test2.git/tree/master/sp");

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @BeforeMethod
    public void setUp() throws Exception {

        DAOTestUtils.initiateH2Base(DB_NAME, DAOTestUtils.getFilePath("permission.sql"));
    }

    @Test
    public void testBuildForIfCondition() throws Exception {

        Map<String, String> actionListenerAttributesMap = new HashMap<>();
        remoteFetchConfiguration.setActionListenerAttributes(actionListenerAttributesMap);
        actionListenerAttributesMap.put("frequency", "60");

        DeploymentRevision deploymentRevision = buildDummyDeploymentRevision();
        List<DeploymentRevision> list = new ArrayList<>();
        list.add(deploymentRevision);

        DataSource dataSource = mock(DataSource.class);
        mockStatic(JdbcUtils.class);
        when(JdbcUtils.getNewTemplate()).thenReturn(new JdbcTemplate(dataSource));
        mockStatic(DeploymentRevisionDAOImpl.class);
        when(deploymentRevisionDAO.getDeploymentRevisionsByConfigurationId
                (remoteFetchConfiguration.getRemoteFetchConfigurationId())).thenReturn(list);
        try (Connection connection = DAOTestUtils.getConnection(DB_NAME)) {
            Connection spy = DAOTestUtils.spyConnection(connection);
            when(dataSource.getConnection()).thenReturn(spy);
            ActionListenerBuilder actionListenerBuilder = new PollingActionListenerBuilder();
            assertNotNull(actionListenerBuilder.addRemoteFetchConfig(remoteFetchConfiguration)
                    .addConfigDeployer(configDeployer)
                    .addRepositoryConnector(repoConnector).build());
        }
    }

    @Test(expectedExceptions = ActionListenerBuilderException.class)
    public void testBuildForElseCondition() throws Exception {

        ActionListenerBuilder actionListenerBuilder = new PollingActionListenerBuilder();
        Map<String, String> actionListenerAttributesMap = new HashMap<>();
        actionListenerAttributesMap.put("polling", "60");
        remoteFetchConfiguration.setActionListenerAttributes(actionListenerAttributesMap);
        actionListenerBuilder.addRemoteFetchConfig(remoteFetchConfiguration).addRepositoryConnector(repoConnector)
                .addConfigDeployer(configDeployer).build();
    }

    @Test(expectedExceptions = {NumberFormatException.class, ActionListenerBuilderException.class})
    public void testBuildForException() throws Exception {

        ActionListenerBuilder actionListenerBuilder = new PollingActionListenerBuilder();
        actionListenerAttributesMap.put("frequency", "6000000000");
        remoteFetchConfiguration.setActionListenerAttributes(actionListenerAttributesMap);
        actionListenerBuilder.addRemoteFetchConfig(remoteFetchConfiguration).addConfigDeployer(configDeployer)
                .addRepositoryConnector(repoConnector).build();
    }

    private DeploymentRevision buildDummyDeploymentRevision() {

        DeploymentRevision deploymentRevision = new DeploymentRevision
                (REMOTE_FETCH_CONFIGURATION_ID, null);
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        deploymentRevision.setDeploymentRevisionId(DEPLOYMENT_REVISION_ID);
        deploymentRevision.setItemName("NewDemoApp");
        deploymentRevision.setFileHash("12345678");
        deploymentRevision.setFile(new File("sp/newFile.xml"));
        deploymentRevision.setConfigId(remoteFetchConfiguration.getRemoteFetchConfigurationId());
        deploymentRevision.setDeployedDate(date);
        deploymentRevision.setDeploymentStatus(DeploymentRevision.DeploymentStatus.SUCCESS);
        return deploymentRevision;
    }
}
