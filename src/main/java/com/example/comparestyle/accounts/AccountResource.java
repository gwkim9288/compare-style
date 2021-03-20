package com.example.comparestyle.accounts;

import com.example.comparestyle.accountZone.AccountZone;
import com.example.comparestyle.region.Region;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class AccountResource extends EntityModel<AccountResponse> {
    public static EntityModel<AccountResponse> modelOf(Account account) {

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setUsername(account.getUsername());
        List<Region> regions = account.getRegions();
        for (Region region : regions) {
            accountResponse.getRegions().add(region.getCity());
        }
        List<AccountZone> accountZones = account.getAccountZones();
        for (AccountZone accountZone : accountZones) {
            String street = accountZone.getZone().getStreet();
            accountResponse.getTags().add(street);
        }

        // 추가로 여기에 동일하게 region도 풀어줘야한다

        EntityModel<AccountResponse> accountResource = EntityModel.of(accountResponse);
        accountResource.add(linkTo(AccountController.class).slash(account.getId()).withSelfRel());
        return accountResource;
    }
}
