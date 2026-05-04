package ch.kenner.maximilian.smartreserve.controller;

import ch.kenner.maximilian.smartreserve.model.User;
import ch.kenner.maximilian.smartreserve.model.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User syncUserWithKeycloak(Jwt jwt) {
        // The 'sub' claim is the unique Keycloak user ID
        String keycloakId = jwt.getSubject();

        // Extract the email claim (ensure 'email' scope is enabled in Keycloak client)
        String email = jwt.getClaimAsString("email");

        // Extract preferred username (optional)
        String username = jwt.getClaimAsString("preferred_username");

        // Check if user exists, if not create them. If they do, update their info.
        return userRepository.findById(keycloakId).map(existingUser -> {
            // Update email if they changed it in Keycloak
            if (!existingUser.getEmail().equals(email)) {
                existingUser.setEmail(email);
                existingUser.setUsername(username);
                return userRepository.save(existingUser);
            }
            return existingUser;
        }).orElseGet(() -> {
            // Create brand new user record locally
            User newUser = new User(keycloakId, username, email);
            return userRepository.save(newUser);
        });
    }
}