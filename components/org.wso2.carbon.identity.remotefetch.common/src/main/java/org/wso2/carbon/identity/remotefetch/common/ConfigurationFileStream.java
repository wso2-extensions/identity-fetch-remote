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
import java.io.InputStream;

/**
 * Holds raw config file content for reading xml files to deploy configurations.
 */
public class ConfigurationFileStream {

    private InputStream contentStream;
    private File path;

    /**
     * Default constructor.
     *
     * @param contentStream Input contentStream
     * @param path          File path
     */
    public ConfigurationFileStream(InputStream contentStream, File path) {

        this.contentStream = contentStream;
        this.path = path;
    }

    /**
     * Get input stream to read file.
     *
     * @return contentStream
     */
    public InputStream getContentStream() {

        return contentStream;
    }

    /**
     * Get file path.
     *
     * @return path.
     */
    public File getPath() {

        return path;
    }
}
