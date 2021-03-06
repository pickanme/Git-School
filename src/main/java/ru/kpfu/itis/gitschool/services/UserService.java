package ru.kpfu.itis.gitschool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.gitschool.models.User;
import ru.kpfu.itis.gitschool.repositories.UserAuthorityRepository;
import ru.kpfu.itis.gitschool.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAuthorityRepository userAuthorityRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signUpUser(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new DuplicateKeyException("Duplicate key - email field");
        }
        user.addAuthority(userAuthorityRepo.findByAuthority(user.getRole()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email);
    }
}
