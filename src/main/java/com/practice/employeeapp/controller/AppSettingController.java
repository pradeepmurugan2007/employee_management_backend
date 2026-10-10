package com.practice.employeeapp.controller;

import com.practice.employeeapp.models.AppSetting;
import com.practice.employeeapp.repository.AppSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(originPatterns = "*")
public class AppSettingController {

    @Autowired
    private AppSettingRepository repository;

    @GetMapping
    public AppSetting getSettings() {
        return repository.findById(1L).orElseGet(() -> {
            AppSetting defaultSettings = new AppSetting();
            defaultSettings.setId(1L);
            defaultSettings.setCompanyName("EmpLy");
            defaultSettings.setEmailAddress("info@emply.com");
            defaultSettings.setPhoneNumber("9876543210");
            defaultSettings.setAddress("123, Business Park,\nCoimbatore, Tamil\nNadu - 641001");
            defaultSettings.setWebsite("https://www.emply.com");
            defaultSettings.setDateFormat("dd/mm/yyyy");
            defaultSettings.setTimeFormat("12 Hour (AM/PM)");
            defaultSettings.setLanguage("English");
            defaultSettings.setItemsPerPage("10");
            defaultSettings.setEnableDarkMode(false);
            defaultSettings.setEnableEmailNotifications(true);
            defaultSettings.setMinPasswordLength(8);
            defaultSettings.setRequireUppercase(true);
            defaultSettings.setRequireLowercase(true);
            defaultSettings.setRequireNumber(true);
            defaultSettings.setRequireSpecialChar(false);
            defaultSettings.setSessionTimeout(30);
            defaultSettings.setAutoLogout(true);
            defaultSettings.setAllowMultipleSessions(false);
            defaultSettings.setRememberMeDuration(7);
            return repository.save(defaultSettings);
        });
    }

    @PutMapping
    public AppSetting updateSettings(@RequestBody AppSetting settings) {
        settings.setId(1L);
        return repository.save(settings);
    }
}
