package com.schoolapp.teacher_manager.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    private String lastName;
    private String originalLastName; // for women
    private String firstName;
    private String maritalStatus;
    private Integer childrenCount;

    private LocalDate birthDate;
    private String birthPlace;
    // Contact Info
    private String address;
    private String phoneNumber;
    private String email;

    // Work Info
    private String rank;
    private String status; // Permanent, Probation, Contract
    private String currentInstitution;
    private String previousInstitution;
    private LocalDate resumptionDate;
    private String appointmentDecisionNumber;
    private LocalDate appointmentDecisionDate;
    private LocalDate appointmentDate;
    private String appointmentCode;
    // Official Numbers
    private String postalAccountNumber;
    private String postalAccountKey;
    private String socialSecurityNumber;

    public Teacher() {}

    // Getters & Setters (you can use Lombok @Data if preferred)
    // ... [all getters and setters here]
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getOriginalLastName() { return originalLastName; }
    public void setOriginalLastName(String originalLastName) { this.originalLastName = originalLastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public Integer getChildrenCount() { return childrenCount; }
    public void setChildrenCount(Integer childrenCount) { this.childrenCount = childrenCount; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrentInstitution() { return currentInstitution; }
    public void setCurrentInstitution(String currentInstitution) { this.currentInstitution = currentInstitution; }

    public String getPreviousInstitution() { return previousInstitution; }
    public void setPreviousInstitution(String previousInstitution) { this.previousInstitution = previousInstitution; }

    public LocalDate getResumptionDate() { return resumptionDate; }
    public void setResumptionDate(LocalDate resumptionDate) { this.resumptionDate = resumptionDate; }

    public String getAppointmentDecisionNumber() { return appointmentDecisionNumber; }
    public void setAppointmentDecisionNumber(String appointmentDecisionNumber) { this.appointmentDecisionNumber = appointmentDecisionNumber; }

    public LocalDate getAppointmentDecisionDate() { return appointmentDecisionDate; }
    public void setAppointmentDecisionDate(LocalDate appointmentDecisionDate) { this.appointmentDecisionDate = appointmentDecisionDate; }

    public String getPostalAccountNumber() { return postalAccountNumber; }
    public void setPostalAccountNumber(String postalAccountNumber) { this.postalAccountNumber = postalAccountNumber; }

    public String getPostalAccountKey() { return postalAccountKey; }
    public void setPostalAccountKey(String postalAccountKey) { this.postalAccountKey = postalAccountKey; }

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate;}
    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getAppointmentCode() { return appointmentCode; }
    public void setAppointmentCode(String appointmentCode) { this.appointmentCode = appointmentCode;}
}
