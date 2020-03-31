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

package org.wso2.carbon.identity.remotefetch.core.util;

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchComponentRegistry;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.ValidationReport;
import org.wso2.carbon.identity.remotefetch.common.actionlistener.ActionListenerComponent;
import org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployerComponent;
import org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManagerComponent;
import org.wso2.carbon.identity.remotefetch.common.ui.UIField;
import org.wso2.carbon.identity.remotefetch.common.ui.UIFieldValidator;

import java.util.List;
import java.util.Map;

public class RemoteFetchConfigurationValidator {

    private RemoteFetchComponentRegistry fetchComponentRegistry;
    private ActionListenerComponent actionListenerComponent;
    private ConfigDeployerComponent configDeployerComponent;
    private RepositoryManagerComponent repositoryManagerComponent;
    private RemoteFetchConfiguration fetchConfiguration;
    private ValidationReport validationReport = new ValidationReport();

    public RemoteFetchConfigurationValidator(RemoteFetchComponentRegistry fetchComponentRegistry,
                                             RemoteFetchConfiguration fetchConfiguration) {

        this.fetchComponentRegistry = fetchComponentRegistry;
        this.fetchConfiguration = fetchConfiguration;

    }

    private void getComponents() {
        this.actionListenerComponent =
                this.fetchComponentRegistry.getActionListenerComponent(fetchConfiguration
                        .getActionListenerType());

        this.configDeployerComponent =
                this.fetchComponentRegistry.getConfigDeployerComponent(fetchConfiguration
                        .getConfigurationDeployerType());

        this.repositoryManagerComponent =
                this.fetchComponentRegistry.getRepositoryManagerComponent(fetchConfiguration
                        .getRepositoryManagerType());
    }

    public ValidationReport validate() {

        if (!this.checkBasicFields()) {
            return this.returnReport();
        }
        if (!this.checkEmpty()) {
            return this.returnReport();
        }

        this.getComponents();

        if (!this.checkComponents()) {
            return this.returnReport();
        }
        if (this.actionListenerComponent.getUIFields() != null) {

            this.checkAttributes(this.fetchConfiguration.getActionListenerAttributes(),
                    this.actionListenerComponent.getUIFields(), this.actionListenerComponent.getName());
        }

        if (this.configDeployerComponent.getUIFields() != null) {

            this.checkAttributes(this.fetchConfiguration.getConfigurationDeployerAttributes(),
                    this.configDeployerComponent.getUIFields(), this.configDeployerComponent.getName());
        }

        if (this.repositoryManagerComponent.getUIFields() != null) {

            this.checkAttributes(this.fetchConfiguration.getRepositoryManagerAttributes(),
                    this.repositoryManagerComponent.getUIFields(), this.repositoryManagerComponent.getName());
        }

        return this.returnReport();

    }

    private boolean checkBasicFields() {
        if (this.fetchConfiguration.getUserName() == null && !this.fetchConfiguration.getUserName().isEmpty()){
            this.validationReport.addMessage("username field is empty");
            return true;
        }else{
            return false;
        }
    }

    private boolean checkEmpty() {
        boolean isValid = true;
        if(this.fetchConfiguration.getActionListenerType().isEmpty()){
            this.validationReport.addMessage("Empty field provided for Action Listener Type");
            isValid = false;
        }
        if(this.fetchConfiguration.getConfigurationDeployerType().isEmpty()){
            this.validationReport.addMessage("Empty field provided for Config Deployer Type");
            isValid = false;
        }
        if(this.fetchConfiguration.getRepositoryManagerType().isEmpty()){
            this.validationReport.addMessage("Empty field provided for Repository Manager Type");
            isValid = false;
        }
        return isValid;
    }

    private boolean checkComponents() {

        boolean isValid = true;

        if (actionListenerComponent == null) {
            this.validationReport.addMessage("%s is not a valid Action Listener",
                    this.fetchConfiguration.getActionListenerType());
            isValid = false;
        }
        if (configDeployerComponent == null) {
            this.validationReport.addMessage("%s is not a valid Config Deployer",
                    this.fetchConfiguration.getConfigurationDeployerType());
            isValid = false;
        }
        if (repositoryManagerComponent == null) {
            this.validationReport.addMessage("%s is not a valid Repository Manager",
                    this.fetchConfiguration.getRepositoryManagerType());
            isValid = false;
        }

        return isValid;
    }

    private void checkAttributes(Map<String, String> attributes, List<UIField> fieldList, String component_name) {

        ValidationReport componentReport = UIFieldValidator.validate(attributes, fieldList);
        for (String report : componentReport.getMessages()) {
            this.validationReport.addMessage("%s for %s", report, component_name);
        }
    }

    private ValidationReport returnReport(){
        if(this.validationReport.getMessages().size() == 0){
            this.validationReport.setValidationStatus(ValidationReport.ValidationStatus.PASSED);
        }else{
            this.validationReport.setValidationStatus(ValidationReport.ValidationStatus.FAILED);
        }
        return this.validationReport;
    }
}
