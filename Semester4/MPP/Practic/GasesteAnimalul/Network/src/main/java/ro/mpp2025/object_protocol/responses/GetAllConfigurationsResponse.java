package ro.mpp2025.object_protocol.responses;

import ro.mpp2025.Configuration;

public class GetAllConfigurationsResponse implements Response {
    private Configuration[] configurations;
    public GetAllConfigurationsResponse(Configuration[] configurations) {
        this.configurations = configurations;
    }
    public Configuration[] getConfigurations() {
        return configurations;
    }
}
