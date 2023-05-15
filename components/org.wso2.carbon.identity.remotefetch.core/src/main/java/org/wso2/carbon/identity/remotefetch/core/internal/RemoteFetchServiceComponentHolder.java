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

package org.wso2.carbon.identity.remotefetch.core.internal;

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchCoreConfiguration;
import org.wso2.carbon.identity.xds.client.mgt.XDSClientService;
import org.wso2.carbon.user.core.service.RealmService;

import javax.sql.DataSource;

/**
 * Holds remote fetch service component.
 */
public class RemoteFetchServiceComponentHolder {

    private static RemoteFetchServiceComponentHolder instance = new RemoteFetchServiceComponentHolder();
    private ApplicationManagementService applicationManagementService;
    private RemoteFetchConfigurationService remoteFetchConfigurationService;
    private RemoteFetchComponentRegistry remoteFetchComponentRegistry;
    private RealmService realmService;
    private DataSource dataSource;
    private RemoteFetchCoreConfiguration fetchCoreConfiguration;
    private XDSClientService xdsClientService;

    public static RemoteFetchServiceComponentHolder getInstance() {

        return instance;
    }

    public RemoteFetchComponentRegistry getRemoteFetchComponentRegistry() {

        return remoteFetchComponentRegistry;
    }

    public void setRemoteFetchComponentRegistry(RemoteFetchComponentRegistry remoteFetchComponentRegistry) {

        this.remoteFetchComponentRegistry = remoteFetchComponentRegistry;
    }

    public RemoteFetchConfigurationService getRemoteFetchConfigurationService() {

        return remoteFetchConfigurationService;
    }

    public void setRemoteFetchConfigurationService(RemoteFetchConfigurationService remoteFetchConfigurationService) {

        this.remoteFetchConfigurationService = remoteFetchConfigurationService;
    }

    public ApplicationManagementService getApplicationManagementService() {

        return applicationManagementService;
    }

    public void setApplicationManagementService(ApplicationManagementService applicationManagementService) {

        this.applicationManagementService = applicationManagementService;
    }

    public RealmService getRealmService() {

        return realmService;
    }

    public void setRealmService(RealmService realmService) {

        this.realmService = realmService;
    }

    public DataSource getDataSource() {

        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public RemoteFetchCoreConfiguration getFetchCoreConfiguration() {

        return fetchCoreConfiguration;
    }

    public void setFetchCoreConfiguration(RemoteFetchCoreConfiguration fetchCoreConfiguration) {

        this.fetchCoreConfiguration = fetchCoreConfiguration;
    }

    public XDSClientService getXdsClientService() {

        return xdsClientService;
    }

    public void setXdsClientService(XDSClientService xdsClientService) {

        this.xdsClientService = xdsClientService;
    }

}
