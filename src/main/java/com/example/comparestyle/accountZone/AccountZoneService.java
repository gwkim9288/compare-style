package com.example.comparestyle.accountZone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountZoneService {

    private final AccountZoneRepository accountZoneRepository;

}
