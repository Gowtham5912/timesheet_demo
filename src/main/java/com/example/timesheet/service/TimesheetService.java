package com.example.timesheet.service;

import com.example.timesheet.entity.AuditLog;
import com.example.timesheet.entity.Timesheet;
import com.example.timesheet.repository.AuditLogRepository;
import com.example.timesheet.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimesheetService {

    private final TimesheetRepository repository;
    private final AuditLogRepository auditLogRepository;

    public TimesheetService(TimesheetRepository repository, AuditLogRepository auditLogRepository) {
        this.repository = repository;
        this.auditLogRepository = auditLogRepository;
    }

    // Save Timesheet
    public Timesheet saveTimesheet(Timesheet timesheet, String actor) {
        boolean isNew = timesheet.getId() == null || !repository.existsById(timesheet.getId());
        timesheet.setStatus("PENDING");
        Timesheet saved = repository.save(timesheet);

        String action = isNew ? "Created timesheet" : "Updated timesheet";
        String details = String.format("%s for %s (ID: %s)", action, saved.getEmployeeName(), saved.getId());
        auditLogRepository.save(new AuditLog(null, actor, action, details, saved.getId(), LocalDateTime.now()));

        return saved;
    }

    // Get All
    public List<Timesheet> getAllTimesheets() {
        return repository.findAll();
    }

    // Get by Employee
    public List<Timesheet> getTimesheetsByEmployee(String employeeName) {
        return repository.findByEmployeeName(employeeName);
    }

    // Get recent audit logs
    public List<AuditLog> getRecentAuditLogs() {
        return auditLogRepository.findTop10ByOrderByTimestampDesc();
    }

    // Update Status
    public void updateStatus(Long id, String status, String actor) {
        repository.findById(id).ifPresent(timesheet -> {
            timesheet.setStatus(status);
            Timesheet saved = repository.save(timesheet);

            String action = "APPROVED".equals(status) ? "Approved timesheet" : "REJECTED".equals(status) ? "Rejected timesheet" : "Updated timesheet status";
            String details = String.format("%s for %s (ID: %s)", action, saved.getEmployeeName(), saved.getId());
            auditLogRepository.save(new AuditLog(null, actor, action, details, saved.getId(), LocalDateTime.now()));
        });
    }

    // Delete
    public void deleteTimesheet(Long id, String actor) {
        repository.findById(id).ifPresent(timesheet -> {
            String details = String.format("Deleted timesheet for %s (ID: %s)", timesheet.getEmployeeName(), id);
            auditLogRepository.save(new AuditLog(null, actor, "Deleted timesheet", details, id, LocalDateTime.now()));
        });
        repository.deleteById(id);
    }
}