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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.dao.RemoteFetchConfigurationDAO;
import org.wso2.carbon.identity.remotefetch.core.dao.impl.RemoteFetchConfigurationDAOImpl;
import org.wso2.carbon.identity.remotefetch.core.executers.RemoteFetchTaskExecutor;

import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ID_UI_FIELD_BRANCH;
import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ID_UI_FIELD_DIRECTORY;
import static org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ID_UI_FIELD_URI;

/**
 * This class is used to handle web hook.{@see https://developer.github.com/webhooks/}
 */
public class WebHookHandler {

    private String url;
    private String branch;
    private List<String> modifiedFileNames;
    private RemoteFetchTaskExecutor remoteFetchTaskExecutor;
    private RemoteFetchConfigurationDAO remoteFetchConfigurationDAO;

    /**
     * Constructor to create web hook handler.
     *
     * @param url                     Remote repository clone url.
     * @param branch                  Remote repository branch.
     * @param modifiedFileNames       Modified file name list.
     * @param remoteFetchTaskExecutor Task Executor to schedule immediate task for valid web hooks.
     */
    public WebHookHandler(String url, String branch, List<String> modifiedFileNames,
                          RemoteFetchTaskExecutor remoteFetchTaskExecutor) {

        this.url = url;
        this.branch = branch;
        this.modifiedFileNames = modifiedFileNames;
        this.remoteFetchTaskExecutor = remoteFetchTaskExecutor;
        remoteFetchConfigurationDAO = new RemoteFetchConfigurationDAOImpl();
    }

    /**
     * This method is used to check if web hook matches with any remote fetch configuration for particular tenant.
     * If so schedule a immediate task for matched remote fetch configuration.
     *
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    public void handleWebHook() throws RemoteFetchCoreException {

        int tenantId = IdentityTenantUtil.getTenantId(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        List<RemoteFetchConfiguration> remoteFetchConfigurations =
                this.remoteFetchConfigurationDAO.getWebHookRemoteFetchConfigurationsByTenant(tenantId);
        for (RemoteFetchConfiguration remoteFetchConfiguration : remoteFetchConfigurations) {
            if (isWebHookMatches(remoteFetchConfiguration)) {
                remoteFetchTaskExecutor.startImmediateTaskExecution(remoteFetchConfiguration);
            }
        }
    }

    /**
     * Check whether given web hook params are matching with remote fetch configuration.
     *
     * @param remoteFetchConfiguration RemoteFetchConfiguration
     * @return flag used to point whether webhook matches with corresponding remote fetch configuration.
     */
    private boolean isWebHookMatches(RemoteFetchConfiguration remoteFetchConfiguration) {

        Map<String, String> repositoryManagerAttributes = remoteFetchConfiguration.getRepositoryManagerAttributes();

        return (StringUtils.equalsIgnoreCase(url, repositoryManagerAttributes.get(ID_UI_FIELD_URI))) &&
                (StringUtils.equalsIgnoreCase(branch, repositoryManagerAttributes.get(ID_UI_FIELD_BRANCH))) &&
                modifiedFileNames.stream().anyMatch(modifiedFileName ->
                        (modifiedFileName.startsWith(repositoryManagerAttributes.get(ID_UI_FIELD_DIRECTORY))));
    }
}
