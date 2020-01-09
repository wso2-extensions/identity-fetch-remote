<%@ page import="org.wso2.carbon.identity.remotefetch.core.ui.client.RemoteFetchConfigurationClient" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.implementations.actionHandlers.PollingActionListener" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.repomanager.RepositoryManager" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.core.implementations.repositoryHandlers.GitRepositoryManager" %>
<%@ page import="java.io.File" %>
<%@ page import="org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider" %>
<%@ page
        import="org.wso2.carbon.identity.remotefetch.core.implementations.configDeployers.VelocityTemplatedSPDeployer" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.configdeployer.ConfigDeployer" %>
<%@ page import="org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>

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
    int id = Integer.parseInt(request.getParameter("id"));
    RemoteFetchConfiguration fetchConfiguration = null;
    Map<String, String> repoAttributes;
    
    try {
        fetchConfiguration = RemoteFetchConfigurationClient.getRemoteFetchConfiguration(id);
    } catch (RemoteFetchCoreException e) {
        CarbonUIMessage.sendCarbonUIMessage("Invalid Config for id", CarbonUIMessage.ERROR, request, e);
    }
    
    repoAttributes = fetchConfiguration.getRepositoryManagerAttributes();
    
    RepositoryManager repositoryManager = new
            GitRepositoryManager("repo-" + id,
            repoAttributes.get("uri"),
            repoAttributes.get("branch"),
            new File(repoAttributes.get("directory")),
            new File("/tmp"),
            new UsernamePasswordCredentialsProvider(repoAttributes.get("userName"), repoAttributes.get("accessToken")));
    
    ConfigDeployer configDeployer = new
            VelocityTemplatedSPDeployer(fetchConfiguration.getTenantId(),
            fetchConfiguration.getUserName(),
            fetchConfiguration.getRemoteFetchConfigurationId());
    
    PollingActionListener pollingActionListener = new PollingActionListener(repositoryManager, configDeployer, 0,
            fetchConfiguration.getRemoteFetchConfigurationId(), fetchConfiguration.getTenantId(), fetchConfiguration.getUserName());
    
    pollingActionListener.iteration();

%>

<script>
    location.href = 'list-remotefetch-configs.jsp';
</script>