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
    
    if (request.getParameter("id") != null) {
        int id = -1;
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            CarbonUIMessage.sendCarbonUIMessage("ID is invalid", CarbonUIMessage.ERROR, request, e);
        }
        
        if (id != -1) {
            try {
                RemoteFetchConfigurationClient.deleteRemoteFetchComponent(id);
                CarbonUIMessage.sendCarbonUIMessage("Configuration successfully deleted!", CarbonUIMessage.INFO, request);
            } catch (RemoteFetchCoreException e) {
                CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
            }
        }
    }

%>


<script>
    location.href = 'list-remotefetch-configs.jsp';
</script>