package com.semillero.ecosistemas.auth;

import com.semillero.ecosistemas.jwt.JwtService;
import com.semillero.ecosistemas.model.User;
import com.semillero.ecosistemas.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    JwtService jwtConfig;

    public User saveGoogleUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> foundUser = userRepository.findUserByEmail(email);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            user.setName(name);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPicture(picture);
            return userRepository.save(user);
        }

        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDeleted(false);
        user.setPicture(picture);
        return userRepository.save(user);
    }

    public String generateJwtToken(User user) {
        return jwtConfig.generateToken(user);
    }
}