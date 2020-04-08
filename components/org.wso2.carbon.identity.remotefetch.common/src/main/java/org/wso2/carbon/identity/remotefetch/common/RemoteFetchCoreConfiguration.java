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

package org.wso2.carbon.identity.remotefetch.common;

import java.io.File;

/**
 * Holds core configuration
 */
public class RemoteFetchCoreConfiguration {

    private File workingDirectory;
    private boolean isEnableCore;

    public RemoteFetchCoreConfiguration(File workingDirectory, boolean isEnableCore) {

        this.workingDirectory = workingDirectory;
        this.isEnableCore = isEnableCore;
    }

    public File getWorkingDirectory() {

        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {

        this.workingDirectory = workingDirectory;
    }

    public boolean isEnableCore() {

        return isEnableCore;
    }

    public void setEnableCore(boolean isEnableCore) {

        this.isEnableCore = isEnableCore;
    }
}
