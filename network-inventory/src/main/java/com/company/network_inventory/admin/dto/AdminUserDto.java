package com.company.network_inventory.admin.dto;

import com.company.network_inventory.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private Long userId;
    private String username;
    private UserRole role;
    private LocalDateTime lastLogin;
}
