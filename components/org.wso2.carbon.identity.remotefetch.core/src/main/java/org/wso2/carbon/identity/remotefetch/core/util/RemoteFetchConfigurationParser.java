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

import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchCoreConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.io.File;

public class RemoteFetchConfigurationParser {
    public static RemoteFetchCoreConfiguration parseConfiguration() throws RemoteFetchCoreException {

        boolean isEnabled = false;
        File workingDirectory = null;

        String isEnabledProperty = IdentityUtil.getProperty("RemoteFetch.FetchEnabled");
        String workingDirectoryProperty = IdentityUtil.getProperty("RemoteFetch.WorkingDirectory");

        if(isEnabledProperty != null && !isEnabledProperty.isEmpty()){
            isEnabled = isEnabledProperty.equalsIgnoreCase("true");
        }

        if(workingDirectoryProperty != null && !workingDirectoryProperty.isEmpty()){
            workingDirectory = new File(workingDirectoryProperty);
            validateDirectory(workingDirectory);
        }

        return new RemoteFetchCoreConfiguration(workingDirectory,isEnabled);

    }

    private static void validateDirectory(File workingDirectory) throws RemoteFetchCoreException{

        if(!workingDirectory.isDirectory()){
            throw new RemoteFetchCoreException("Not a valid WorkingDirectory for RemoteFetchCore");
        }
    }
}
