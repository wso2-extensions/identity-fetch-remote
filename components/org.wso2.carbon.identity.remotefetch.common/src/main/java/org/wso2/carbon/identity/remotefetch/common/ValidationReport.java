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

    /**
     * Status of validation report
     */
    public static enum ValidationStatus {
        PASSED, FAILED
    }

    private ValidationStatus validationStatus;
    private List<String> validationMessages = new ArrayList<>();

    /**
     * @param message
     */
    public void addMessage(String message) {

        this.validationMessages.add(message);
    }


    public void addMessageForMandatoryValidation(String field) {
        this.addMessage(new Formatter().format(ValidationFormat.MANDATORY_VALUE, field).toString());
    }

    public void addMessageForPatternValidation(String field) {
        this.addMessage(new Formatter().format(ValidationFormat.DOES_NOT_MATCH_PATTERN, field).toString());
    }

    public void addMessageForActionListenerValidation(String actionListenerType) {
        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_ACTION_LISTENER, actionListenerType)
                .toString());
    }

    public void addMessageForRepoManagerValidation(String repoManagerType) {
        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_REPO_MANAGER, repoManagerType).toString());
    }

    public void addMessageForConfigDeployerValidation(String configDeployerType) {
        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_CONFIG_DEPLOYER, configDeployerType)
                .toString());
    }

    public void addMessageForComponentValidation(String validationReport, String componentName) {
        this.addMessage(new Formatter().format(ValidationFormat.COMPONENT_VALIDATION, validationReport, componentName)
                .toString());
    }

    /**
     * @return
     */
    public List<String> getMessages() {

        return this.validationMessages;
    }

    public ValidationStatus getValidationStatus() {

        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {

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
