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

function UIFieldGenerator() {
    //Extract templates from script type and compile.
    this.templates = $('script[type="text/x-handlebars-template"]');
    this.compiledTemplates = {};
    var context = this;
    $.each(this.templates, function (i, template) {
        context.compiledTemplates[template.dataset.identifier] = Handlebars.compile(template.innerHTML);
    });
}

UIFieldGenerator.prototype.renderRows = function (fields, values) {
    var elements = [];
    var context = this;
    $.each(fields, function (i, field) {
        var default_value = typeof field.defaultValues === undefined ? "" : field.defaultValues[0];
        var value = values[field.id] ? values[field.id] : default_value;
        elements.push(
            context.renderRow(field, value)
        );
    });
    return elements;
};

UIFieldGenerator.prototype.renderRow = function (field, value) {
    var element = this.renderField(field.type, field, value);
    return this.compiledTemplates["row"]({
        obj: field,
        element: element
    });
};

UIFieldGenerator.prototype.renderField = function (templateName, field, value) {
    return this.compiledTemplates[templateName]({
        obj: field,
        value: value
    });
};

UIFieldGenerator.prototype.validateForm = function (form) {
    var validation_messages = [];
    $(form[0].elements).each(function (i, item) {
        var is_required = item.getAttribute("data-validation-required") === "true";
        var pattern = item.getAttribute("data-validation-pattern");
        var name = item.getAttribute("data-validation-name");

        if (item.value == "" && is_required) {
            validation_messages.push(name + " is a required field.");
            return;
        }

        if (pattern) {
            var re = new RegExp(pattern);
            if (!re.test(item.value)) {
                validation_messages.push(name + " didn't meet pattern requirements.");
            }
        }
    });
    return validation_messages;
};

// Main Entry

function updatePartial(selected, componentSet, section, placeholder, valueset) {
    $(section).slideUp().after(function () {
        $(placeholder).html("");
        var fields = componentSet[selected];
        if (fields) {
            $(uiGen.renderRows(fields, valueset)).each(function (i, element) {
                $(placeholder).append(element);
            });
        } else {
            $(placeholder).append(uiGen.renderField("NONE", undefined, undefined));
        }
        $(section).slideDown();
    });
}

function validateForms() {
    var validation = uiGen.validateForm($("#basicInformationForm"));
    if (validation.length == 0) {
        validation = validation.concat(uiGen.validateForm($("#repositoryManagerForm")));
        validation = validation.concat(uiGen.validateForm($("#configurationDeployerForm")));
        validation = validation.concat(uiGen.validateForm($("#actionListenerForm")));
    }
    return validation;
}

function serializeForm(selector) {
    var payload = {};
    $($(selector).serializeArray()).each(function (i, value) {
        if(typeof value.value === "string") {
            payload[value.name] = value.value.replace(/"/g, '');
        }else{
            payload[value.name] = value.value;
        }
    });
    return payload;
}

function makePayload() {
    var payload = serializeForm("#basicInformationForm");
    payload["configurationDeployerAttributes"] = serializeForm("#configurationDeployerForm");
    payload["repositoryManagerAttributes"] = serializeForm("#repositoryManagerForm");
    payload["actionListenerAttributes"] = serializeForm("#actionListenerForm");
    return payload;
}

function populateForms(config){
    updatePartial(
        config.configurationDeployerType, remoteFetchState.componentUIFields.configDeployer,
        "#configurationDeployerSection", "#configurationDeployerPlaceholder", config.configurationDeployerAttributes
    );
    updatePartial(
        config.repositoryManagerType, remoteFetchState.componentUIFields.repositoryManager,
        "#repositoryManagerSection", "#repositoryManagerPlaceholder", config.repositoryManagerAttributes
    );
    updatePartial(
        config.actionListenerType, remoteFetchState.componentUIFields.actionListener,
        "#actionListenerSection", "#actionListenerPlaceholder", config.actionListenerAttributes
    );
}

var uiGen = undefined;

$(document).ready(function () {
    uiGen = new UIFieldGenerator();

    $("#error-dialog-validation").dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            OK: function () {
                $(this).dialog("close");
            }
        },
        width: "fit-content"
    });

    $("#configurationDeployerType").change(function () {
        updatePartial(
            this.value, remoteFetchState.componentUIFields.configDeployer,
            "#configurationDeployerSection", "#configurationDeployerPlaceholder", {}
        );
    });

    $("#repositoryManagerType").change(function () {
        updatePartial(
            this.value, remoteFetchState.componentUIFields.repositoryManager,
            "#repositoryManagerSection", "#repositoryManagerPlaceholder", {}
        );
    });

    $("#actionListenerType").change(function () {
        updatePartial(
            this.value, remoteFetchState.componentUIFields.actionListener,
            "#actionListenerSection", "#actionListenerPlaceholder", {}
        );
    });

    $("#registerBtn").click(function () {
        var validation = validateForms();

        if (validation.length == 0) {
            $("#jsonPayload").val(JSON.stringify(makePayload()));
            $("#hiddenForm").submit();
        } else {
            $("#error-dialog-validation-placeholder").html("");
            $(validation).each(function (i, msg) {
                $("#error-dialog-validation-placeholder").append("<p>" + msg + "</p>")
            });
            $("#error-dialog-validation").dialog("open");
        }
    });

    $("#canncelBtn").click(function () {
        window.location.href = "list-remotefetch-configs.jsp";
    });

    if (remoteFetchState.configurationObject){
        populateForms(remoteFetchState.configurationObject);
        $("#hiddenFormAction").val("update");
        $("#registerBtn").val("Update");
    }

});