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

package org.wso2.carbon.identity.remotefetch.core.ui.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;

@Component(
        name = "org.wso2.carbon.identity.remotefetch.core.ui",
        immediate = true
)
public class RemotefetchCoreUIComponent {
    private static final Log log = LogFactory.getLog(RemotefetchCoreUIComponent.class);

    @Activate
    protected void activate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("RemoteFetch Core UI bundle activated!");
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("RemoteFetch Core UI bundle deactivated!");
        }
    }

    @Reference(
            name = "config.remotefetch.registry.service",
            service = RemoteFetchComponentRegistry.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRemoteFetchComponentRegistryReference"
    )
    protected void setRemoteFetchComponentRegistryReference(RemoteFetchComponentRegistry componentRegistry){
        RemotefetchCoreUIComponentDataHolder.getInstance().setComponentRegistry(componentRegistry);

    }
    protected void unsetRemoteFetchComponentRegistryReference(RemoteFetchComponentRegistry componentRegistry){
        RemotefetchCoreUIComponentDataHolder.getInstance().setComponentRegistry(null);
    }

    @Reference(
            name = "config.remotefetch.configuration.service",
            service = RemoteFetchConfigurationService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRemoteFetchConfigurationServiceReference"
    )
    protected void setRemoteFetchConfigurationServiceReference(
            RemoteFetchConfigurationService fetchConfigurationService){

        RemotefetchCoreUIComponentDataHolder.getInstance()
                .setRemoteFetchConfigurationService(fetchConfigurationService);

    }
    protected void unsetRemoteFetchConfigurationServiceReference(
            RemoteFetchConfigurationService fetchConfigurationService){

        RemotefetchCoreUIComponentDataHolder.getInstance().setRemoteFetchConfigurationService(null);
    }
}
