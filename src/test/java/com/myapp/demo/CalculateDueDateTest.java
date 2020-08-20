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

        LocalDateTime expectedResolveDate = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 0);
        LocalDateTime resolveDate = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnaroundTime);
        assertEquals(expectedResolveDate, resolveDate);
    }

    @Test
    public void ShouldReturnCalculatedWorkdayDayWhenTurnaroundTimeIsWholeWorkday() throws CalculateDueDateException {
        Duration turnAroundTimeFullWorkDay = Duration.ofHours(workHours);
        Duration turnAroundTimeTwoFullWorkDays = Duration.ofHours(workHours * 2);

        LocalDateTime expectedDueDateWithOneDay = LocalDateTime.of(2020, Month.AUGUST, 21, 9, 0);
        LocalDateTime actualDueDateWithOneDay = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTimeFullWorkDay);
        assertEquals(expectedDueDateWithOneDay, actualDueDateWithOneDay);

        LocalDateTime expectedDueDateWithTwoDays = LocalDateTime.of(2020, Month.AUGUST, 24, 9, 0);
        LocalDateTime actualDueDateWithTwoDays = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTimeTwoFullWorkDays);
        assertEquals(expectedDueDateWithTwoDays, actualDueDateWithTwoDays);
    }

    @Test
    public void ShouldReturnCalculatedWorkdayDayWhenTurnaroundTimeIsNotWholeWorkday() throws CalculateDueDateException {
        Duration turnAroundTime1 = Duration.ofHours(9);
        Duration turnAroundTime2 = Duration.ofHours(31);

        LocalDateTime expectedDueDate1 = LocalDateTime.of(2020, Month.AUGUST, 21, 10, 0);
        LocalDateTime actualDueDate1 = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTime1);
        assertEquals(expectedDueDate1, actualDueDate1);

        LocalDateTime expectedDueDate2 = LocalDateTime.of(2020, Month.AUGUST, 25, 16, 0);
        LocalDateTime actualDueDate2 = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, turnAroundTime2);
        assertEquals(expectedDueDate2, actualDueDate2);
    }

}
