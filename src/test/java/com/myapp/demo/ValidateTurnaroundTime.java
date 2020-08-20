package com.myapp.demo;

import com.myapp.demo.exception.CalculateDueDateException;
import com.myapp.demo.service.IssueTrackingSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ValidateTurnaroundTime {

    @Autowired
    IssueTrackingSystemService issueTrackingSystemService;

    private static final Duration POSITIVE_TURNAROUND = Duration.ofHours(7);
    private static final Duration NEGATIVE_TURNAROUND = Duration.ofHours(-7);
    private static final Duration ZERO_TURNAROUND = Duration.ofHours(0);

    @Test
    public void ShouldNotThrowExceptionWhenTurnaroundTimeIsPositive() throws CalculateDueDateException {
        issueTrackingSystemService.validateTurnaroundTime(POSITIVE_TURNAROUND);
    }

    @Test
    public void ShouldThrowExceptionWhenTurnaroundTimeIsNegativeOrZero() {
        assertThrows(Exception.class, () -> issueTrackingSystemService.validateTurnaroundTime(NEGATIVE_TURNAROUND));
        assertThrows(Exception.class, () -> issueTrackingSystemService.validateTurnaroundTime(ZERO_TURNAROUND));
    }

}
