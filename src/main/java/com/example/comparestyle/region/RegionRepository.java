package com.example.comparestyle.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RegionRepository extends JpaRepository<Region,Long> {
}
