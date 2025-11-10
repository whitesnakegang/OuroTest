package com.c102.ourotest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String email;
    private String name;
    private int age;
    private String phone;
}
