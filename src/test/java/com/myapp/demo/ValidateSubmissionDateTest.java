package com.myapp.demo;

import com.myapp.demo.exception.CalculateDueDateException;
import com.myapp.demo.service.IssueTrackingSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidateSubmissionDateTest {

    @Autowired
    IssueTrackingSystemService issueTrackingSystemService;

    private static final LocalDateTime SUBMISSION_DATE_INSIDE_WORKING_HOURS = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 59);
    private static final LocalDateTime SUBMISSION_DATE_OUTSIDE_WORKING_HOURS = LocalDateTime.of(2020, Month.AUGUST, 20, 17, 0);
    private static final LocalDateTime SUBMISSION_DATE_OUTSIDE_WORKING_DAYS = LocalDateTime.of(2020, Month.AUGUST, 22, 13, 0);

    @Test
    public void ShouldNotThrowExceptionWhenSubmissionDateIsOnAWorkday() throws CalculateDueDateException {
        issueTrackingSystemService.validateSubmissionDate(SUBMISSION_DATE_INSIDE_WORKING_HOURS);
    }

    @Test
    public void ShouldThrowExceptionWhenSubmissionDateIsOutsideOfWorkHours() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.validateSubmissionDate(SUBMISSION_DATE_OUTSIDE_WORKING_HOURS));
    }

    @Test
    public void ShouldThrowExceptionWhenSubmissionDateIsOnWeekend() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.validateSubmissionDate(SUBMISSION_DATE_OUTSIDE_WORKING_DAYS));
    }


}
