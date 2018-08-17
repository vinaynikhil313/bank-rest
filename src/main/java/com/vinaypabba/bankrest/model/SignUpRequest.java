package com.vinaypabba.bankrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

        @NotBlank
        @Size(min = 3, max = 15)
        private String username;

        @NotBlank
        @Size(max = 40)
        private String accountNumber;

        @NotBlank
        @Size(min = 6, max = 20)
        private String password;

}
