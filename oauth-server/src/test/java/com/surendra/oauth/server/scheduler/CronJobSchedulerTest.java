package com.surendra.oauth.server.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CronJobSchedulerTest {

    @Autowired
    CronJobScheduler cronJobScheduler;

    /**
     * TestCase: Test for successful execution of scheduleTaskUsingCronExpression method
     * Action: void scheduleTaskUsingCronExpression method called
     * Expected: verified successful execution of scheduleTaskUsingCronExpression method
     */
    @Test
    public void testScheduleTaskUsingCronExpression_successfulExecution() {
        Assertions.assertDoesNotThrow(() -> {
            cronJobScheduler.scheduleTaskUsingCronExpression();
        });
    }
}
