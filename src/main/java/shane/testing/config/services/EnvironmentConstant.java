package shane.testing.config.services;

public class EnvironmentConstant {

    public static final String DEFAULT_CHARSET = "default.charset";
    public static final String API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String POOLING_RETRY_SET_UP_CACHE_DATA_COUNT = "default.pooling.retry.setup.cache.data.count";
    public static final String POOLING_TIMEOUT_MS = "default.pooling.timeout.ms";

    private EnvironmentConstant() {
        throw new IllegalStateException("Utility class");
    }

}
