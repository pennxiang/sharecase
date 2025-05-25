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
     * @param caseDetail
     * @param outputPath
     * @throws Exception
     */
    public static void exportCaseDetailToPdf(CaseDetail caseDetail, String outputPath) throws Exception {
        String html = buildHtml(caseDetail);

        try (OutputStream os = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }
    }

    private static String buildHtml(CaseDetail caseDetail) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return """
                <html>
                <head>
                    <meta charset='UTF-8'>
                    <style>
                        body { font-family: sans-serif; line-height: 1.5; padding: 20px; }
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
                    <p><span class='label'>医院ID：</span>%d</p>
                    <p><span class='label'>用户ID：</span>%d</p>
                    <p><span class='label'>医生ID：</span>%d</p>
                    <p><span class='label'>主诉：</span>%s</p>
                    <p><span class='label'>现病史：</span>%s</p>
                    <p><span class='label'>既往史：</span>%s</p>
                    <p><span class='label'>初步诊断：</span>%s</p>
                    <p><span class='label'>医生建议：</span>%s</p>
                    <p><span class='label'>复查结果：</span>%s</p>
                    <p><span class='label'>创建时间：</span>%s</p>
                    <p><span class='label'>更新时间：</span>%s</p>
                </body>
                </html>
                """.formatted(
                caseDetail.getCaseId(),
                caseDetail.getTitle(),
                caseDetail.getIcdCode(),
                caseDetail.getHospitalId(),
                caseDetail.getUserId(),
                caseDetail.getDoctorId(),
                caseDetail.getChiefComplaint(),
                caseDetail.getPresentIllnessHistory(),
                caseDetail.getPastHistory(),
                caseDetail.getDiagnosis(),
                caseDetail.getDoctorAdvice(),
                caseDetail.getIsRecheck(),
                caseDetail.getCreateTime() != null ? caseDetail.getCreateTime().format(formatter) : "",
                caseDetail.getUpdateTime() != null ? caseDetail.getUpdateTime().format(formatter) : ""
        );
    }

    /**
     * 解析PDF文件为病例详情
     * @param pdfFile
     * @return
     * @throws Exception
     */

    public static CaseDetail parsePdfToCaseDetail(File pdfFile) throws Exception {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            CaseDetail detail = new CaseDetail();
            Map<String, String> map = extractFields(text);

            detail.setCaseId(map.get("病例ID"));
            detail.setTitle(map.get("标题"));
            detail.setIcdCode(map.get("ICD编码"));
            detail.setHospitalId(Long.parseLong(map.get("医院ID")));
            detail.setUserId(Long.parseLong(map.get("用户ID")));
            detail.setDoctorId(Long.parseLong(map.get("医生ID")));
            detail.setChiefComplaint(map.get("主诉"));
            detail.setPresentIllnessHistory(map.get("现病史"));
            detail.setPastHistory(map.get("既往史"));
            detail.setDiagnosis(map.get("初步诊断"));
            detail.setDoctorAdvice(map.get("医生建议"));
            detail.setIsRecheck(map.get("是否复查"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            detail.setCreateTime(LocalDateTime.parse(map.get("创建时间"), formatter));
            detail.setUpdateTime(LocalDateTime.parse(map.get("更新时间"), formatter));

            return detail;
        }
    }

    private static Map<String, String> extractFields(String text) {
        Map<String, String> result = new HashMap<>();
        String[] lines = text.split("\n");
        for (String line : lines) {
            int sep = line.indexOf("：");
            if (sep > 0) {
                String key = line.substring(0, sep).trim();
                String value = line.substring(sep + 1).trim();
                result.put(key, value);
            }
        }
        return result;
    }

}
