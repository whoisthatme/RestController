package ru.kata.spring.boot_security.demo.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            return getByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public User getByUsername(String username) {

        return userDAO
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> getUsers() {
        return userDAO.findAll();
    }

    public void editUser(User user) {

        String oldPassword = getById(user.getUserId()).getPassword();

        if (oldPassword.equals(user.getPassword())) {
            System.out.println("TRUE");
            user.setPassword(oldPassword);
            createUser(user);
        } else {
            System.out.println("FALSE");
            userDAO.save(user);
        }
    }

    public void createUser(User user) {
        userDAO.save(passwordCoder(user));
    }

    public User getById(long id) {

        return userDAO
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void deleteById(long id) {
        userDAO.deleteById(id);
    }

    public User passwordCoder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addDefaultUser() {
        Set<Role> roles1 = new HashSet<>();
        roles1.add(roleDAO.findById(1L).orElse(null));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleDAO.findById(1L).orElse(null));
        roles2.add(roleDAO.findById(2L).orElse(null));
        User user1 = new User("Lana", "Bana", (byte) 21, "user@mail.com", "user", "user", roles1);
        User user2 = new User("Kana", "Fana", (byte) 25, "admin@mail.com", "admin", "admin", roles2);
        createUser(user1);
        createUser(user2);
    }
}
