package com.niat.cms.service;

import com.niat.cms.domain.User;
import com.niat.cms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dunknown
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void setRole(long id, User.Role role) {
        userRepository.findOne(id).setRole(role);
    }
}
