package com.practice.employeeapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class AppSetting {
    @Id
    private Long id = 1L;

    private String companyName;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private String website;
    private String dateFormat;
    private String timeFormat;
    private String language;
    private String itemsPerPage;
    private Boolean enableDarkMode;
    private Boolean enableEmailNotifications;
    private Integer minPasswordLength;
    private Boolean requireUppercase;
    private Boolean requireLowercase;
    private Boolean requireNumber;
    private Boolean requireSpecialChar;
    private Integer sessionTimeout;
    private Boolean autoLogout;
    private Boolean allowMultipleSessions;
    private Integer rememberMeDuration;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String companyLogo;
}
