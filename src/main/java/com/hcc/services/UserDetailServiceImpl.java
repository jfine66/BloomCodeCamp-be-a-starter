package com.hcc.services;

 import com.hcc.entities.User;
 import com.hcc.repositories.UserRepository;
 import com.hcc.utils.CustomPasswordEncoder;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;


 import java.util.Optional;


@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepo.findByUsername(username);
        return userOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
    }
}
