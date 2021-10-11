package shane.testing.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void waitForSync(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.info("Wait for syncing transaction progress");
        }
    }

}
