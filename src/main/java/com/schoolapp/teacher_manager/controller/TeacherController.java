package com.schoolapp.teacher_manager.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.schoolapp.teacher_manager.model.Teacher;
import com.schoolapp.teacher_manager.service.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // ------------------- REST CRUD -------------------

    @GetMapping
    @ResponseBody
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PostMapping
    @ResponseBody
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return teacher;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        teacherService.saveTeacher(teacher);
        return teacher;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "Teacher with ID " + id + " deleted successfully.";
    }
@PostMapping("/bulk-delete")
public ResponseEntity<Void> bulkDelete(@RequestBody List<Long> ids) {
    teacherService.deleteAllById(ids);
    return ResponseEntity.ok().build();
}
    // ------------------- Excel Upload -------------------

    @PostMapping("/upload")
    @ResponseBody
    public String uploadTeachers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "❌ File is empty!";
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Teacher> teachers = new ArrayList<>();

            // Assume first row is header
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Teacher teacher = new Teacher();

                teacher.setLastName(getStringCellValue(row, 1));
                teacher.setOriginalLastName(getStringCellValue(row, 2));
                teacher.setFirstName(getStringCellValue(row, 3));
                teacher.setMaritalStatus(getStringCellValue(row, 6));
                teacher.setChildrenCount(getIntegerCellValue(row, 7));
                teacher.setAddress(getStringCellValue(row, 8));
                teacher.setPostalAccountNumber(getStringCellValue(row, 9));
                teacher.setPostalAccountKey(getStringCellValue(row, 10));
                teacher.setSocialSecurityNumber(getStringCellValue(row, 11));
                teacher.setPhoneNumber(getStringCellValue(row, 12));
                teacher.setEmail(getStringCellValue(row, 13));
                teacher.setRank(getStringCellValue(row, 14));
                teacher.setCurrentInstitution(getStringCellValue(row, 15));
                teacher.setResumptionDate(getLocalDateCellValue(row, 16));

                teachers.add(teacher);
            }

            // Save all teachers
            for (Teacher t : teachers) {
                teacherService.saveTeacher(t);
            }

            return "✅ Successfully uploaded " + teachers.size() + " teachers!";

        } catch (IOException e) {
            return "❌ Error reading file: " + e.getMessage();
        }
    }

    // ------------------- Helper Methods -------------------

    private String getStringCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        return cell != null ? cell.toString().trim() : null;
    }

    private Long getLongCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        return (cell != null && cell.getCellType() == CellType.NUMERIC) ? (long) cell.getNumericCellValue() : null;
    }

    private Integer getIntegerCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        return (cell != null && cell.getCellType() == CellType.NUMERIC) ? (int) cell.getNumericCellValue() : null;
    }

    private LocalDate getLocalDateCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
