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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchCoreConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchClientException;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchServerException;

import java.io.File;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

/**
 * Parser for core configuration from deployment.toml file.
 */
public class RemoteFetchConfigurationUtils {

    private static final Log log = LogFactory.getLog(RemoteFetchConfigurationUtils.class);


    /**
     * Parse configuration from deployment toml file.
     *
     * @return RemoteFetchCoreConfiguration
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    public static RemoteFetchCoreConfiguration parseConfiguration() throws RemoteFetchCoreException {

        boolean isEnabled = false;
        File workingDirectory = null;

        String isEnabledProperty = IdentityUtil.getProperty("RemoteFetch.FetchEnabled");
        String workingDirectoryProperty = IdentityUtil.getProperty("RemoteFetch.WorkingDirectory");

        if (isEnabledProperty != null && !isEnabledProperty.isEmpty()) {
            isEnabled = isEnabledProperty.equalsIgnoreCase("true");
        }

        if (workingDirectoryProperty != null && !workingDirectoryProperty.isEmpty()) {
            workingDirectory = new File(workingDirectoryProperty);
            validateDirectory(workingDirectory);
        }

        return new RemoteFetchCoreConfiguration(workingDirectory, isEnabled);

    }

    private static void validateDirectory(File workingDirectory) throws RemoteFetchCoreException {

        if (!workingDirectory.isDirectory()) {
            throw new RemoteFetchCoreException("Not a valid WorkingDirectory for RemoteFetchCore");
        }
    }

    /**
     * Generate UUID.
     *
     * @return UUID
     */
    public static String generateUniqueID() {

        return UUID.randomUUID().toString();
    }

    /**
     * Parse the Default Items per Page needed to display.
     *
     * @return defaultItemsPerPage
     */
    public static int getDefaultItemsPerPage() {

        int defaultItemsPerPage = RemoteFetchConstants.DEFAULT_ITEMS_PRE_PAGE;
        try {
            String defaultItemsPerPageProperty = IdentityUtil.getProperty(RemoteFetchConstants
                    .DEFAULT_ITEMS_PRE_PAGE_PROPERTY);
            if (StringUtils.isNotBlank(defaultItemsPerPageProperty)) {
                int defaultItemsPerPageConfig = Integer.parseInt(defaultItemsPerPageProperty);
                if (defaultItemsPerPageConfig > 0) {
                    defaultItemsPerPage = defaultItemsPerPageConfig;
                }
            }
        } catch (NumberFormatException e) {
            log.warn("Error occurred while parsing the 'DefaultItemsPerPage' property value in identity.xml.", e);
        }
        return defaultItemsPerPage;
    }

    /**
     * Get the Maximum Items per Page needed to display.
     *
     * @return maximumItemsPerPage
     */
    public static int getMaximumItemPerPage() {

        int maximumItemsPerPage = RemoteFetchConstants.DEFAULT_MAXIMUM_ITEMS_PRE_PAGE;
        String maximumItemsPerPagePropertyValue =
                IdentityUtil.getProperty(RemoteFetchConstants.MAXIMUM_ITEMS_PRE_PAGE_PROPERTY);
        if (StringUtils.isNotBlank(maximumItemsPerPagePropertyValue)) {
            try {
                maximumItemsPerPage = Integer.parseInt(maximumItemsPerPagePropertyValue);
            } catch (NumberFormatException e) {
                log.warn("Error occurred while parsing the 'MaximumItemsPerPage' property value in identity.xml.", e);
            }
        }
        return maximumItemsPerPage;
    }


    /**
     * This method is used to generate RemoteFetchClientException from RemoteFetchConstants.ErrorMessage
     * when no exception is thrown.
     *
     * @param error RemoteFetchConstants.ErrorMessage
     * @param data  data to replace if message needs to be replaced.
     * @return RemoteFetchClientException
     */
    public static RemoteFetchClientException handleClientException(RemoteFetchConstants.ErrorMessage error,
                                                                   String data) {

        String message = includeData(error, data);
        return new RemoteFetchClientException(error.getCode(), message);
    }

    /**
     * This method is used to generate RemoteFetchClientException from RemoteFetchConstants.ErrorMessage
     * when no exception is thrown.
     *
     * @param error RemoteFetchConstants.ErrorMessage
     * @param data  data to replace if message needs to be replaced.
     * @return RemoteFetchClientException
     */
    public static RemoteFetchClientException handleClientException(RemoteFetchConstants.ErrorMessage error,
                                                                   List<String> data) {

        StringBuilder exceptionStringBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String exceptionString : data) {
                exceptionStringBuilder.append(exceptionString);
                exceptionStringBuilder.append(System.lineSeparator());
            }
        }
        String message = includeData(error, exceptionStringBuilder.toString());
        return new RemoteFetchClientException(error.getCode(), message);
    }


    /**
     * This method is used to generate RemoteFetchServerException from RemoteFetchConstants.ErrorMessage
     * when no exception is thrown.
     *
     * @param error RemoteFetchConstants.ErrorMessage
     * @param data  data to replace if message needs to be replaced.
     * @return RemoteFetchServerException
     */
    public static RemoteFetchServerException handleServerException(RemoteFetchConstants.ErrorMessage error,
                                                                   String data) {

        String message = includeData(error, data);
        return new RemoteFetchServerException(error.getCode(), message);
    }

    /**
     * This method is used to generate RemoteFetchServerException from RemoteFetchConstants.ErrorMessage
     * when no exception is thrown.
     *
     * @param error RemoteFetchConstants.ErrorMessage
     * @param data  data to replace if message needs to be replaced.
     * @param e     Throwable
     * @return RemoteFetchServerException
     */
    public static RemoteFetchServerException handleServerException(RemoteFetchConstants.ErrorMessage error,
                                                                   String data,
                                                                   Throwable e) {

        String message = includeData(error, data);
        return new RemoteFetchServerException(error.getCode(), message, e);
    }

    /**
     * This method is used to generate RemoteFetchServerException from RemoteFetchConstants.ErrorMessage
     * when no exception is thrown.
     *
     * @param error RemoteFetchConstants.ErrorMessage
     * @param e     Throwable
     * @return RemoteFetchServerException
     */
    public static RemoteFetchServerException handleServerException(RemoteFetchConstants.ErrorMessage error,
                                                                   Throwable e) {

        String message = error.getMessage();
        return new RemoteFetchServerException(error.getCode(), message, e);
    }

    private static String includeData(RemoteFetchConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = new Formatter().format(error.getMessage(), data).toString();
        } else {
            message = error.getMessage();
        }
        return message;
    }

    /**
     * Trim Error message inorder to store in db
     * @param message Error Message
     * @param inspectThrowable Throwable to get full stack trace
     * @return full trimmed error message
     */
    public static String trimErrorMessage(String message, Throwable inspectThrowable) {

        StringBuilder errorStringBuilder = new StringBuilder();
        errorStringBuilder.append(message);

        String fullMessage = ExceptionUtils.getFullStackTrace(inspectThrowable);

        errorStringBuilder.append(fullMessage);

        String errorMessage = errorStringBuilder.toString();
        if (errorMessage.length() >= Integer.MAX_VALUE) {
            return errorMessage.substring(Integer.MAX_VALUE);
        }
        return errorMessage;
    }
}
