<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.client.RemoteFetchConfigurationClient" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.ValidationReport" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException" %>

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

<%
    String httpMethod = request.getMethod();
    String redirect = "list-remotefetch-configs.jsp";
    
    if (!"post".equalsIgnoreCase(httpMethod)) {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }
    
    String payload = request.getParameter("payload");
    String action = request.getParameter("action");
    
    if (payload != null && !payload.isEmpty() && action != null && !action.isEmpty()) {
        ValidationReport validationReport = null;
        try {
            String currentUser = (String) session.getAttribute("logged-user");
            
            if (action.equalsIgnoreCase("insert")) {
                validationReport = RemoteFetchConfigurationClient.addFetchConfiguration(payload, currentUser);
                CarbonUIMessage.sendCarbonUIMessage("Configuration successfully added!", CarbonUIMessage.INFO, request);
            } else if (action.equalsIgnoreCase("update")) {
                validationReport = RemoteFetchConfigurationClient.updateFetchConfiguration(payload, currentUser);
                CarbonUIMessage.sendCarbonUIMessage("Configuration successfully updated!", CarbonUIMessage.INFO,
                        request);
            }
        } catch (RemoteFetchCoreException e) {
            CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
            redirect = "add-remotefetch-config.jsp";
        }
        
        if (validationReport != null &&
                validationReport.getValidationStatus() == ValidationReport.VALIDATION_STATUS.FAILED) {
            CarbonUIMessage.sendCarbonUIMessage("Validation Error", CarbonUIMessage.ERROR, request);
            redirect = "add-remotefetch-config.jsp";
        }
        
    } else {
        CarbonUIMessage.sendCarbonUIMessage("Empty Payload sent", CarbonUIMessage.ERROR, request);
        redirect = "add-remotefetch-config.jsp";
    }
    redirect = redirect;
%>


<script>
    location.href = "<%=redirect%>";
</script>