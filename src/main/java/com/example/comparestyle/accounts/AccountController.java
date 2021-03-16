package com.example.comparestyle.accounts;

import com.example.comparestyle.commons.ErrorResource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/accounts", produces = MediaTypes.HAL_JSON_VALUE)
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;
    private final ModelMapper modelMapper;


    /**
     * 로직처리는  @Transactional이 달린 service 단에서 처리
     * WHY?
     * 하나의 업무처리가 금액지불 - 상품준비 - 배달 순서일 때 /  주문-상품준비-(만약 여기서 에러가 발생했다고 가정하면)-배달
     * 로직의 일부분에 에러가 발생한것은 결국 이 하나의 업무는 실패 //
     * 그런데 만약 전체 로직은 실패인데 일부분인 금액지불-상품준비는 성공으로 처리하면 배달결과는 모르겠고 돈먹고튀는것
     * 따라서 전체로직의 일부분이라도 실패한다면 그 로직은 전체적으로 실패한 로직  -> 일부분이라도 반영 x
     * 
     * BUT 여기서는 귀찮아서 그냥 컨트롤러에서
     * */

    @GetMapping
    public ResponseEntity queryAccounts(Pageable pageable, PagedResourcesAssembler<Account> assembler) {
        Page<Account> paged = accountRepository.findAll(pageable);
        PagedModel<EntityModel<Account>> accountResource = assembler.toModel(paged, p -> AccountResource.modelOf(p));
        //원래는 ROLE_USER권한인 사람들에게만
        accountResource.add(linkTo(AccountController.class).withRel("create-account"));
        return ResponseEntity.ok(accountResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        Optional<Account> byId = accountRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<Account> accountResource = AccountResource.modelOf(byId.get());
        accountResource.add(linkTo(AccountController.class).withRel("query-accounts"));
        // put도 만들어서 update추가해줘야한다

        // 만약 본인이라면 삭제도 할수있는 delete도 링크추가
        return ResponseEntity.ok(accountResource);
    }

    @PostMapping
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto accountDto, Errors errors) {
        if (errors.hasErrors()) {
            EntityModel<Errors> error1 = ErrorResource.modelOf(errors);
            return ResponseEntity.badRequest().body(error1);
        }
        //중복검사
        accountValidator.validate(accountDto, errors);
        if (errors.hasErrors()) {
            EntityModel<Errors> error2 = ErrorResource.modelOf(errors);
            return ResponseEntity.badRequest().body(error2);
        }
        Account saved = accountService.createAccount(accountDto);
        URI createdUri = linkTo(AccountController.class).slash(saved.getId()).toUri();
        // == /accounts/{id}

        //다음상태로 전이가능한 링크 hateoas를 만족 // 여기서는 셀프로 자기자신으로 가는링크와 전체 어카운트를 조회하는 링크추가
        EntityModel<Account> accountResource = AccountResource.modelOf(saved);
        accountResource.add(linkTo(AccountController.class).withRel("query-accounts"));

        return ResponseEntity.created(createdUri).body(accountResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
