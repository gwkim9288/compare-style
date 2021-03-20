package com.example.comparestyle.accounts;

import com.example.comparestyle.accountZone.AccountZone;
import com.example.comparestyle.region.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String username;
    private String email;


    @OneToMany(mappedBy = "manager")
    private List<Region> regions = new ArrayList<>();

    @OneToMany(mappedBy = "zoneUser")
    private List<AccountZone> accountZones = new ArrayList<>();
}
