package shane.testing.util;

import io.cucumber.java.Status;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;


public class TestReportUtils {
    private static final Instant startTime = Instant.now();
    @Getter
    private static int passed = 0;
    @Getter
    private static int failed = 0;
    @Getter
    private static int skipped = 0;

    private TestReportUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void updateSummaryResult(Status status) {
        switch (status) {
            case PASSED:
                passed++;
                break;
            case FAILED:
                failed++;
                break;
            default:
                skipped++;
                break;
        }
    }

    public static String getTestExecutionDuration() {
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        return String.format("Execution duration: %d hours %d minutes %d seconds", hours, minutes, seconds);
    }
}
