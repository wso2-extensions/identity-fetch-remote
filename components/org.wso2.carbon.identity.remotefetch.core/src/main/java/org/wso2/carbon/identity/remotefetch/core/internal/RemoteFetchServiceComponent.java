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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchCoreConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.RemoteFetchComponentRegistryImpl;
import org.wso2.carbon.identity.remotefetch.core.RemoteFetchConfigurationServiceImpl;
import org.wso2.carbon.identity.remotefetch.core.executers.RemoteFetchTaskExecutor;
import org.wso2.carbon.identity.remotefetch.core.impl.deployers.config.ServiceProviderConfigDeployerComponent;
import org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.polling.PollingActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.webhook.WebHookActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.core.impl.handlers.repository.GitRepositoryManagerComponent;
import org.wso2.carbon.identity.remotefetch.core.util.RemoteFetchConfigurationUtils;
import org.wso2.carbon.identity.xds.client.mgt.XDSClientService;
import org.wso2.carbon.user.core.service.RealmService;

import javax.sql.DataSource;

/**
 * Holds functions for service component.
 */
@Component(
        name = "identity.application.remotefetch.component",
        immediate = true
)
public class RemoteFetchServiceComponent {

    private static final Log log = LogFactory.getLog(RemoteFetchServiceComponent.class);
    private RemoteFetchTaskExecutor remoteFetchTaskExecutor;


    @Activate
    protected void activate(ComponentContext context) {

        RemoteFetchComponentRegistry remoteFetchComponentRegistry = new RemoteFetchComponentRegistryImpl();
        remoteFetchTaskExecutor = new RemoteFetchTaskExecutor();
        remoteFetchTaskExecutor.createScheduler();
        RemoteFetchConfigurationService remoteFetchConfigurationService =
                            new RemoteFetchConfigurationServiceImpl(remoteFetchTaskExecutor);
        RemoteFetchCoreConfiguration fetchCoreConfiguration = this.parseRemoteFetchCoreConfiguration();

        remoteFetchComponentRegistry.registerRepositoryManager(new GitRepositoryManagerComponent());
        remoteFetchComponentRegistry.registerConfigDeployer(new ServiceProviderConfigDeployerComponent());
        remoteFetchComponentRegistry.registerActionListener(new PollingActionListenerComponent());
        remoteFetchComponentRegistry.registerActionListener(new WebHookActionListenerComponent());

        RemoteFetchServiceComponentHolder.getInstance().setRemoteFetchComponentRegistry(remoteFetchComponentRegistry);
        RemoteFetchServiceComponentHolder.getInstance()
                .setRemoteFetchConfigurationService(remoteFetchConfigurationService);

        RemoteFetchServiceComponentHolder.getInstance().setDataSource(this.getDataSource());
        RemoteFetchServiceComponentHolder.getInstance().setFetchCoreConfiguration(fetchCoreConfiguration);

        BundleContext bundleContext = context.getBundleContext();
        bundleContext.registerService(RemoteFetchComponentRegistry.class.getName(),

                RemoteFetchServiceComponentHolder.getInstance().getRemoteFetchComponentRegistry(), null);
        bundleContext.registerService(RemoteFetchConfigurationService.class.getName(),

                RemoteFetchServiceComponentHolder.getInstance().getRemoteFetchConfigurationService(), null);

        if (fetchCoreConfiguration.isEnableCore()) {

            remoteFetchTaskExecutor.startBatchTaskExecution();
        }
        if (log.isDebugEnabled()) {
            log.debug("Identity RemoteFetchServiceComponent bundle is activated");
        }

    }

    @Deactivate
    protected void deactivate(ComponentContext context) {

        remoteFetchTaskExecutor.shutdownScheduler();
        if (log.isDebugEnabled()) {
            log.debug("Identity RemoteFetchServiceComponent bundle is deactivated");
        }
    }

    @Reference(
            name = "user.applicationmanagementservice.default",
            service = ApplicationManagementService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetApplicationManagerService"
    )
    protected void setApplicationManagerService(ApplicationManagementService applicationManagerService) {

        RemoteFetchServiceComponentHolder.getInstance().setApplicationManagementService(applicationManagerService);
    }

    protected void unsetApplicationManagerService(ApplicationManagementService applicationManagerService) {

        RemoteFetchServiceComponentHolder.getInstance().setApplicationManagementService(null);
    }

    @Reference(
            name = "user.realmservice.default",
            service = RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {

        RemoteFetchServiceComponentHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {

        RemoteFetchServiceComponentHolder.getInstance().setRealmService(null);
    }

    @Reference(
            name = "identityCoreInitializedEventService",
            service = IdentityCoreInitializedEvent.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityCoreInitializedEventService"
    )
    protected void setIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    protected void unsetIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    private DataSource getDataSource() {

        return IdentityDatabaseUtil.getDataSource();
    }

    private RemoteFetchCoreConfiguration parseRemoteFetchCoreConfiguration() {

        try {
            return RemoteFetchConfigurationUtils.parseConfiguration();
        } catch (RemoteFetchCoreException e) {
            log.error("Error parsing RemoteFetchCoreConfiguration, Core disabled", e);
            return new RemoteFetchCoreConfiguration(null, false);
        }
    }

    @Reference(
            name = "xds.client.service",
            service = org.wso2.carbon.identity.xds.client.mgt.XDSClientService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetXDSClientService")
    protected void setXDSClientService(XDSClientService xdsClientService) {

        RemoteFetchServiceComponentHolder.getInstance().setXdsClientService(xdsClientService);
    }

    protected void unsetXDSClientService(XDSClientService xdsClientService) {

        RemoteFetchServiceComponentHolder.getInstance().setXdsClientService(null);
    }
}
