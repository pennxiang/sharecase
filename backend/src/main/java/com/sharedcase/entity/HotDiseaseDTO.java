package com.sharedcase.entity;

import lombok.Data;

/**
 * ClassName: HotDiseeaseDTO
 * Package: com.sharedcase.entity
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/23 15:52
 */
@Data
public class HotDiseaseDTO {
    private String icdCode;
    private Long count;
    private String icdName; // 可选，从 ICD 表联查
}
