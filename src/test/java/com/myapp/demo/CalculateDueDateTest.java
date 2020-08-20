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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CalculateDueDate {

    @Autowired
    IssueTrackingSystemService issueTrackingSystemService;

    @Value("${admin.configuration.workHours}")
    private long workHours;

    private static final LocalDateTime SUBMISSION_DATE = LocalDateTime.of(2020, Month.AUGUST, 20, 9, 0);

    private static final Duration POSITIVE_TURNAROUND = Duration.ofHours(7);
    private static final Duration NEGATIVE_TURNAROUND = Duration.ofHours(-7);
    private static final Duration ZERO_TURNAROUND = Duration.ofHours(0);

    private static final LocalDateTime SUBMISSION_DATE_INSIDE_WORKING_HOURS = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 59);
    private static final LocalDateTime SUBMISSION_DATE_OUTSIDE_WORKING_HOURS = LocalDateTime.of(2020, Month.AUGUST, 20, 17, 0);
    private static final LocalDateTime SUBMISSION_DATE_OUTSIDE_WORKING_DAYS = LocalDateTime.of(2020, Month.AUGUST, 22, 13, 0);

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

    @Test
    public void ShouldNotThrowExceptionWhenTurnaroundTimeIsPositive() throws CalculateDueDateException {
        issueTrackingSystemService.validateTurnaroundTime(POSITIVE_TURNAROUND);
    }

    @Test
    public void ShouldThrowExceptionWhenTurnaroundTimeIsNegativeOrZero() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, Duration.ZERO));
        assertThrows(Exception.class, () -> issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, Duration.ofHours(-5)));
    }

    @Test
    public void ShouldNotThrowExceptionWhenSubmissionDateIsOnAWorkday() throws CalculateDueDateException {
        issueTrackingSystemService.validateSubmissionDate(SUBMISSION_DATE_INSIDE_WORKING_HOURS);
    }

    @Test
    public void ShouldThrowExceptionWhenSubmissionDateIsOutsideOfWorkHours() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE_OUTSIDE_WORKING_HOURS, POSITIVE_TURNAROUND));
    }

    @Test
    public void ShouldThrowExceptionWhenSubmissionDateIsOnWeekend() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE_OUTSIDE_WORKING_DAYS, POSITIVE_TURNAROUND));
    }

    @Test
    public void ShouldThrowExceptionWhenSubmissionDateIsNull() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.CalculateDueDate(null, POSITIVE_TURNAROUND));
    }

}