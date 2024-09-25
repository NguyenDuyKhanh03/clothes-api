package com.example.clothes_api.services.impl;

import com.example.clothes_api.entity.Account;
import com.example.clothes_api.exception.AccountAlreadyExistsException;
import com.example.clothes_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Optional<Account> getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email="";
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return accountRepository.findByEmail(email);
    }

    public void saveAccount(Account user) {
        if(Objects.isNull(user)){
            throw new AccountAlreadyExistsException("User not found");
        }
        accountRepository.save(user);
    }
}
