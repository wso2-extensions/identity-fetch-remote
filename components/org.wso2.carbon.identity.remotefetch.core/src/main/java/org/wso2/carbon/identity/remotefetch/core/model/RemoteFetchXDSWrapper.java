package org.wso2.carbon.identity.remotefetch.core.model;

import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.xds.common.constant.XDSWrapper;

/**
 * Wrapper class for RemoteFetchConfiguration.
 */
public class RemoteFetchXDSWrapper implements XDSWrapper {

    private RemoteFetchConfiguration remoteFetchConfiguration;
    private String fetchConfigurationId;
    private String timestamp;

    public RemoteFetchXDSWrapper(RemoteFetchXDSWrapperBuilder builder) {
        this.remoteFetchConfiguration = builder.remoteFetchConfiguration;
        this.fetchConfigurationId = builder.fetchConfigurationId;
        this.timestamp = builder.timestamp;
    }

    public RemoteFetchConfiguration getRemoteFetchConfiguration() {
        return remoteFetchConfiguration;
    }

    public String getFetchConfigurationId() {
        return fetchConfigurationId;
    }

    /**
     * Builder class for RemoteFetchXDSWrapper.
     */
    public static class RemoteFetchXDSWrapperBuilder {

        private RemoteFetchConfiguration remoteFetchConfiguration;
        private String fetchConfigurationId;
        private String timestamp;

        public RemoteFetchXDSWrapperBuilder setRemoteFetchConfiguration(
                RemoteFetchConfiguration remoteFetchConfiguration) {
            this.remoteFetchConfiguration = remoteFetchConfiguration;
            return this;
        }

        public RemoteFetchXDSWrapperBuilder setFetchConfigurationId(String fetchConfigurationId) {
            this.fetchConfigurationId = fetchConfigurationId;
            return this;
        }

        public RemoteFetchXDSWrapper build() {

            this.timestamp = String.valueOf(System.currentTimeMillis());
            return new RemoteFetchXDSWrapper(this);
        }
    }

}
