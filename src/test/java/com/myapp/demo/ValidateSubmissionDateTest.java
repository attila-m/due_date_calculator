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

    private static final LocalDateTime SUBMISSION_DATE = LocalDateTime.of(2020,
            Month.AUGUST,
            20,
            9,
            0);

    @Test
    public void ShouldNotThrowExceptionWhenSubmissionDateIsOnAWorkday() throws CalculateDueDateException {
        issueTrackingSystemService.validateSubmissionDate(SUBMISSION_DATE);
    }
}
