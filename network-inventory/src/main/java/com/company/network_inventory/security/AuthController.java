package com.company.network_inventory.security;

import com.company.network_inventory.entity.AppUser;
import com.company.network_inventory.entity.enums.UserRole;
import com.company.network_inventory.repository.AppUserRepository;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (BadCredentialsException ex) {
            // ✅ should be 401 not 500
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }

        AppUser user = appUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getUsername(), user.getRole().name());
    }

    /**
     * ✅ Round-1 Bootstrap: create first admin user
     * Remove or lock down in Round-2.
     */
    @PostMapping("/bootstrap")
    public String bootstrap(@RequestBody BootstrapRequest req) {
        if (appUserRepository.findByUsername(req.username()).isPresent()) {
            return "User already exists: " + req.username();
        }

        AppUser u = new AppUser();
        u.setUsername(req.username());
        u.setPasswordHash(passwordEncoder.encode(req.password())); // ✅ BCrypt
        u.setRole(req.role() == null ? UserRole.ADMIN : req.role());

        appUserRepository.save(u);
        return "Created user: " + u.getUsername() + " role=" + u.getRole();
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(Authentication authentication) {

        Map<String, Object> data = new HashMap<>();
        data.put("username", authentication.getName());

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        data.put("roles", roles);

        return ApiResponse.ok("Authenticated user", data);
    }



    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String username, String role) {}
    public record MeResponse(String username) {}
    public record BootstrapRequest(String username, String password, UserRole role) {}
}
