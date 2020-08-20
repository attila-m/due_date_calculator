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

    public static final long WORK_STARTS_AT = 9;
    public static final long WORK_ENDS_AT = 17;
    public static final long WORK_HOURS = 8;

    private static final Logger LOGGER = LogManager.getLogger(IssueTrackingSystemService.class);

    public LocalDateTime CalculateDueDate(LocalDateTime submissionDate, Duration turnaroundTime) throws CalculateDueDateException{

        LocalDateTime resolveDate = submissionDate;

        validateSubmissionDate(submissionDate);
        validateTurnaroundTime(turnaroundTime);

        resolveDate = resolveDate
                .plusDays(calculateWorkdays(submissionDate.getDayOfWeek(), turnaroundTime))
                .plusHours(turnaroundTime.toHours() % 8);

        return resolveDate;
    }

    public long calculateWorkdays(DayOfWeek dayOfWeek, Duration turnaroundTime) {
        long fullDays = turnaroundTime.toHours() / WORK_HOURS;

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

        if (date.getHour() < WORK_STARTS_AT || date.getHour() > WORK_ENDS_AT - 1) {
            return false;
        }

        return true;
    }

    private boolean isWeekendDay(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
