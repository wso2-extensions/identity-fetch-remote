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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.ConfigurationFileStream;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VelocityTemplatedSPDeployer extends ServiceProviderConfigDeployer {

    private static final Log log = LogFactory.getLog(VelocityTemplatedSPDeployer.class);

    private int id;

    public VelocityTemplatedSPDeployer(int tenantId, String userName, int id) {

        super(tenantId, userName, id);
        this.id = id;
    }

    @Override
    public void deploy(ConfigurationFileStream configurationFileStream) throws RemoteFetchCoreException {

        String velocityTemplate;
        try {
            velocityTemplate = IOUtils.toString(configurationFileStream.getContentStream());
        } catch (IOException e) {
            log.error("Failed to load template", e);
            return;
        }

        String fileNumber = String.valueOf(this.id);
        String workingDirectoryProperty = IdentityUtil.getProperty("RemoteFetch.WorkingDirectory");
        String workingDirectory = (new File(workingDirectoryProperty)).toString();

        HashMap<String, String> map = new HashMap<String, String>();
        ArrayList<String> arrayList = new ArrayList<String>();

        final Properties props = new Properties();
        props.setProperty("file.resource.loader.path",
                workingDirectory + "/repo-" + fileNumber + "/" + configurationFileStream.getPath().getParent());

        try {
            String currentLine;
            BufferedReader bufferedReader = new BufferedReader(new StringReader(velocityTemplate));
            while ((currentLine = bufferedReader.readLine()) != null) {
                Pattern pattern = Pattern.compile("(\\$\\w+)");
                Matcher matcher = pattern.matcher(currentLine);
                while (matcher.find()) {
                    String[] parts = matcher.group(1).split("\\$");
                    arrayList.add(parts[1]);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            log.error("Error while reading " + configurationFileStream.getPath(), e);
        }

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("/etc/environment"));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length >= 2) {
                    String key = parts[0];
                    String value = parts[1];
                    map.put(key, value);
                }
            }
            reader.close();
        } catch (IOException e) {
            log.error("Error while reading environment file", e);
        }

        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(props);
            VelocityContext context = new VelocityContext();

            for (String key : map.keySet()) {
                for (String string : arrayList) {
                    if (key.equalsIgnoreCase(string)) {
                        context.put(string, map.get(key));
                    }
                }
            }

            Template template = velocityEngine.getTemplate(configurationFileStream.getPath().getName());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            velocityTemplate = writer.toString();

        } catch (ResourceNotFoundException e) {
            log.error("Cannot find velocity template", e);
        }

        String spXml = velocityTemplate;

        ConfigurationFileStream updatedStream = new ConfigurationFileStream(IOUtils.toInputStream(spXml),
                configurationFileStream.getPath());

        super.deploy(updatedStream);
    }
}

