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

package org.wso2.carbon.identity.remotefetch.core.util;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.core.RemoteFetchComponentRegistryImpl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test covering RemoteFetchConfigurationValidator
 */
@PrepareForTest(RemoteFetchConfigurationValidator.class)
public class RemoteFetchConfigurationValidatorTest extends PowerMockTestCase {

    private static final String ID = "00000000-0000-0000-0000-d29bed62f7bd";
    private static final int TENANT_ID = -1234;
    private static final boolean IS_ENABLED = true;
    private static final String USER_NAME = "admin";
    private static final String REPO_MANAGER_TYPE = "GIT";
    private static final String ACTION_LISTENER_TYPE = "POLLING";
    private static final String CONFIG_DEPLOYER_TYPE = "SP";
    private static final String REMOTE_FETCH_NAME = "RemoteFetchTest";

    RemoteFetchConfiguration remoteFetchConfiguration =
            new RemoteFetchConfiguration(ID, TENANT_ID, IS_ENABLED, USER_NAME,
            REPO_MANAGER_TYPE, ACTION_LISTENER_TYPE, CONFIG_DEPLOYER_TYPE, REMOTE_FETCH_NAME);

    RemoteFetchComponentRegistry remoteFetchComponentRegistry = new RemoteFetchComponentRegistryImpl();

    @BeforeMethod
    public void setUp() {

    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testValidate() {

        RemoteFetchConfigurationValidator remoteFetchConfigurationValidator =
                new RemoteFetchConfigurationValidator(remoteFetchComponentRegistry, remoteFetchConfiguration);

        assertNotNull(remoteFetchConfigurationValidator.validate().getMessages());
        assertEquals(remoteFetchConfigurationValidator.validate().getValidationStatus().toString(), "PASSED");
    }

    @Test
    public void testValidateForNullUserName() {

        remoteFetchConfiguration.setUserName(null);
        RemoteFetchConfigurationValidator remoteFetchConfigurationValidator =
                new RemoteFetchConfigurationValidator(remoteFetchComponentRegistry, remoteFetchConfiguration);

        assertEquals(remoteFetchConfigurationValidator.validate().getValidationStatus().toString(), "FAILED");
    }

}
