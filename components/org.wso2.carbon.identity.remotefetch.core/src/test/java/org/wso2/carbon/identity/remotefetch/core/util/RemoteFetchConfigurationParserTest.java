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

package org.wso2.carbon.identity.remotefetch.core.util;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.io.File;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Unit test covering RemoteFetchConfigurationParser
 */
@PrepareForTest(IdentityUtil.class)
public class RemoteFetchConfigurationParserTest extends PowerMockTestCase {

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testParseConfiguration() throws RemoteFetchCoreException {

        mockStatic(IdentityUtil.class);
        when(IdentityUtil.getProperty("RemoteFetch.FetchEnabled")).thenReturn("true");
        RemoteFetchConfigurationParser.parseConfiguration();
        assertNotNull(RemoteFetchConfigurationParser.parseConfiguration());
        assertTrue(RemoteFetchConfigurationParser.parseConfiguration().isEnableCore());
    }

    @Test
    public void testParseConfigurationForNullValues() throws RemoteFetchCoreException {

        assertNotNull(RemoteFetchConfigurationParser.parseConfiguration());
        assertFalse(RemoteFetchConfigurationParser.parseConfiguration().isEnableCore());
        assertNull(RemoteFetchConfigurationParser.parseConfiguration().getWorkingDirectory());
    }
}
