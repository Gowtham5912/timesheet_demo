package com.example.timesheet.service;

import com.example.timesheet.entity.Timesheet;
import com.example.timesheet.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesheetService {

    private final TimesheetRepository repository;

    public TimesheetService(TimesheetRepository repository) {
        this.repository = repository;
    }

    // Save Timesheet
    public Timesheet saveTimesheet(Timesheet timesheet) {
        timesheet.setStatus("PENDING");
        return repository.save(timesheet);
    }

    // Get All
    public List<Timesheet> getAllTimesheets() {
        return repository.findAll();
    }

    // Get by Employee
    public List<Timesheet> getTimesheetsByEmployee(String employeeName) {
        return repository.findByEmployeeName(employeeName);
    }

    // Update Status
    public void updateStatus(Long id, String status) {
        repository.findById(id).ifPresent(timesheet -> {
            timesheet.setStatus(status);
            repository.save(timesheet);
        });
    }

    // Delete
    public void deleteTimesheet(Long id) {
        repository.deleteById(id);
    }
}