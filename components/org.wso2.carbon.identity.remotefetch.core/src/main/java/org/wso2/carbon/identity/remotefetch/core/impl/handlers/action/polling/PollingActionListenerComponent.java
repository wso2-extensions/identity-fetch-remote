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

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerBuilder;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.ui.UIField;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data for UI elements.
 */
public class PollingActionListenerComponent implements ActionListenerComponent {

    /**
     * Returns a new builder object for the component.
     *
     * @return ActionListenerBuilder
     */
    @Override
    public ActionListenerBuilder getActionListenerBuilder() {

        return new PollingActionListenerBuilder();
    }

    /**
     * Returns a string of the unique identifier of the component.
     *
     * @return ID
     */
    @Override
    public String getIdentifier() {

        return RemoteFetchConstants.IDENTIFIER_POLLING_ACTION_LISTENER_COMPONENT;
    }

    /**
     * Returns screen name of the component.
     *
     * @return Name
     */
    @Override
    public String getName() {

        return "Frequent Polling";
    }

    /**
     * Returns list of UIFields to configure UI.
     *
     * @return List of UI fields.
     */
    @Override
    public List<UIField> getUIFields() {

        ArrayList<UIField> fieldList = new ArrayList();

        fieldList.add(new UIField(
                "frequency", UIField.FieldTypes.TEXT_BOX, "Polling Frequency",
                "Number of seconds polling should occur", "^\\d+$", "60",
                true, false, false
        ));
        return fieldList;
    }
}
