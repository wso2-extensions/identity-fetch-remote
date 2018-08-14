<%--
  ~ Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="carbon" uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.client.RemoteFetchRegistryClient" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.client.RemoteFetchConfigurationClient" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException" %>


<%
    Gson gson = new Gson();
    
    pageContext.setAttribute("fetchConfiguration",null);
    pageContext.setAttribute("fetchConfigurationJs",null);
    
    if( request.getParameter("id") != null ){
        int editId = -1;
        RemoteFetchConfiguration fetchConfiguration;
        
        try {
            editId = Integer.parseInt(request.getParameter("id"));
        }catch (NumberFormatException e){
            CarbonUIMessage.sendCarbonUIMessage("ID is invalid",CarbonUIMessage.ERROR,request,e);
        }
        
        if (editId != -1){
            try{
                fetchConfiguration = RemoteFetchConfigurationClient.getRemoteFetchConfiguration(editId);
                pageContext.setAttribute("fetchConfiguration",fetchConfiguration);
                pageContext.setAttribute("fetchConfigurationJs", gson.toJson(fetchConfiguration));
            }catch (RemoteFetchCoreException e){
                CarbonUIMessage.sendCarbonUIMessage("Invalid Config for id",CarbonUIMessage.ERROR,request,e);
            }
        }
    }
    
    
    pageContext.setAttribute("repoManagerList", RemoteFetchRegistryClient.getRepositoryManagers());
    pageContext.setAttribute("actionListenerList", RemoteFetchRegistryClient.getActionListener());
    pageContext.setAttribute("configDeployerList", RemoteFetchRegistryClient.getConfigDeployer());
    pageContext.setAttribute("componentUIFields", gson.toJson(RemoteFetchRegistryClient.getAllComponentUIFields()));
%>

<link rel="stylesheet" href="css/remotefetchcore.css">

<jsp:include page="../dialog/display_messages.jsp"/>

