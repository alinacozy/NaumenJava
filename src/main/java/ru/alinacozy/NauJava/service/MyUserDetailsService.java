package ru.alinacozy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alinacozy.NauJava.entity.User;
import ru.alinacozy.NauJava.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    MyUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                mapRoles(appUser)
        );
    }

    private Collection<GrantedAuthority> mapRoles(User appUser) {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())
        );
    }

}
