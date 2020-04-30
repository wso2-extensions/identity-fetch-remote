/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.remotefetch.core.executers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.core.executers.tasks.RemoteFetchConfigurationBatchTask;
import org.wso2.carbon.identity.remotefetch.core.executers.tasks.RemoteFetchConfigurationImmediateTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The execution engine for Fetching the artifacts from remote repository and loads it to the system.
 * This execution engine has functions to start batch task and immediate task.
 * This execution engine starts batch task at component activation and schedule at fixed rate.
 * {@link RemoteFetchConfigurationBatchTask}
 * This execution engine starts immediate task when OSGi trigger service called.
 * {@link RemoteFetchConfigurationImmediateTask}
 *
 */
public class RemoteFetchTaskExecutor {

    private static final Log log = LogFactory.getLog(RemoteFetchTaskExecutor.class);

    private ScheduledExecutorService scheduler;
    private RemoteFetchConfigurationBatchTask remoteFetchConfigurationBatchTask;

    public RemoteFetchTaskExecutor() {

    }

    public void createScheduler() {

        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Shutdown scheduler while deactivating the component.
     */
    public void shutdownScheduler() {

        scheduler.shutdown();
    }

    /**
     * Schedule Batch execution after checking whether enabled or not.
     */
    public void startBatchTaskExecution() {

        remoteFetchConfigurationBatchTask = new RemoteFetchConfigurationBatchTask();
        try {
            scheduler.scheduleAtFixedRate(remoteFetchConfigurationBatchTask, 0, (60 * 1), TimeUnit.SECONDS);
            log.info("Batch Task is scheduled.");
        } catch (Exception e) {
            log.error("Error while scheduling batch task", e);
        }
    }

    /**
     * Schedule immediate task execution when OSGi trigger service called.
     * @param remoteFetchConfiguration
     * @throws RemoteFetchCoreException
     */
    public void startImmediateTaskExecution(RemoteFetchConfiguration remoteFetchConfiguration)
            throws RemoteFetchCoreException {

        RemoteFetchConfigurationImmediateTask remoteFetchConfigurationImmediateTask =
                new RemoteFetchConfigurationImmediateTask(remoteFetchConfiguration);

        try {
            scheduler.schedule(remoteFetchConfigurationImmediateTask, 0, TimeUnit.SECONDS);

            if (log.isDebugEnabled()) {
                log.debug("Immediate Task is scheduled for remote fetch configuration "
                        + remoteFetchConfiguration.getRemoteFetchConfigurationId());
            }
        } catch (Exception e) {
            throw new RemoteFetchCoreException("Error while scheduling immediate task for remote fetch configuration "
                    + remoteFetchConfiguration.getRemoteFetchConfigurationId(), e);
        }
    }

    /**
     * Remove entries from batch task when delete remote fetch configuration.
     * @param id
     */
    public void deleteRemoteFetchConfigurationFromBatchTask(String id) {

        remoteFetchConfigurationBatchTask.deleteRemoteFetchConfiguration(id);
    }
}
