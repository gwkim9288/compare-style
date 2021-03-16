package com.example.comparestyle.accounts;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AccountResource extends EntityModel<Account> {
    public static EntityModel<Account> modelOf(Account account) {
        EntityModel<Account> accountResource = EntityModel.of(account);
        accountResource.add(linkTo(AccountController.class).slash(account.getId()).withSelfRel());
        return accountResource;
    }
}
