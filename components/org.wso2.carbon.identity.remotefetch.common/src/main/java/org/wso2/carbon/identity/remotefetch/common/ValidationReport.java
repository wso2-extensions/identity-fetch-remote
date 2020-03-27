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

package org.wso2.carbon.identity.remotefetch.common;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Hold Validation Messages.
 */
public class ValidationReport {

    public static enum VALIDATION_STATUS {
        PASSED,FAILED
    }
    private VALIDATION_STATUS validationStatus;
    private List<String> validationMessages = new ArrayList<>();

    /**
     * @param message
     */
    public void addMessage(String message) {

        this.validationMessages.add(message);
    }

    public void addMessage(String format, Object... args) {
        this.addMessage(new Formatter().format(format, args).toString());
    }

    /**
     * @return
     */
    public List<String> getMessages() {

        return this.validationMessages;
    }

    public VALIDATION_STATUS getValidationStatus() {

        return validationStatus;
    }

    public void setValidationStatus(VALIDATION_STATUS validationStatus) {

        this.validationStatus = validationStatus;
    }

    @Override
    public String toString() {

        return "ValidationReport{" +
                "validationStatus=" + validationStatus +
                ", validationMessages=" + validationMessages +
                '}';
    }
}
