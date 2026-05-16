package com.ddangkhoa.trafficfx.service;

import com.ddangkhoa.trafficfx.model.AppUser;
import com.ddangkhoa.trafficfx.model.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {
    private final Map<String, String> passwords = new HashMap<>();
    private final Map<String, AppUser> users = new HashMap<>();

    public AuthService() {
        addUser("admin", "admin123", "Nguyễn Quản Trị", Role.ADMIN);
        addUser("tech", "tech123", "Trần Kỹ Thuật", Role.TECHNICIAN);
        addUser("analyst", "analyst123", "Lê Phân Tích", Role.ANALYST);
    }

    private void addUser(String username, String password, String fullName, Role role) {
        passwords.put(username, password);
        users.put(username, new AppUser(username, fullName, role));
    }

    public Optional<AppUser> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        String normalizedUsername = username.trim();
        String storedPassword = passwords.get(normalizedUsername);
        if (storedPassword != null && storedPassword.equals(password.trim())) {
            return Optional.ofNullable(users.get(normalizedUsername));
        }
        return Optional.empty();
    }
}
