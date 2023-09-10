package com.example.final_UI_dev.config;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = repository.findByEmail(username);
        return user.map((Users user1) -> new UserInfoUserDetails(user1))
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
}
