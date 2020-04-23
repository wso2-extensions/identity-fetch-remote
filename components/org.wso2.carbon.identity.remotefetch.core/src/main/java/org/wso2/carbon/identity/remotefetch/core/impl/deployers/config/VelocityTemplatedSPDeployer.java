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

package org.wso2.carbon.identity.remotefetch.core.impl.deployers.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.ConfigurationFileStream;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configure service provider using environment variables.
 */
public class VelocityTemplatedSPDeployer extends ServiceProviderConfigDeployer {

    private static final Log log = LogFactory.getLog(VelocityTemplatedSPDeployer.class);

    private String id;

    public VelocityTemplatedSPDeployer(int tenantId, String userName, String id) {

        super(tenantId, userName, id);
        this.id = id;
    }

    /**
     * Parameter replacement.
     *
     * @param configurationFileStream
     * @throws RemoteFetchCoreException
     */
    @Override
    public void deploy(ConfigurationFileStream configurationFileStream) throws RemoteFetchCoreException, IOException {

        String velocityTemplate;
        velocityTemplate = IOUtils.toString(configurationFileStream.getContentStream());

        String fileNumber = String.valueOf(this.id);
        String workingDirectory = IdentityUtil.getProperty("RemoteFetch.WorkingDirectory");

        ArrayList<String> arrayList = new ArrayList<String>();

        final Properties props = new Properties();
        props.setProperty("file.resource.loader.path",
                workingDirectory + "/repo-" + fileNumber + "/" + configurationFileStream.getPath().getParent());


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


        BufferedReader reader = null;
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(props);
            VelocityContext context = new VelocityContext();

            String line;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("/etc/environment"), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if ((parts.length >= 2) && (arrayList.contains(parts[0].toLowerCase(Locale.ENGLISH)))) {
                    String key = parts[0].toLowerCase(Locale.ENGLISH);
                    String value = parts[1];
                    context.put(key, value);
                }
            }

            Template template = velocityEngine.getTemplate(configurationFileStream.getPath().getName());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            velocityTemplate = writer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        String spXml = velocityTemplate;

        ConfigurationFileStream updatedStream = new ConfigurationFileStream(IOUtils.toInputStream(spXml),
                configurationFileStream.getPath());

        super.deploy(updatedStream);
    }
}

