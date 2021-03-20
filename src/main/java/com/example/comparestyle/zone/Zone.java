package com.example.comparestyle.zone;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Zone {
    @Id
    @GeneratedValue
    @Column(name = "zone_id")
    private Long id;

    private String street;
}
