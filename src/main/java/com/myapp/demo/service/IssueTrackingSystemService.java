package com.myapp.demo.service;

import com.myapp.demo.exception.CalculateDueDateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class IssueTrackingSystemService {

    private static final Logger LOGGER = LogManager.getLogger(IssueTrackingSystemService.class);

    public LocalDateTime CalculateDueDate(LocalDateTime submissionDate, Duration turnaroundTime) throws CalculateDueDateException{
        validateSubmissionDate(submissionDate);
        return submissionDate.plus(turnaroundTime);
    }

    public void validateSubmissionDate(LocalDateTime submissionDate) throws CalculateDueDateException {
        if(!isDuringWorkingHours(submissionDate)) {
            String errorMessage = submissionDate + " is not a workday.";
            LOGGER.error(errorMessage);
            throw new CalculateDueDateException(errorMessage);
        }
    }

    public boolean isDuringWorkingHours(LocalDateTime date) {

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ) {
            return false;
        }

        if (date.getHour() < 9 || date.getHour() > 16) {
            return false;
        }

        return true;
    }

}
