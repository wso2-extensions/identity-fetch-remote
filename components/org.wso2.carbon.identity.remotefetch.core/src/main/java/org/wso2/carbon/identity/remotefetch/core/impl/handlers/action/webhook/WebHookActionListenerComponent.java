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

package org.wso2.carbon.identity.remotefetch.core.impl.handlers.action.webhook;

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilder;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.ui.UIField;

import java.util.List;

/**
 *  Action Listener Component class to provide web hook action listener builder,
 */
public class WebHookActionListenerComponent implements ActionListenerComponent {

    @Override
    public ActionListenerBuilder getActionListenerBuilder() {

        return new WebHookActionListenerBuilder();
    }

    @Override
    public String getIdentifier() {

        return RemoteFetchConstants.IDENTIFIER_WEB_HOOK_ACTION_LISTENER_COMPONENT;
    }

    @Override
    public String getName() {

        return "GitHub Web Hook";
    }

    @Override
    public List<UIField> getUIFields() {

        return null;
    }
}
