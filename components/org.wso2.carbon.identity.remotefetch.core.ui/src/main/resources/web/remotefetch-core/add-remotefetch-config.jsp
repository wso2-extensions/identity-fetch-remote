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
    RemoteFetchConfiguration fetchConfiguration = null;
    String componentUIFields = gson.toJson(RemoteFetchRegistryClient.getAllComponentUIFields());
    pageContext.setAttribute("fetchConfigurationJs", null);

    String editId = request.getParameter("id");
    if (editId != null) {
        

        try {
            fetchConfiguration = RemoteFetchConfigurationClient.getRemoteFetchConfiguration(editId);
            pageContext.setAttribute("fetchConfigurationJs", gson.toJson(fetchConfiguration));
        } catch (RemoteFetchCoreException e) {
            CarbonUIMessage.sendCarbonUIMessage("Invalid Config for id", CarbonUIMessage.ERROR, request, e);
        }
    }
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
                <% if (fetchConfiguration != null) {%>
                <input type="hidden" name="remoteFetchConfigurationId"
                       value="<%=fetchConfiguration.getRemoteFetchConfigurationId()%>">
                <% } %>
                <div class="sectionSeperator togglebleTitle">Basic Information</div>
                <table class="carbonFormTable">
                    <tbody>
                    
                    <tr>
                        <td class="leftCol-med labelField">Repository Configuration Name:</td>
                        <td>
                            <input id="remoteFetchName" name="remoteFetchName" type="text"
                                   value=<%=(fetchConfiguration != null) ? fetchConfiguration.getRemoteFetchName() : "" %>>
                            <div class="sectionHelp">
                                Name for the repository configuration
                            </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td class="leftCol-med labelField">Enabled:<span class="required">*</span></td>
                        <td>
                            <input id="isEnabled" name="isEnabled" type="checkbox" value="true"
                                    <%=(fetchConfiguration != null && (!fetchConfiguration.isEnabled())) ? "" : "checked"%>>
                            <div class="sectionHelp">
                                Activate the configuration
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
            
            <form id="configurationDeployerForm">
                <div id="configurationDeployerSection" class="hide-block">
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
                <input id="id" type="hidden" name="id" value=<%=(fetchConfiguration != null) ? fetchConfiguration.getRemoteFetchConfigurationId() : null %>>
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
    
    <script type="text/javascript">
        var remoteFetchState = {
            "configurationObject": <%= pageContext.getAttribute("fetchConfigurationJs") == null? "null" : pageContext.getAttribute("fetchConfigurationJs") %>,
            "componentUIFields": <%= componentUIFields%>
        }
    </script>
    <jsp:include page="partials/handlebars_templates.jsp"/>
    <script src="js/handlebars.min-v4.7.7.js"></script>
    <script src="js/main.js"></script>
</fmt:bundle>