package com.vinaypabba.bankrest.service;

import com.vinaypabba.bankrest.model.Account;
import com.vinaypabba.bankrest.model.SignUpRequest;
import com.vinaypabba.bankrest.model.User;
import com.vinaypabba.bankrest.model.UserPrincipal;
import com.vinaypabba.bankrest.repo.AccountRepository;
import com.vinaypabba.bankrest.repo.UserRepository;
import com.vinaypabba.bankrest.util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    @Autowired
    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
                );
        return UserPrincipal.create(user);
    }

    public boolean registerUser(SignUpRequest signUpRequest) {
        Account account = accountRepository.findAccountByNumber(signUpRequest.getAccountNumber());
        if(account.getUser() != null) {
            throw new BusinessException("Account number already registered");
        }
        User user = new User();
        user.setAccountNumber(signUpRequest.getAccountNumber());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        User savedUser = userRepository.save(user);
        account.setUser(savedUser);
        accountRepository.save(account);
        return true;
    }

    public Map<String, Object> signInUser(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        LocalDateTime expiry = LocalDateTime.now().plusHours(12);
        response.put("accessToken", userPrincipal.getUsername() + "_" + expiry.toString());
        response.put("tokenType", "Bearer");
        response.put("expiry", expiry.toString());
        return response;
    }

}
