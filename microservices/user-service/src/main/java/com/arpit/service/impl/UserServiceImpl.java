package com.arpit.service.impl;

import com.arpit.exception.UserException;
import com.arpit.modal.User;
import com.arpit.payload.dto.KeycloakUserDTO;
import com.arpit.repository.UserRepository;
import com.arpit.service.KeycloakService;
import com.arpit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isPresent()){
            return otp.get();
        }
        throw new UserException("User not found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isEmpty()){
            throw new UserException("User not exist with id " + id);
        }
        userRepository.deleteById(otp.get().getId());
    }

    @Override
    public User updateUser(Long id, User user) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isEmpty()){
            throw new UserException("User not found");
        }

        User existingUser = otp.get();

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());
        return userRepository.save(existingUser);
    }

    @Override
    public User getUserFromJwt(String jwt) throws Exception {

        KeycloakUserDTO keycloakUserDTO = keycloakService.fetchUserProfileByJwt(jwt);

        System.out.println("Keycloak email: [" + keycloakUserDTO.getEmail() + "]");

        User user = userRepository.findByEmail(keycloakUserDTO.getEmail());

        if (user == null) {
            throw new UserException("No local user found for email: " + keycloakUserDTO.getEmail());
        }

        return user;
    }
}