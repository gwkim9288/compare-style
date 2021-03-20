package com.example.comparestyle.accountZone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountZoneRepository extends JpaRepository<AccountZone,Long> {
}
