package com.schoolapp.teacher_manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.schoolapp.teacher_manager.model.Teacher;
import com.schoolapp.teacher_manager.service.TeacherService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private TeacherService teacherService;

    private static final float FONT_SIZE = 15f;

    private static class Coordinates {
        public final float x;
        public final float y;
        public Coordinates(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Map<String, Coordinates> INSTALLATION_COORDS = new HashMap<>();
    private static final Map<String, Coordinates> RESUME_COORDS = new HashMap<>();

    static {
        // Installation report coordinates - fill with your current coords
        INSTALLATION_COORDS.put("lastName", new Coordinates(470, 841 - 383));
        INSTALLATION_COORDS.put("originalLastName", new Coordinates(180, 841 - 383));
        INSTALLATION_COORDS.put("firstName", new Coordinates(470, 841 - 408));
        INSTALLATION_COORDS.put("maritalStatus", new Coordinates(436, 841 - 433));
        INSTALLATION_COORDS.put("childrenCount", new Coordinates(180, 841 - 433));
        INSTALLATION_COORDS.put("address", new Coordinates(450, 841 - 457));
        INSTALLATION_COORDS.put("phoneNumber", new Coordinates(436, 841 - 531));
        INSTALLATION_COORDS.put("email", new Coordinates(340, 841 - 555));
        INSTALLATION_COORDS.put("rank", new Coordinates(500, 841 - 580));
        INSTALLATION_COORDS.put("currentInstitution", new Coordinates(436, 841 - 630));
        INSTALLATION_COORDS.put("previousInstitution", new Coordinates(436, 841 - 653));
        INSTALLATION_COORDS.put("appointmentDecisionNumber", new Coordinates(352, 841 - 335));
        INSTALLATION_COORDS.put("appointmentDecisionDate", new Coordinates(156, 841 - 335));
        INSTALLATION_COORDS.put("postalAccountNumber", new Coordinates(436, 841 - 482));
        INSTALLATION_COORDS.put("postalAccountKey", new Coordinates(180, 841 - 482));
        INSTALLATION_COORDS.put("socialSecurityNumber", new Coordinates(436, 841 - 506));
        INSTALLATION_COORDS.put("resumptionDate", new Coordinates(120, 510));
        INSTALLATION_COORDS.put("todayDate1", new Coordinates(212, 841 - 358));  // example for today date #1
        INSTALLATION_COORDS.put("todayDate2", new Coordinates(170, 841 - 701));  // example for today date #2

        // TODO: Add actual resume report coordinates here
        // For example:
        // RESUME_COORDS.put("lastName", new Coordinates(x, y));
        // RESUME_COORDS.put("originalLastName", new Coordinates(x, y));
        // ... and so on
        RESUME_COORDS.put("lastName", new Coordinates(470, 841 - 363));
        RESUME_COORDS.put("originalLastName", new Coordinates(180, 841 - 363));
        RESUME_COORDS.put("firstName", new Coordinates(470, 841 -   408+18));
        RESUME_COORDS.put("maritalStatus", new Coordinates(436, 841 -   433+15));
        RESUME_COORDS.put("childrenCount", new Coordinates(180, 841 -   433+15));                 
        RESUME_COORDS.put("address", new Coordinates(450, 841 -   457+12));
        RESUME_COORDS.put("phoneNumber", new Coordinates(436, 841 -   531+5));
        RESUME_COORDS.put("email", new Coordinates(340, 841 -   555));                  
        RESUME_COORDS.put("rank", new Coordinates(500, 841 -   580));
        RESUME_COORDS.put("currentInstitution", new Coordinates(436, 841 -   630-5));         
        RESUME_COORDS.put("postalAccountNumber", new Coordinates(436, 841 -   482+10));
        RESUME_COORDS.put("postalAccountKey", new Coordinates(180, 841 -    482+10));
        RESUME_COORDS.put("socialSecurityNumber", new Coordinates(436, 841 -    506+8)); 
        
        //continue heeeeeeeeeeeere
        RESUME_COORDS.put("resumptionDate", new Coordinates(376, 841 -   650-11));
        RESUME_COORDS.put("todayDate2", new Coordinates(110, 841 -   701-16));  // example for today date #2               
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(@RequestBody PdfRequest request) throws Exception {
        Teacher teacher = teacherService.getTeacherById(request.getTeacherId());
        if (teacher == null) {
            return ResponseEntity.badRequest().build();
        }

        String reportType = request.getReportType();
        String templatePath = "pdf/templates/" +
                ("resumeReport".equals(reportType) ? "resumeReport.pdf" : "installationReport.pdf");

        Map<String, Coordinates> coords = "resumeReport".equals(reportType) ? RESUME_COORDS : INSTALLATION_COORDS;

        try (InputStream templateStream = new ClassPathResource(templatePath).getInputStream()) {
            byte[] pdfBytes = templateStream.readAllBytes();
            try (PDDocument pdf = Loader.loadPDF(pdfBytes)) {

                PDPage page = pdf.getPage(0);

                try (InputStream fontStream = new ClassPathResource("fonts/NotoNaskhArabic-Regular.ttf").getInputStream();
                     PDPageContentStream cs = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, true)) {

                    PDType0Font font = PDType0Font.load(pdf, fontStream, true);

                    // Write fields dynamically using the selected coordinate map
                    writeField(cs, font, shapeArabic(teacher.getLastName()), "lastName", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getOriginalLastName()), "originalLastName", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getFirstName()), "firstName", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getMaritalStatus()), "maritalStatus", coords, FONT_SIZE);
                    writeField(cs, font, String.valueOf(teacher.getChildrenCount()), "childrenCount", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getAddress()), "address", coords, FONT_SIZE);
                    writeField(cs, font, teacher.getPhoneNumber(), "phoneNumber", coords, FONT_SIZE);
                    writeField(cs, font, teacher.getEmail(), "email", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getRank()), "rank", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getCurrentInstitution()), "currentInstitution", coords, FONT_SIZE);
                    writeField(cs, font, shapeArabic(teacher.getPreviousInstitution()), "previousInstitution", coords, FONT_SIZE);
                    writeField(cs, font, teacher.getAppointmentDecisionNumber(), "appointmentDecisionNumber", coords, FONT_SIZE);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String appointmentDecisionDateStr = teacher.getAppointmentDecisionDate() != null
                            ? teacher.getAppointmentDecisionDate().format(formatter)
                            : "";
                    writeField(cs, font, appointmentDecisionDateStr, "appointmentDecisionDate", coords, FONT_SIZE);

                    writeField(cs, font, teacher.getPostalAccountNumber(), "postalAccountNumber", coords, FONT_SIZE);
                    writeField(cs, font, teacher.getPostalAccountKey(), "postalAccountKey", coords, FONT_SIZE);
                    writeField(cs, font, teacher.getSocialSecurityNumber(), "socialSecurityNumber", coords, FONT_SIZE);

                    String resumptionDateStr = teacher.getResumptionDate() != null
                            ? teacher.getResumptionDate().format(formatter)
                            : "";
                    writeField(cs, font, resumptionDateStr, "resumptionDate", coords, FONT_SIZE);

                    LocalDate today = LocalDate.now();
                    String formattedToday = today.format(formatter);
                    writeField(cs, font, formattedToday, "todayDate1", coords, FONT_SIZE);
                    writeField(cs, font, formattedToday, "todayDate2", coords, FONT_SIZE);

                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                pdf.save(baos);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(baos.toByteArray());
            }
        }
    }

    private void writeField(PDPageContentStream cs, PDType0Font font, String text, String fieldName,
                            Map<String, Coordinates> coords, float fontSize) throws Exception {
        Coordinates c = coords.get(fieldName);
        if (c != null) {
            writeRightAlignedText(cs, font, text, c.x, c.y, fontSize);
        }
    }

    private void writeRightAlignedText(PDPageContentStream cs, PDType0Font font, String text,
                                       float x, float y, float fontSize) throws Exception {
        if (text == null) text = "";
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float startX = x - textWidth;
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(startX, y);
        cs.showText(text);
        cs.endText();
    }

    private String shapeArabic(String input) throws ArabicShapingException {
        if (input == null) return "";
        ArabicShaping shaper = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
        String shaped = shaper.shape(input);
        Bidi bidi = new Bidi(shaped, Bidi.REORDER_DEFAULT);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }
}
