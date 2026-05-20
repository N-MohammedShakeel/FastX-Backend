package com.example.FastX.security.service;


import com.example.FastX.entity.User;
import com.example.FastX.security.model.UserPrincipal;
import com.example.FastX.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndActiveTrue(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return new UserPrincipal(user);
    }
}
