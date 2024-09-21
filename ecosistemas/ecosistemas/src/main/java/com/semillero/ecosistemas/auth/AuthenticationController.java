package com.semillero.ecosistemas.auth;

import com.semillero.ecosistemas.model.User;
import com.semillero.ecosistemas.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> saveGoogleUser(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {
        User newUser = authenticationService.saveGoogleUser(oAuth2User);
        String token = authenticationService.generateJwtToken(newUser);

        response.sendRedirect("http://localhost:5173/login");
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        session.invalidate();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Sesión cerrada.";
    }
//    @GetMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
//        session.invalidate();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        response.sendRedirect("http://localhost:5173");
//    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@AuthenticationPrincipal OAuth2User oAuth2User) {
        try {
            if (oAuth2User == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email attribute is missing");
            }

            Optional<User> user = userService.findUserByEmail(email);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            String token = authenticationService.generateJwtToken(user.get());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}