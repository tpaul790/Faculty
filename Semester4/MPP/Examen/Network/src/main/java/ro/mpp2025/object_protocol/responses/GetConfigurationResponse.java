package ro.mpp2025.object_protocol.responses;

import ro.mpp2025.Configuration;

public class GetConfigurationResponse implements Response {
    private Configuration configuration;
    public GetConfigurationResponse(Configuration configuration) {
        this.configuration = configuration;
    }
    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
