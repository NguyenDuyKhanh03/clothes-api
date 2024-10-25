package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.dto.JwtResponse;
import com.example.clothes_api.entity.Account;
import com.example.clothes_api.exception.AccountAlreadyExistsException;
import com.example.clothes_api.exception.ResourceNotFoundException;
import com.example.clothes_api.repository.AccountRepository;
import com.example.clothes_api.repository.RoleRepository;
import com.example.clothes_api.services.IAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthentication {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public void signUp(AccountDTO.SignUpRequest signUpRequest) {
        accountRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(account -> {
                    throw new AccountAlreadyExistsException("Email already exists");
                });

        Account user = new Account();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        roleRepository.findByName("ROLE_USER").ifPresent(user::setRole);
        user.setName(signUpRequest.getFull_name());
        user.setNumberPhone(signUpRequest.getPhone_number());
        user.setActive(true);
        accountRepository.save(user);
    }



    public JwtResponse verify(AccountDTO.RegisterRequest request){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        if(authentication.isAuthenticated()){
            return new JwtResponse(jwtService.generateToken(request.getEmail()));
        }
        return new JwtResponse("Login failed");
    }

    public void resetPassword(String email, String password) {
        Account account= accountRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

}
