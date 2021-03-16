package com.example.comparestyle.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountDto.class.isAssignableFrom(aClass);
    }

    public void validate(Object target, Errors errors) {
        AccountDto accountDto = (AccountDto) target;
        Account account1 =  accountRepository.findByUsername(accountDto.getUsername());
        if (account1 != null) {
            errors.rejectValue("username", "wrongValue", "someone already use this name");
        }
    }
}
