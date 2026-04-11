package ru.alinacozy.NauJava.service;

import ru.alinacozy.NauJava.entity.User;

public interface UserService {
    User findUserByUsername(String username);
    void addUser(User user);
}
