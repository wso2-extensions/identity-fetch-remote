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
 * Since UI fields are dynamic, this class provide a report to process whether UI fields are valid or not.
 */
public class ValidationReport {

    //TODO:UUID

    /**
     * Status of validation report
     */
    public static enum ValidationStatus {
        PASSED, FAILED
    }

    private String id;
    private ValidationStatus validationStatus;
    private List<String> validationMessages = new ArrayList<>();

    /**
     * Add message into validation report.
     * @param message message
     */
    public void addMessage(String message) {

        this.validationMessages.add(message);
    }

    /**
     * add MessageForMandatoryValidation
     * @param field field
     */
    public void addMessageForMandatoryValidation(String field) {

        this.addMessage(new Formatter().format(ValidationFormat.MANDATORY_VALUE, field).toString());
    }

    /**
     * add MessageForPatternValidation
     * @param field field
     */
    public void addMessageForPatternValidation(String field) {

        this.addMessage(new Formatter().format(ValidationFormat.DOES_NOT_MATCH_PATTERN, field).toString());
    }

    /**
     * add MessageForActionListenerValidation
     * @param actionListenerType actionListenerType
     */
    public void addMessageForActionListenerValidation(String actionListenerType) {

        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_ACTION_LISTENER, actionListenerType)
                .toString());
    }

    /**
     * add MessageForRepoManagerValidation
     * @param repoManagerType repoManagerType
     */
    public void addMessageForRepoManagerValidation(String repoManagerType) {

        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_REPO_MANAGER, repoManagerType).toString());
    }

    /**
     * add MessageForConfigDeployerValidation
     * @param configDeployerType configDeployerType
     */
    public void addMessageForConfigDeployerValidation(String configDeployerType) {

        this.addMessage(new Formatter().format(ValidationFormat.NOT_A_VALID_CONFIG_DEPLOYER, configDeployerType)
                .toString());
    }

    /**
     * add MessageForComponentValidation
     * @param validationReport validationReport
     * @param componentName componentName
     */
    public void addMessageForComponentValidation(String validationReport, String componentName) {
        this.addMessage(new Formatter().format(ValidationFormat.COMPONENT_VALIDATION, validationReport, componentName)
                .toString());
    }

    /**
     * get Messages
     * @return validationMessages
     */
    public List<String> getMessages() {

        return this.validationMessages;
    }

    /**
     * get status of the report.
     * @return validationStatus
     */
    public ValidationStatus getValidationStatus() {

        return validationStatus;
    }

    /**
     * set ValidationStatus.
     * @param validationStatus validationStatus
     */
    public void setValidationStatus(ValidationStatus validationStatus) {

        this.validationStatus = validationStatus;
    }

    /**
     * get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set id of processed remote fetch configuration.
     * @param id UUID of remote fetch configuration.
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return "ValidationReport{" +
                "validationStatus=" + validationStatus +
                ", validationMessages=" + validationMessages +
                '}';
    }
}
