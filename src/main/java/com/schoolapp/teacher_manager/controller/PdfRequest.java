package com.schoolapp.teacher_manager.controller;

public class PdfRequest {
    private String reportType;
    private Long teacherId;

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
