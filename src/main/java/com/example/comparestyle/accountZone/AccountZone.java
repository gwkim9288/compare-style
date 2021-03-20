package com.example.comparestyle.accountZone;

import com.example.comparestyle.accounts.Account;
import com.example.comparestyle.zone.Zone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AccountZone {

    @Id
    @GeneratedValue
    @Column(name = "account_zone_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account zoneUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Zone zone;

}
