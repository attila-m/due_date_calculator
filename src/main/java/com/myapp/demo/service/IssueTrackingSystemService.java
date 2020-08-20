package com.myapp.demo.service;

import com.myapp.demo.exception.CalculateDueDateException;
import com.myapp.demo.configuration.ImmutableConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class IssueTrackingSystemService {

    private ImmutableConfiguration configuration;
    IssueTrackingSystemService(@Autowired ImmutableConfiguration configuration) {
        this.configuration = configuration;
    }

    private static final Logger LOGGER = LogManager.getLogger(IssueTrackingSystemService.class);

    public LocalDateTime CalculateDueDate(LocalDateTime submissionDate, Duration turnaroundTime) throws CalculateDueDateException{

        LocalDateTime resolveDate = submissionDate;

        validateSubmissionDate(submissionDate);
        validateTurnaroundTime(turnaroundTime);

        resolveDate = resolveDate
                .plusDays(calculateWorkdays(submissionDate.getDayOfWeek(), turnaroundTime))
                .plusHours(turnaroundTime.toHours() % configuration.getWorkHours());

        return resolveDate;
    }

    public long calculateWorkdays(DayOfWeek dayOfWeek, Duration turnaroundTime) {
        long fullDays = turnaroundTime.toHours() / configuration.getWorkHours();

        DayOfWeek currentDay = dayOfWeek;
        long fullWorkDays = 0;

        for (int i = 0; i < fullDays; i++) {
            if (!isWeekendDay(currentDay)) {
                fullWorkDays++;
            }
            currentDay.plus(1);
        }
        return fullWorkDays;
    }

    public void validateSubmissionDate(LocalDateTime submissionDate) throws CalculateDueDateException {
        if(!isDuringWorkingHours(submissionDate)) {
            String errorMessage = submissionDate + " is not during working hours. Working hours are between 9AM to 5PM, from Monday to Friday.";
            LOGGER.error(errorMessage);
            throw new CalculateDueDateException(errorMessage);
        }
    }

    public void validateTurnaroundTime(Duration turnaroundTime) throws CalculateDueDateException {
        if(!isTurnaroundTimeGreaterThanZero(turnaroundTime)) {
            String errorMessage = turnaroundTime + " is not a valid value. Turnaround time should be more than zero.";
            LOGGER.error(errorMessage);
            throw new CalculateDueDateException(errorMessage);
        }
    }

    private boolean isTurnaroundTimeGreaterThanZero(Duration turnaroundTime) {
        if (turnaroundTime.isNegative() || turnaroundTime.isZero()) {
            return false;
        }
        return true;
    }

    private boolean isDuringWorkingHours(LocalDateTime date) {

        if (isWeekendDay(date.getDayOfWeek())) {
            return false;
        }

        if (date.getHour() < configuration.getWorkStartHour() || date.getHour() > configuration.getWorkEndHour() - 1) {
            return false;
        }

        return true;
    }

    private boolean isWeekendDay(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
