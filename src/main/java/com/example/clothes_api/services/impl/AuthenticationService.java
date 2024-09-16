package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.entity.Account;
import com.example.clothes_api.repository.AccountRepository;
import com.example.clothes_api.repository.RoleRepository;
import com.example.clothes_api.services.IAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthentication {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Override
    public void signUp(AccountDTO.SignUpRequest signUpRequest) {
        accountRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(account -> {
                    throw new RuntimeException();
                });

        Account user = new Account();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        roleRepository.findByName("ROLE_USER").ifPresent(user::setRole);
        user.setName(signUpRequest.getFull_name());
        user.setNumberPhone(signUpRequest.getPhone_number());
        user.setActive(true);
        accountRepository.save(user);
    }

    public void resetPassword(String email, String password) {
        Account account= accountRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);

        account.setPassword(password);
        accountRepository.save(account);
    }
}
