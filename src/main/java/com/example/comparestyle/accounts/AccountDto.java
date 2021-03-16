package com.example.comparestyle.accounts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
}
