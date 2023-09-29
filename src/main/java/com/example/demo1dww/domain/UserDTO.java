package com.example.demo1dww.domain;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;
    private String user_pass;
    private String nikname;
    private String email;
    private byte[] p_img;
}
