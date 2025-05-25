package com.sharedcase.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ClassName: IcdFrequency
 * Package: com.sharedcase.DTO
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/25 17:50
 */
@Data
@AllArgsConstructor
public class IcdFrequency {
    private String icdCode;
    private Long count;
}
