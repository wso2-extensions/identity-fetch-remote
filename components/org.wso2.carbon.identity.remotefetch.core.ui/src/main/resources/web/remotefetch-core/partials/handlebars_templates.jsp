<%--
  ~ Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~ WSO2 Inc. licenses this file to you under the Apache License,
  ~ Version 2.0 (the "License"); you may not use this file except
  ~ in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>

<script id="row-template" data-identifier="row" type="text/x-handlebars-template">
    <tr>
        <td class="leftCol-med labelField">
            {{obj.displayName}}:{{#if obj.isMandatory}}<span class="required">*</span>{{/if}}
        </td>
        <td>
            {{{element}}}
            
            <div class="sectionHelp">
                {{obj.helpText}}
            </div>
        </td>
    </tr>
</script>

<script id="element-textbox" data-identifier="TEXT_BOX" type="text/x-handlebars-template">
    <input name="{{obj.id}}"
           type="text"
           value="{{value}}"
           {{#if obj.validationRegex}} data-validation-pattern="{{obj.validationRegex}}" {{/if}}
           {{#if obj.isMandatory}} data-validation-required="true"{{/if}}
            data-validation-name="{{obj.displayName}}">
</script>

<script id="element-empty" data-identifier="NONE" type="text/x-handlebars-template">
    <p><i>No configuration options available</i></p>
    <br>
</script>