<fmt:bundle basename="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources">
    
    <carbon:breadcrumb
            label="remotefetch.add" resourceBundle="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources"
            topPage="true" request="<%=request%>"
    />
    
    <div id="middle">
        <h2><fmt:message key="remotefetch.core"/></h2>
        </br>
        
        <div id="workArea">
            <form id="basicInformationForm">
                <c:if test="${not empty fetchConfiguration}">
                    <input type="hidden" name="remoteFetchConfigurationId" 
                           value="${fetchConfiguration.remoteFetchConfigurationId}">
                </c:if>
                <div class="sectionSeperator togglebleTitle">Basic Information</div>
                <table class="carbonFormTable">
                    <tbody>
                        <tr>
                            <td class="leftCol-med labelField">Enabled:
                            </td>
                            <td>
                                <input id="isEnabled" name="isEnabled" type="checkbox" value="true"
                                       ${not empty fetchConfiguration and not fetchConfiguration.enabled ? "" : "checked"}>
                    
                                <div class="sectionHelp">
                                    Activate the configuration
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="leftCol-med labelField">Configuration type:<span class="required">*</span>
                            </td>
                            <td>
                                <select name="configurationDeployerType" id="configurationDeployerType"
                                        data-validation-required="true" data-validation-name="Configuration Deployer">
                                    <c:if test="${not empty configDeployerList and empty fetchConfiguration}">
                                        <option value="" selected disabled hidden>Choose Configuration Deployer</option>
                                    </c:if>
                                    <c:if test="${empty configDeployerList}">
                                        <option value="" selected disabled hidden>No Configuration Deployers registered</option>
                                    </c:if>
                                    <c:forEach items="${configDeployerList}" var="deployer">
                                        <option
                                                ${not empty fetchConfiguration and fetchConfiguration.configurationDeployerType == deployer.identifier ? "selected" : ""}
                                                value="${deployer.identifier}">
                                                ${deployer.name}
                                        </option>
                                    </c:forEach>
                                </select>
                    
                                <div class="sectionHelp">
                                    Configuration Deployer Type
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="leftCol-med labelField">Repository:<span class="required">*</span>
                            </td>
                            <td>
                                <select name="repositoryManagerType" id="repositoryManagerType"
                                        value="${not empty fetchConfiguration ? fetchConfiguration.repositoryManagerType : ""}"
                                        data-validation-required="true" data-validation-name="Repository Manager">
                                    <c:if test="${not empty repoManagerList and empty fetchConfiguration}">
                                        <option value="" selected disabled hidden>Choose Repository Manager</option>
                                    </c:if>
                                    <c:if test="${empty repoManagerList}">
                                        <option value="" selected disabled hidden>No Repository Managers registered</option>
                                    </c:if>
                                    <c:forEach items="${repoManagerList}" var="manager">
                                        <option
                                                ${not empty fetchConfiguration and fetchConfiguration.repositoryManagerType == manager.identifier ? "selected" : ""}
                                                value="${manager.identifier}">
                                                ${manager.name}
                                        </option>
                                    </c:forEach>
                                </select>
                    
                                <div class="sectionHelp">
                                    Repository Type
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="leftCol-med labelField">Action Listener:<span class="required">*</span>
                            </td>
                            <td>
                                <select name="actionListenerType" id="actionListenerType"
                                        value="${not empty fetchConfiguration ? fetchConfiguration.actionListenerType : ""}"
                                        data-validation-required="true" data-validation-name="Action Listener">
                                    <c:if test="${not empty actionListenerList and empty fetchConfiguration}">
                                        <option value="" selected disabled hidden>Choose Action Listener</option>
                                    </c:if>
                                    <c:if test="${empty actionListenerList}">
                                        <option value="" selected disabled hidden>No Action Listeners registered</option>
                                    </c:if>
                                    <c:forEach items="${actionListenerList}" var="listener">
                                        <option
                                                ${not empty fetchConfiguration and fetchConfiguration.actionListenerType == listener.identifier ? "selected" : ""}
                                                value="${listener.identifier}">
                                                ${listener.name}
                                        </option>
                                    </c:forEach>
                                </select>
                    
                                <div class="sectionHelp">
                                    Action Listener
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
            
            <form id="configurationDeployerForm">
                <div id="configurationDeployerSection" class="hide-block">
                    <div class="sectionSeperator togglebleTitle">Configuration Deployment</div>
                    <table class="carbonFormTable">
                        <tbody id="configurationDeployerPlaceholder"></tbody>
                    </table>
                </div>
            </form>
    
            <form id="repositoryManagerForm">
                <div id="repositoryManagerSection" class="hide-block">
                    <div class="sectionSeperator togglebleTitle">Repository Manager</div>
                    <table class="carbonFormTable">
                        <tbody id="repositoryManagerPlaceholder"></tbody>
                    </table>
                </div>
            </form>
    
            <form id="actionListenerForm">
                <div id="actionListenerSection" class="hide-block">
                    <div class="sectionSeperator togglebleTitle">Action Listener</div>
                    <table class="carbonFormTable">
                        <tbody id="actionListenerPlaceholder"></tbody>
                    </table>
                </div>
            </form>
            
            <div class="buttonRow">
                <input id="registerBtn" type="button" value="Insert">
                <input id="canncelBtn" type="button" value="Cancel">
            </div>
    
            <form id="hiddenForm" action="add-remotefetch-config-insert.jsp" method="post">
                <input id="jsonPayload" type="hidden" name="payload">
                <input id="hiddenFormAction" type="hidden" name="action" value="insert">
            </form>
            
        </div>
    </div>
    
    <div id="error-dialog-validation" class="ui-dialog-container rf-dialog">
        <div id="messagebox-error">
            <h2>Validation Errors</h2>
            <div id="error-dialog-validation-placeholder"></div>
        </div>
    </div>
    
    <%-- Set JS Variables --%>
    <script type="text/javascript">
        var remoteFetchState = {
            "configurationObject" : ${empty fetchConfigurationJs ? "null" : fetchConfigurationJs},
            "componentUIFields" : ${componentUIFields}
        }
    </script>
    <jsp:include page="partials/handlebars_templates.jsp"/>
    <script src="js/handlebars-v4.0.11.js"></script>
    <script src="js/main.js"></script>
</fmt:bundle>
