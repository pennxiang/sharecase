package com.sharedcase.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sharedcase.entity.CaseDetail;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: PdfExportUtil
 * Package: com.sharedcase.util
 * Description:
 *      pdf工具类
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/23 13:35
 */
public class PdfUtil {

    /**
     * 导出病例详情为PDF
     * @param aCaseDetail
     * @param outputPath
     * @throws Exception
     */
    public static void exportCaseDetailToPdf(CaseDetail aCaseDetail, String outputPath) throws Exception {
        String html = buildHtml(aCaseDetail);

        try (OutputStream os = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }
    }

    public static File exportCaseDetailToPdf(CaseDetail caseDetail) throws Exception {
        String html = buildHtml(caseDetail);

        // 创建临时文件（自动带后缀，JVM退出时自动删除）
//        File tempPdf = File.createTempFile("case_", ".pdf");
        File tempPdf = new File("case_" + caseDetail.getCaseId() + ".pdf");

        try (OutputStream os = new FileOutputStream(tempPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // 注册中文字体
            builder.useFont(
                    new File("D:\\Projects\\lessonprojects\\sharedcase\\backend\\src\\main\\resources\\fonts\\NotoSerifCJKsc-VF.ttf"),
                    "SimSun"
            );
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }

        return tempPdf;
    }


    private static String buildHtml(CaseDetail aCaseDetail) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return """
                <html>
                <head>
                    <meta charset='UTF-8' />
                    <style>
                        body {
                            font-family: "SimSun", serif;
                            line-height: 1.6;
                            padding: 20px;
                            font-size: 14px;
                        }
                        h1 { text-align: center; }
                        p { margin: 6px 0; }
                        .label { font-weight: bold; }
                    </style>
                
                </head>
                <body>
                    <h1>病例信息</h1>
                    <p><span class='label'>病例ID：</span>%s</p>
                    <p><span class='label'>标题：</span>%s</p>
                    <p><span class='label'>ICD编码：</span>%s</p>
                    <p><span class='label'>医院ID：</span>%s</p>
                    <p><span class='label'>病人ID：</span>%s</p>
                    <p><span class='label'>医生ID：</span>%s</p>
                    <p><span class='label'>主诉：</span>%s</p>
                    <p><span class='label'>现病史：</span>%s</p>
                    <p><span class='label'>既往史：</span>%s</p>
                    <p><span class='label'>初步诊断：</span>%s</p>
                    <p><span class='label'>医生建议：</span>%s</p>
                    <p><span class='label'>创建时间：</span>%s</p>
                </body>
                </html>
                """.formatted(
                aCaseDetail.getCaseId(),
                aCaseDetail.getTitle(),
                aCaseDetail.getIcdCode(),
                aCaseDetail.getHospitalAddress(),
                aCaseDetail.getPatientAddress(),
                aCaseDetail.getDoctorAddress(),
                aCaseDetail.getChiefComplaint(),
                aCaseDetail.getPresentIllness(),
                aCaseDetail.getPastHistory(),
                aCaseDetail.getDiagnosis(),
                aCaseDetail.getDoctorAdvice(),
                aCaseDetail.getVisitTime() != null ? aCaseDetail.getVisitTime().format(formatter) : ""
        );
    }

    /**
     * 解析PDF文件为病例详情
     * @param pdfFile
     * @return
     * @throws Exception
     */
// 解析PDF为病例详情
    public static CaseDetail parsePdfToCaseDetail(File pdfFile) throws Exception {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Map<String, String> map = extractFields(text);
            CaseDetail detail = new CaseDetail();
            detail.setCaseId(map.get("病例ID"));
            detail.setTitle(map.get("标题"));
            detail.setIcdCode(map.get("ICD编码"));
            detail.setHospitalAddress(map.get("医院地址"));
            detail.setPatientAddress(map.get("病人地址"));
            detail.setDoctorAddress(map.get("医生地址"));
            detail.setChiefComplaint(map.get("主诉"));
            detail.setPresentIllness(map.get("现病史"));
            detail.setPastHistory(map.get("既往史"));
            detail.setDiagnosis(map.get("初步诊断"));
            detail.setDoctorAdvice(map.get("医生建议"));
            String visitTime = map.get("创建时间");
            if (visitTime != null) {
                detail.setVisitTime(LocalDateTime.parse(visitTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            return detail;
        }
    }

    private static Map<String, String> extractFields(String text) {
        Map<String, String> map = new HashMap<>();
        String[] lines = text.split("\r?\n");
        for (String line : lines) {
            int idx = line.indexOf("：");
            if (idx > 0) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                map.put(key, value);
            }
        }
        return map;
    }

}
