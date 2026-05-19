package com.saghar.marketplace.api.controller;

import com.saghar.marketplace.api.dto.ManagerDtos;
import com.saghar.marketplace.api.service.AppState;
import ir.ac.kntu.UserManager;
import ir.ac.kntu.model.Manager;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    private final AppState state;

    public ManagerController(AppState state) {
        this.state = state;
    }

    @PostMapping("/block")
    public ManagerDtos.BlockUserResponse block(@Valid @RequestBody ManagerDtos.BlockUserRequest req, Authentication auth) {
        String username = (String) auth.getPrincipal();
        UserManager um = state.userManager();

        Manager me = um.getManagers().stream()
                .filter(m -> m.getUsername() != null && m.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Manager not found in state"));

        long targetId = req.targetUserId();
        if (targetId < 0 || targetId >= um.getManagers().size()) {
            throw new IllegalArgumentException("targetUserId must be a manager index (demo)");
        }
        Manager target = um.getManagers().get((int) targetId);

        if (!me.canManage(target)) {
            throw new HierarchyViolationException();
        }

        // In real app: persist block relation. In this demo: just return success.
        return new ManagerDtos.BlockUserResponse(targetId, um.getManagers().indexOf(me), "BLOCKED");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    private static class HierarchyViolationException extends RuntimeException {
        public HierarchyViolationException() {
            super("Manager cannot block self/ancestor or same/lower authority manager.");
        }
    }
}
