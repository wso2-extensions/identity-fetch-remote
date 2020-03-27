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

package org.wso2.carbon.identity.remotefetch.core.implementations.configDeployers;

import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.remotefetch.common.ConfigurationFileStream;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.internal.RemoteFetchServiceComponentHolder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.TenantManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;

@PrepareForTest({RemoteFetchServiceComponentHolder.class, RealmService.class, TenantManager.class})
public class ServiceProviderConfigDeployerTest extends PowerMockTestCase {

    @Mock
    RemoteFetchServiceComponentHolder remoteFetchServiceComponentHolder;

    @Mock
    RealmService realmService;

    @Mock
    TenantManager tenantManager;

    private static final int TENANT_ID = -1234;
    private static final String USER_NAME = "admin";
    private static final int ID = 1;

    @BeforeMethod
    public void setUp() {

    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testResolveConfigName() throws Exception {

        File file = new File("src/test/resources/conf/tester.xml");
        String absolutePath = file.getAbsolutePath();
        File filePath = new File(absolutePath);

        VelocityTemplatedSPDeployer velocityTemplatedSPDeployer = new VelocityTemplatedSPDeployer(TENANT_ID, USER_NAME, ID);
        InputStream inputStream = new FileInputStream(absolutePath);
        ConfigurationFileStream configurationFileStream = new ConfigurationFileStream(inputStream, filePath);

        mockStatic(RemoteFetchServiceComponentHolder.class);
        when(RemoteFetchServiceComponentHolder.getInstance()).thenReturn(remoteFetchServiceComponentHolder);
        mockStatic(RealmService.class);
        when(RemoteFetchServiceComponentHolder.getInstance().getRealmService()).thenReturn(realmService);
        mockStatic(TenantManager.class);
        when(RemoteFetchServiceComponentHolder.getInstance().getRealmService().getTenantManager()).thenReturn(tenantManager);
        when(RemoteFetchServiceComponentHolder.getInstance().getRealmService().getTenantManager().getDomain(-1234)).thenReturn("carbon.super");
        assertEquals(velocityTemplatedSPDeployer.resolveConfigName(configurationFileStream), "tester");
    }

    @Test(expectedExceptions = {UserStoreException.class, RemoteFetchCoreException.class, NullPointerException.class})
    public void testResolvedConfigNameForException() throws Exception {

        File file = new File("src/test/resources/conf/tester.xml");
        String absolutePath = file.getAbsolutePath();
        File filePath = new File(absolutePath);

        VelocityTemplatedSPDeployer velocityTemplatedSPDeployer = new VelocityTemplatedSPDeployer(TENANT_ID, USER_NAME, ID);
        InputStream inputStream = new FileInputStream(absolutePath);
        ConfigurationFileStream configurationFileStream = new ConfigurationFileStream(inputStream, filePath);
        velocityTemplatedSPDeployer.resolveConfigName(configurationFileStream);
    }
}
