package com.example.comparestyle.accounts;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public Account createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setEmail(accountDto.getEmail());

        Account map = modelMapper.map(accountDto, Account.class);
        return accountRepository.save(map);
    }
}
