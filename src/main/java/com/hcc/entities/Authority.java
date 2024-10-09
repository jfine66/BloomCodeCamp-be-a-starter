package com.hcc.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

//@Entity
@Component
public class Authority implements GrantedAuthority {
    private long id;
    private String authority;
    @ManyToOne
    private User user;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
