package com.sharedcase.util;

import com.sharedcase.entity.CaseDetail;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName: CaseDiffUtil
 * Package: com.sharedcase.util
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/26 22:40
 */
public class CaseDiffUtil {

    public static Map<String, List<String>> compare(CaseDetail c1, CaseDetail c2) {
        Map<String, List<String>> diff = new LinkedHashMap<>();

        compareField("title", c1.getTitle(), c2.getTitle(), diff);
        compareField("icdCode", c1.getIcdCode(), c2.getIcdCode(), diff);
        compareField("chiefComplaint", c1.getChiefComplaint(), c2.getChiefComplaint(), diff);
        compareField("presentIllness", c1.getPresentIllness(), c2.getPresentIllness(), diff);
        compareField("pastHistory", c1.getPastHistory(), c2.getPastHistory(), diff);
        compareField("diagnosis", c1.getDiagnosis(), c2.getDiagnosis(), diff);
        compareField("doctorAdvice", c1.getDoctorAdvice(), c2.getDoctorAdvice(), diff);
        compareField("hospitalAddress", c1.getHospitalAddress(), c2.getHospitalAddress(), diff);
        compareField("visitTime", c1.getVisitTime().toString(), c2.getVisitTime().toString(), diff);
        return diff;
    }

    private static void compareField(String key, String v1, String v2, Map<String, List<String>> diff) {
        if (!Objects.equals(v1, v2)) {
            diff.put(key, List.of(nullToEmpty(v1), nullToEmpty(v2)));
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

