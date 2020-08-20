package com.myapp.demo.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class IssueTrackingSystemService {

    public LocalDateTime CalculateDueDate(LocalDateTime submissionDate, Duration turnaroundTime) {
        return submissionDate.plus(turnaroundTime);
    }

}
