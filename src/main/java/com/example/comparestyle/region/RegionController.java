package com.example.comparestyle.region;

import com.example.comparestyle.accounts.Account;
import com.example.comparestyle.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {

    private final RegionRepository regionRepository;
    private final RegionService regionService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;


    @PostMapping("/{accountId}")
    public ResponseEntity createRegion(@PathVariable Long accountId, @RequestBody RegionDto regionDto) {
        Optional<Account> byId = accountRepository.findById(accountId);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Region map = modelMapper.map(regionDto, Region.class);
        map.setManager(byId.get());
        Region saved = regionRepository.save(map);

        EntityModel<Region> regionResource = RegionResource.modelOf(saved);
        URI uri = linkTo(RegionController.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(regionResource);
    }
    // 당연히 region의 account를 json으로 만들어주려고하고 -> 그럼 account의 region을 또 json으로 만들어주려하고
    // -> 또 region의 account를 json으로 만들어주려고하는 무한참조
}
