package com.app.ecommere.security;

import com.app.ecommere.entity.Role;
import com.app.ecommere.entity.User;
import com.app.ecommere.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with this username: "+username));

        return new CustomUserDetail(user);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapRolesToAuthorities(user));
    }
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(User user) {
//        return Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));
//
//    }

}
