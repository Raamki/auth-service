package com.security.authservice.service;

import com.security.authservice.entity.UserDetail;
import com.security.authservice.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserDetailRepository userDetailRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetail userDetail = userDetailRepository.findTopByUsernameIgnoreCase(userName);
        if (Objects.nonNull(userDetail)) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userDetail.getRole().getName());
            return new User(userDetail.getUsername(), userDetail.getPassword(), Collections.singleton(grantedAuthority));
        }
        return null;
    }



}
