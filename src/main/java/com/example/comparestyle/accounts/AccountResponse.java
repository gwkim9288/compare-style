package com.example.comparestyle.accounts;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse implements Serializable {
    private String username;
    private String email;
    private List<String> regions = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
}
