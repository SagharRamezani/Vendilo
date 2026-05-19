package com.saghar.marketplace.api.controller;

import com.saghar.marketplace.api.dto.AuthDtos;
import com.saghar.marketplace.api.security.JwtService;
import com.saghar.marketplace.api.service.AppState;
import ir.ac.kntu.UserManager;
import ir.ac.kntu.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AppState state;
    private final JwtService jwt;
    private final long expiry;

    public AuthController(AppState state, JwtService jwt, @Value("${app.jwt.expirySeconds}") long expiry) {
        this.state = state;
        this.jwt = jwt;
        this.expiry = expiry;
    }

    @PostMapping("/login")
    public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest req) throws Exception {
        UserManager um = state.userManager();
        String key = req.usernameOrEmail();
        String password = req.password();

        // Try manager
        try {
            Manager m = um.authManager(key, password);
            return ok(key, AuthDtos.Role.MANAGER, m.getFirstName() + " " + m.getLastName());
        } catch (Exception ignored) {}

        // Try supporter
        try {
            // Core UserManager exposes supporter auth as logInSupporter.
            Supporter s = um.logInSupporter(key, password);
            return ok(key, AuthDtos.Role.SUPPORT, s.getFirstName() + " " + s.getLastName());
        } catch (Exception ignored) {}

        // Try customer email
        try {
            Customer c = um.logInCustomerE(key, password);
            return ok(c.getEmail(), AuthDtos.Role.CUSTOMER, c.getFirstName() + " " + c.getLastName());
        } catch (Exception ignored) {}

        // Try customer phone
        try {
            Customer c = um.logInCustomerP(key, password);
            return ok(c.getPhone(), AuthDtos.Role.CUSTOMER, c.getFirstName() + " " + c.getLastName());
        } catch (Exception ignored) {}

        // Try seller store code
        try {
            Seller s = um.getSellerByCode(key);
            if (ir.ac.kntu.util.PasswordHasher.matches(s.getPassword(), password)) {
                return ok(s.getStoreCode(), AuthDtos.Role.SELLER, s.getStoreName());
            }
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Invalid credentials");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest req) throws Exception {
        UserManager um = state.userManager();
        return switch (req.role()) {
            case CUSTOMER -> {
                if (req.email() == null || req.email().isBlank()) {
                    throw new IllegalArgumentException("email is required for CUSTOMER");
                }
                // Customer constructor: firstName, lastName, email, phone, password
                Customer c = new Customer(req.firstName(), req.lastName(), req.email(), req.phone(), req.password());
                um.registerCustomer(c);
                yield ok(c.getEmail(), AuthDtos.Role.CUSTOMER, c.getFirstName() + " " + c.getLastName());
            }
            case SELLER -> {
                if (req.storeName() == null || req.storeName().isBlank()) {
                    throw new IllegalArgumentException("storeName is required for SELLER");
                }
                // Seller constructor: firstName, lastName, phone, password, storeCode, storeName, storeState, nationalId
                Seller s = new Seller(req.firstName(), req.lastName(), req.phone(), req.password(),
                        req.storeCode() == null ? "" : req.storeCode(), req.storeName(),
                        req.city() == null ? "" : req.city(),
                        req.nationalId() == null ? "" : req.nationalId());
                um.registerSeller(s);
                yield ok(s.getStoreCode(), AuthDtos.Role.SELLER, s.getStoreName());
            }
            case MANAGER -> {
                if (req.username() == null || req.username().isBlank()) {
                    throw new IllegalArgumentException("username is required for MANAGER");
                }
                Manager parent = null;
                if (req.parentManagerId() != null) {
                    // best-effort lookup by list index (demo). In real impl use id.
                    var ms = um.getManagers();
                    if (req.parentManagerId() >= 0 && req.parentManagerId() < ms.size()) {
                        parent = ms.get(req.parentManagerId().intValue());
                    }
                }
                Manager m = (parent == null)
                        ? new Manager(req.firstName(), req.lastName(), req.password(), req.username(), 0)
                        : new Manager(req.firstName(), req.lastName(), req.password(), req.username(), parent);
                um.addManager(m);
                yield ok(req.username(), AuthDtos.Role.MANAGER, m.getFirstName() + " " + m.getLastName());
            }
            case SUPPORT -> {
                if (req.username() == null || req.username().isBlank()) {
                    throw new IllegalArgumentException("username is required for SUPPORT");
                }
                Supporter sp = new Supporter(req.firstName(), req.lastName(), req.username(), req.password(), SupportSection.ORDER);
                um.addSupporter(sp);
                yield ok(req.username(), AuthDtos.Role.SUPPORT, sp.getFirstName() + " " + sp.getLastName());
            }
        };
    }

    private AuthDtos.AuthResponse ok(String key, AuthDtos.Role role, String displayName) {
        String token = jwt.createToken(key, role.name());
        return new AuthDtos.AuthResponse(token, "Bearer", expiry, new AuthDtos.UserProfile(key, role, displayName));
    }
}
