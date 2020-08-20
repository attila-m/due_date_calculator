package com.myapp.demo;

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

    private static final LocalDateTime SUBMISSION_DATE = LocalDateTime.of(2020,
            Month.AUGUST,
            20,
            9,
            0);
    private static final Duration TURNAROUND_TIME = Duration.ofHours(7);

    @Test
    public void ShouldReturnSubmissionDatePlusTurnaroundTime() {
        LocalDateTime resolveDate = issueTrackingSystemService.CalculateDueDate(SUBMISSION_DATE, TURNAROUND_TIME);
        LocalDateTime expectedResolveDate = LocalDateTime.of(2020, Month.AUGUST, 20, 16, 0);

        assertEquals(resolveDate, expectedResolveDate);
    }

}
