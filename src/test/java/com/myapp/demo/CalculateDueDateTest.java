package com.myapp.demo;

import com.myapp.demo.exception.CalculateDueDateException;
import com.myapp.demo.service.IssueTrackingSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CalculateDueDate {

    @Autowired
    IssueTrackingSystemService issueTrackingSystemService;

    @Value("${admin.configuration.workHours}")
    private long workHours;

    private static final LocalDateTime SUBMISSION_DATE = LocalDateTime.of(2020, Month.AUGUST, 20, 9, 0);

    @Test
    public void ShouldReturnSubmissionDatePlusTurnaroundTime() throws CalculateDueDateException {
        Duration turnaroundTime = Duration.ofHours(workHours - 1);

        LocalDateTime resolveDate = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnaroundTime);
        LocalDateTime expectedResolveDate = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 0);

        assertEquals(resolveDate, expectedResolveDate);
    }

    @Test
    public void ShouldReturnCalculatedWorkdayDayWhenTurnaroundTimeIsWholeWorkday() throws CalculateDueDateException {
        Duration turnAroundTimeFullWorkDay = Duration.ofHours(workHours);
        Duration turnAroundTimeTwoFullWorkDays = Duration.ofHours(workHours * 2);

        LocalDateTime resolveDateWithOneDay = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTimeFullWorkDay);
        LocalDateTime expectedResolveDateWithOneDay = LocalDateTime.of(2020, Month.AUGUST, 21, 9, 0);
        assertEquals(resolveDateWithOneDay, expectedResolveDateWithOneDay);

        LocalDateTime resolveDateWithTwoDays = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTimeTwoFullWorkDays);
        LocalDateTime expectedResolveDateWithTwoDays = LocalDateTime.of(2020, Month.AUGUST, 22, 9, 0);
        assertEquals(resolveDateWithTwoDays, expectedResolveDateWithTwoDays);
    }

}
