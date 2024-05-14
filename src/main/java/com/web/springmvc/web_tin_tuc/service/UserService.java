package com.web.springmvc.web_tin_tuc.service;

//import com.web.springmvc.newsweb.model.UserDetailsImpl;

import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.ConfirmationToken;
import com.web.springmvc.web_tin_tuc.model.Role;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.ConfirmationTokenRepository;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final FileUpload fileUploadUtils;
    public void register(User userRegister) {
        User user = new User();
        user.setUsername(userRegister.getUsername());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setEmail(userRegister.getEmail());
        user.setRole(Role.USER);
        // TODO: Generate confirmationToken
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );
        userRepository.save(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

//        TODO: Send email to user with token
        emailService.sendHtmlMessage(user.getUsername(), user.getEmail(), confirmationToken.getToken());
    }


    public void verifyToken(ConfirmationToken confirmationToken) {
        User user = userRepository.findByEmail(confirmationToken.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) return null;
        return mapToDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) return null;
        return mapToDTO(user);
    }

    public void updateUser(UserDTO userDTO) throws IOException {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()-> new UserNotFoundException("Not found user"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if(!userDTO.getPhoto().isEmpty()) {
            fileUploadUtils.uploadImage(userDTO.getPhoto(), user.getId().toString());
            user.setPhotoUrl("/assets/images/"+ user.getId().toString()+"/"+ StringUtils.cleanPath(userDTO.getPhoto().getOriginalFilename()));
        }
        userRepository.save(user);
    }


    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
    public UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole().name());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhotoUrl(user.getPhotoUrl());
        return userDTO;
    }

}
