package com.surendra.oauth.server.scheduler;

import com.surendra.oauth.server.repository.Oauth2AuthorizationRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This is a cron job scheduler class to perform the expired token clean up activity
 * based on the configurable date.
 */
@Component
public class CronJobScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(CronJobScheduler.class);
    @Autowired
    private Oauth2AuthorizationRepository authorizationRepository;

    @Value("${token.retention.period.days}")
    private int tokenRetentionPeriodDays;

    /**
     * Scheduler method used to clean up expire tokens from oauth management server.
     */
    @Scheduled(cron = "${token.cleanup.cron.expression}")
    public void scheduleTaskUsingCronExpression() {
        Date deleteBeforeDate = DateUtils.addDays(new Date(),-tokenRetentionPeriodDays);
        log.info("Scheduler job started to delete token before date {}", deleteBeforeDate);
        int count = authorizationRepository.deleteByAccessTokenExpiresAtBefore(deleteBeforeDate);
        log.info("Scheduler job completed for date: {} and total deleted records: {}", deleteBeforeDate, count);
    }
}
