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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="carbon" uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.client.RemoteFetchConfigurationClient" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.dto.RemoteFetchConfigurationRowDTO" %>
<%@ page import="java.util.List" %>


<%
    List<RemoteFetchConfigurationRowDTO> configurations = RemoteFetchConfigurationClient.getConfigurations();

%>
<jsp:include page="../dialog/display_messages.jsp"/>

<fmt:bundle basename="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources">
    
    <link rel="stylesheet" href="css/remotefetchcore.css">
    
    <carbon:breadcrumb
            label="remotefetch.core" resourceBundle="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources"
            topPage="true" request="<%=request%>"
    />
    
    <div id="middle">
        <h2><fmt:message key="remotefetch.core"/></h2>
        </br>
        <div id="workArea">
            <table style="width: 100%" class="styledLeft">
                <tbody>
                <tr>
                    <td style="border:none !important">
                        <table class="styledLeft" width="100%" id="RemoteFetchConfigurations">
                            <thead>
                            <tr>
                                <th class="leftCol-med enabled-cell"><fmt:message key="field.config.enabled"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.name"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.repositoryType"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.actionListenerType"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.status"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.action"/></th>
                            </tr>
                            </thead>
                            
                            <%if (configurations.size() > 0) { %>
                            <tbody>
                            <%
                                for (RemoteFetchConfigurationRowDTO configuration : configurations) { %>
                            <tr>
                                <td class="text-center">
                                    <input type="checkbox" disabled <%=configuration.getIsEnabled() ? "checked" : "" %>>
                                </td>
                                <td><%=configuration.getConfigurationDeployerType() %> Deployer</td>
                                <td><%=configuration.getRepositoryType()%>
                                </td>
                                <td><%=configuration.getActionListenerType()%>
                                </td>
                                <td class="text-center">
                                    <% if (configuration.getLastDeployed() == null) { %>
                                    <p>No Prior Deployments</p>
                                    <% } else { %>
                                    <% if (configuration.getSuccessfulDeployments() != 0) { %>
                                    <p class="remote-fetch-passed"
                                       title="<fmt:formatDate type = "both" dateStyle = "long" timeStyle = "short" value = "<%=configuration.getLastDeployed()%>"/>">
                                        Deployed : <%=configuration.getSuccessfulDeployments()%>
                                    </p>
                                    <% } %>
                                    <% if (configuration.getFailedDeployments() != 0) { %>
                                    <p class="remote-fetch-failed"
                                       title="<fmt:formatDate type = "both" dateStyle = "long" timeStyle = "short" value="<%=configuration.getLastDeployed()%>"/>">
                                        Deployments Failed : <%=configuration.getFailedDeployments()%>
                                    </p>
                                    <% } %>
                                    <% } %>
                                </td>
                                
                                <td style="width: 100px; white-space: nowrap;">
                                    <a title="Edit Configuration"
                                       href="add-remotefetch-config.jsp?id=<e:forUri value="<%=Integer.toString(configuration.getId())%>"/>"
                                       class="icon-link"
                                       style="background-image: url(images/edit.gif)"> Edit
                                    </a>
                                    <a title="Delete Configuration"
                                       href="#"
                                       data-id="<%=configuration.getId()%>"
                                       class="icon-link delete-config-link"
                                       style="background-image: url(images/delete.gif)">Delete
                                    </a>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                            <% } else { %>
                            <tbody>
                            <tr>
                                <td colspan="6"><i>No Service Providers Registered</i></td>
                            </tr>
                            </tbody>
                            <% } %>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".delete-config-link").click(function () {
                var id = this.getAttribute("data-id");
                CARBON.showConfirmationDialog("Delete Selected config?", function () {
                    window.location.href = "delete-remotefetch-config.jsp?id=" + id;
                });
            })
        });
    </script>
</fmt:bundle>
