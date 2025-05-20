package com.sharedcase.DTO;

import lombok.Data;

/**
 * ClassName: UserDTO
 * Package: com.sharedcase.DTO
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 16:17
 */
@Data
public class UserDTO {

    private Long id;

    private String name;

    private String role;

    private String phone;

    private String idCard;

    private Long workId;
}
