package com.company.network_inventory.admin.controller;

import com.company.network_inventory.admin.dto.AdminUserDto;
import com.company.network_inventory.admin.dto.UpdateUserRoleRequest;
import com.company.network_inventory.entity.AppUser;
import com.company.network_inventory.entity.enums.UserRole;
import com.company.network_inventory.repository.AppUserRepository;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AppUserRepository appUserRepository;

    // ✅ GET /api/admin/users
    @GetMapping
    public ApiResponse<List<AdminUserDto>> allUsers() {
        List<AppUser> users = appUserRepository.findAll();

        List<AdminUserDto> dto = users.stream()
                .map(u -> AdminUserDto.builder()
                        .userId(u.getUserId())
                        .username(u.getUsername())
                        .role(u.getRole())
                        .lastLogin(u.getLastLogin())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.ok("Users", dto);
    }

    // ✅ PUT /api/admin/users/{id}/role  (ADMIN only)
    @PutMapping("/{id}/role")
    public ApiResponse<AdminUserDto> updateRole(@PathVariable("id") Long id,
                                                @Valid @RequestBody UpdateUserRoleRequest req) {

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        UserRole newRole;
        try {
            newRole = UserRole.valueOf(req.getRole().trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + req.getRole());
        }

        user.setRole(newRole);
        AppUser saved = appUserRepository.save(user);

        AdminUserDto dto = AdminUserDto.builder()
                .userId(saved.getUserId())
                .username(saved.getUsername())
                .role(saved.getRole())
                .lastLogin(saved.getLastLogin())
                .build();

        return ApiResponse.ok("Role updated", dto);
    }
}
