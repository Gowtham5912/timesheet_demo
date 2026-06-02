package com.example.timesheet.controller;

import com.example.timesheet.entity.Timesheet;
import com.example.timesheet.service.TimesheetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TimesheetController {

    private final TimesheetService service;

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    // Home Page
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "anonymous";
        boolean isManager = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));

        model.addAttribute("username", username);
        model.addAttribute("isManager", isManager);

        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeName(username);
        model.addAttribute("timesheet", timesheet);

        if (isManager) {
            model.addAttribute("timesheets", service.getAllTimesheets());
            
            // Add statistics for Manager Dashboard
            long totalPending = service.getAllTimesheets().stream().filter(t -> "PENDING".equals(t.getStatus())).count();
            long totalApproved = service.getAllTimesheets().stream().filter(t -> "APPROVED".equals(t.getStatus())).count();
            long totalRejected = service.getAllTimesheets().stream().filter(t -> "REJECTED".equals(t.getStatus())).count();
            model.addAttribute("totalPending", totalPending);
            model.addAttribute("totalApproved", totalApproved);
            model.addAttribute("totalRejected", totalRejected);
        } else {
            model.addAttribute("timesheets", service.getTimesheetsByEmployee(username));
        }

        return "index";
    }

    // Save Timesheet
    @PostMapping("/save")
    public String saveTimesheet(@ModelAttribute Timesheet timesheet, Authentication authentication) {
        if (authentication != null) {
            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
            // Employees should only submit timesheets under their own username
            if (!isManager) {
                timesheet.setEmployeeName(authentication.getName());
            }
        }
        service.saveTimesheet(timesheet);
        return "redirect:/";
    }

    // Approve
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        service.updateStatus(id, "APPROVED");
        return "redirect:/";
    }

    // Reject
    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        service.updateStatus(id, "REJECTED");
        return "redirect:/";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteTimesheet(id);
        return "redirect:/";
    }
}