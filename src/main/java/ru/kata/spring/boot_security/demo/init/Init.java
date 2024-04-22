package ru.kata.spring.boot_security.demo.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initUsers() {
        List<Role> admin = Collections.singletonList(roleRepository.save(new Role(1L, "ROLE_ADMIN")));
        userService.saveUser(new User(1L, "lana", "12345", "wer", "wetr@gafdf.ru", admin));
        List<Role> user = Collections.singletonList(roleRepository.save(new Role(1L, "ROLE_USER")));
        userService.saveUser(new User(1L, "lan", "12345", "wer", "wetr@gafdf.ru", user));

    }
}