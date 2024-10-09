package com.hcc.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String authority;
    @ManyToOne
    @JoinColumn(name = "user_id")
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
