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
<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.dto.RemoteFetchConfigurationRowDTO" %>
<%@ page import="java.util.List" %>

<%@ page import="java.util.Map" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.impl.handlers.repository.GitRepositoryManager" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager" %>
<%@ page import="java.io.File" %>
<%@ page import="org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>

<%
    List<RemoteFetchConfigurationRowDTO> configurations = RemoteFetchConfigurationClient.getConfigurations();
    RemoteFetchConfiguration fetchConfiguration = null;
    Map<String, String> repoAttributes;
    RepositoryManager repo;
%>
<carbon:breadcrumb
        label="remotefetch.core" resourceBundle="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources"
        topPage="true" request="<%=request%>"
/>

<jsp:include page="../dialog/display_messages.jsp"/>

<fmt:bundle basename="org.wso2.carbon.identity.remotefetch.core.ui.i18n.Resources">
    
    <link rel="stylesheet" href="css/remotefetchcore.css">
    
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
                                <th class="leftCol-med"><fmt:message key="field.config.remoteFetchName"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.name"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.status"/></th>
                                <th class="leftCol-med"><fmt:message key="field.config.action"/></th>
                            </tr>
                            </thead>
                            
                            <%if (configurations.size() > 0) { %>
                            <tbody>
                            <%
                                for (RemoteFetchConfigurationRowDTO configuration : configurations) { %>
                            <%
                                try {
                                    fetchConfiguration =
                                            RemoteFetchConfigurationClient.getRemoteFetchConfiguration(configuration.getId());
                                } catch (RemoteFetchCoreException e) {
                                    CarbonUIMessage.sendCarbonUIMessage("Invalid config for id", CarbonUIMessage.ERROR, request, e);
                                }
                                
                                repoAttributes = fetchConfiguration.getRepositoryManagerAttributes();
                                
                                repo = new
                                        GitRepositoryManager("repo-" + fetchConfiguration.getRemoteFetchConfigurationId(),
                                        repoAttributes.get("uri"),
                                        repoAttributes.get("branch"),
                                        new File(repoAttributes.get("directory")),
                                        new File("/tmp"),
                                        new UsernamePasswordCredentialsProvider(repoAttributes.get("userName"), repoAttributes.get("accessToken")));
                            %>
                            
                            <tr>
                                <td class="text-center">
                                    <input type="checkbox" disabled <%=configuration.getIsEnabled() ? "checked" : "" %>>
                                </td>
                                <td><%=configuration.getRemoteFetchName()%>
                                </td>
                                <td><%=configuration.getConfigurationDeployerType() %> Deployer</td>
                                <td class="text-center">
                                    
                                    <% if (configuration.getLastDeployed() == null) { %>
                                    <p>No Prior Deployments</p>
                                    
                                    <% } else { %>
                                    
                                    <% if (configuration.getSuccessfulDeployments() != 0) { %>
                                    <p class="remote-fetch-passed">
                                        Deployed : <%=configuration.getSuccessfulDeployments()%>
                                    </p>
                                    <p class="text-center"><b>Last Deployment: </b><fmt:formatDate type="both"
                                                                                                   dateStyle="long"
                                                                                                   timeStyle="short"
                                                                                                   value="<%=configuration.getLastDeployed()%>"/>
                                    </p>
                                    <p class="text-center"><b>Commit
                                        Hash: </b> <%= repo.getRevisionHash(new File(repoAttributes.get("directory")))%>
                                    </p>
                                    <% } %>
                                    
                                    <% if (configuration.getFailedDeployments() != 0) { %>
                                    <p class="remote-fetch-failed">
                                        Deployments Failed : <%=configuration.getFailedDeployments()%>
                                    </p>
                                    <p class="text-center"><b>Last Deployment: </b><fmt:formatDate type="both"
                                                                                                   dateStyle="long"
                                                                                                   timeStyle="short"
                                                                                                   value="<%=configuration.getLastDeployed()%>"/>
                                    </p>
                                    <% } %>
                                    <% } %>
                                </td>
                                <td style="width: 100px; white-space: normal;">
                                    <a title="Edit Configuration"
                                       href="add-remotefetch-config.jsp?id=<e:forUri value="<%=configuration.getId()%>"/>"
                                       class="icon-link"
                                       style="background-image: url(images/edit.gif)"> Edit
                                    </a>
                                    <a title="Delete Configuration"
                                       href="#"
                                       data-id="<%=configuration.getId()%>"
                                       class="icon-link delete-config-link"
                                       style="background-image: url(images/delete.gif)">Delete
                                    </a>
                                    <button title="Trigger Configuration"
                                            id="trigger-now"
                                            data-id="<%=configuration.getId()%>"
                                            onclick="trigger('<%=configuration.getId()%>')">Trigger Now
                                    </button>
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
                })
            });
        });
        function  trigger(id){
            window.location.href ="trigger-now.jsp?id=" + id;
        }
    </script>
    <script src="js/main.js"></script>
</fmt:bundle>

