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

package org.wso2.carbon.identity.remotefetch.common.exceptions;

/**
 * Exception class for Remote Fetch Configuration client related exceptions.
 */
public class RemoteFetchClientException extends RemoteFetchCoreException {

    /**
     * Exception occurred while processing Remote Fetch Configuration client related exceptions.
     * @param message Error Message
     * @see org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ErrorMessage
     */
    public RemoteFetchClientException(String message) {

        super(message);
    }

    /**
     * Exception occurred while processing Remote Fetch Configuration client related exceptions.
     * @param message Error Message
     * @param cause Throwable cause
     * @see org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ErrorMessage
     */
    public RemoteFetchClientException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Exception occurred while processing Remote Fetch Configuration client related exceptions.
     * @param errorCode Error Code
     * @param message Throwable cause
     * @see org.wso2.carbon.identity.remotefetch.common.RemoteFetchConstants.ErrorMessage
     *
     */
    public RemoteFetchClientException(String errorCode, String message) {

        super(errorCode, message);
    }

    /**
     * Exception occurred while processing Remote Fetch Configuration client related exceptions.
     * @param errorCode Error Code
     * @param message Error Message
     * @param cause Throwable cause
     */
    public RemoteFetchClientException(String errorCode, String message, Throwable cause) {

        super(errorCode, message, cause);
    }
}
