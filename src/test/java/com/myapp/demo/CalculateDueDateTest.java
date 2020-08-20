package com.myapp.demo;

import com.myapp.demo.exception.CalculateDueDateException;
import com.myapp.demo.service.IssueTrackingSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CalculateDueDate {

    @Autowired
    IssueTrackingSystemService issueTrackingSystemService;

    private static final LocalDateTime SUBMISSION_DATE = LocalDateTime.of(2020, Month.AUGUST, 20, 9, 0);
    private static final Duration TURNAROUND_TIME = Duration.ofHours(7);
    private static final Duration TURNAROUND_TIME_FULL_WORKDAY = Duration.ofHours(8);
    private static final Duration TURNAROUND_TIME_TWO_FULL_WORKDAY = Duration.ofHours(16);

    @Test
    public void ShouldReturnSubmissionDatePlusTurnaroundTime() throws CalculateDueDateException {

        LocalDateTime resolveDate = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, TURNAROUND_TIME);
        LocalDateTime expectedResolveDate = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 0);

        assertEquals(resolveDate, expectedResolveDate);
    }

    @Test
    public void ShouldReturnCalculatedWorkdayDayWhenTurnaroundTimeIsWholeWorkday() throws CalculateDueDateException {

        LocalDateTime resolveDateWithOneDay = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, TURNAROUND_TIME_FULL_WORKDAY);
        LocalDateTime expectedResolveDateWithOneDay = LocalDateTime.of(2020, Month.AUGUST, 21, 9, 0);
        assertEquals(resolveDateWithOneDay, expectedResolveDateWithOneDay);

        LocalDateTime resolveDateWithTwoDays = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, TURNAROUND_TIME_TWO_FULL_WORKDAY);
        LocalDateTime expectedResolveDateWithTwoDays = LocalDateTime.of(2020, Month.AUGUST, 22, 9, 0);
        assertEquals(resolveDateWithTwoDays, expectedResolveDateWithTwoDays);
    }

}
