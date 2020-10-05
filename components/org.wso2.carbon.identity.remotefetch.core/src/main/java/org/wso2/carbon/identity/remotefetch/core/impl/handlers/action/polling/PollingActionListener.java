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

package org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.polling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager;
import org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.ActionListenerImpl;

import java.util.Calendar;

/**
 * ActionListener that polls repository with frequency for changes to be deployed.
 */
public class PollingActionListener extends ActionListenerImpl {

    private static final Log log = LogFactory.getLog(PollingActionListener.class);

    private Integer frequency;

    public PollingActionListener(RepositoryManager repo, ConfigDeployer configDeployer,
                                 int frequency, String remoteFetchConfigurationId, int tenantId) {

        super(repo, configDeployer, remoteFetchConfigurationId, tenantId);
        this.frequency = frequency;
    }

    /**
     * Contains logic to listen for updates for polling type.
     */
    @Override
    public void execute() {

        Calendar nextIteration = Calendar.getInstance();
        nextIteration.add(Calendar.MINUTE, this.frequency);
        if ((lastIteration == null) || (lastIteration.before(nextIteration.getTime()))) {
            super.execute();
            if (log.isDebugEnabled()) {
                log.debug("Polling Action Listener is executed`");
            }
        }
    }
}
