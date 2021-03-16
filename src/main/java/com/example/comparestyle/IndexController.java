package com.example.comparestyle;

import com.example.comparestyle.accounts.AccountController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/")
    public RepresentationModel index() { // 여기서 String 리턴해줘서 ErrorResource에서 인덱스 링크달아줄 때 에러 계속생겼던 것
        var index = new RepresentationModel<>();
        index.add(linkTo(AccountController.class).withRel("accounts"));
        return index;
    }
}
