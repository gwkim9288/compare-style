package com.example.comparestyle.region;

import com.example.comparestyle.accounts.Account;
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
public class Region {
    @Id
    @GeneratedValue
    @Column(name = "region_id")
    private Long id;

    private String city;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account manager;

    // 이건 그냥 예시고 앞으로 만들 프로젝트에서
    // n : 1 ( posts : plan )에서 1인 플랜에서는 posts들을 보여줘야함
    // n인 post에서는 plan에 대한 정보를 보여줄 필요가없다
}
