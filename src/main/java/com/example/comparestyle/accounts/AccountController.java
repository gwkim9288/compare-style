package com.example.comparestyle.accounts;

import com.example.comparestyle.accountZone.AccountZone;
import com.example.comparestyle.accountZone.AccountZoneRepository;
import com.example.comparestyle.commons.ErrorResource;
import com.example.comparestyle.zone.Zone;
import com.example.comparestyle.zone.ZoneRepository;
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
import org.springframework.transaction.annotation.Transactional;
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
    private final ZoneRepository zoneRepository;
    private final AccountZoneRepository accountZoneRepository;
    


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
        PagedModel<EntityModel<AccountResponse>> accountResource = assembler.toModel(paged, p -> AccountResource.modelOf(p));
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
        EntityModel<AccountResponse> accountResource = AccountResource.modelOf(byId.get());
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
        EntityModel<AccountResponse> accountResource = AccountResource.modelOf(saved);
        accountResource.add(linkTo(AccountController.class).withRel("query-accounts"));

        return ResponseEntity.created(createdUri).body(accountResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    /**
     * ==   =   =   =   ==  =   === -    -   -   -   -   -   -   -
     * post - postTag - tag를 가정하여
     * account - accountZone - zone을 통해 다대다 풀어나가기 테스트
     */

    @PostMapping("/zone")
    public ResponseEntity createZone(@RequestBody AZDto azDto) {
        // 여기서는 틀만 복습해보기위해 그냥 controller에서 구현
        //당연히 프로젝트 구현할 땐 서비스단으로 빼줘서 로직처리
        // 우리프로젝트에서는 타이틀 일정 태그입력받고 세션에 저장된 유저id로 db에서 찾아와서 manager세팅
        // 이 후 태그 하나씩 만들어서 태그저장하면서 plan.getTagItem().add(만든태그)
        String[] split = azDto.getZone().split(",");

        Account account = new Account();
        account.setEmail(azDto.getEmail());
        account.setUsername(azDto.getUsername());
        Account saved = accountRepository.save(account);


        for (String s : split) {
            Zone zone = new Zone();
            zone.setStreet(s);
            Zone save = zoneRepository.save(zone);

            AccountZone accountZone = new AccountZone();
            accountZone.setZoneUser(saved);
            accountZone.setZone(save);
            AccountZone save1 = accountZoneRepository.save(accountZone);
            //양방향이니까 다에서 먼저 set하고 1에서도 setting해줘
            account.getAccountZones().add(save1);
        }

        /**  응답용 dto(accountResponse // 우리프로젝트에서는 아마 post and planResponse)를 만들고
         * 리소스 클래스의 static modelOf함수에서 다 변환시켜줘야함
         * 안그럼 직렬화하라는 에러가난다 */
        EntityModel<AccountResponse> accountResource = AccountResource.modelOf(saved);
        return ResponseEntity.ok(accountResource);
    }

}
