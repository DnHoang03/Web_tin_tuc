package com.web.springmvc.web_tin_tuc.service;

//import com.web.springmvc.newsweb.model.UserDetailsImpl;

import com.web.springmvc.web_tin_tuc.dto.*;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.ConfirmationToken;
import com.web.springmvc.web_tin_tuc.model.Role;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        user.setFirstName(userRegister.getFirstName());
        user.setLastName(userRegister.getLastName());
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
        emailService.sendHtmlMessage(user.getUsername(), user.getEmail(), confirmationToken.getToken(), "auth/email-confirmation");
    }

    public void sendPasswordToken(ForgotPasswordRequest forgotPasswordRequest, Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Not found user"));
        ConfirmationToken confirmationTokenCheck = confirmationTokenService.getTokenByUserId(id);
        if(confirmationTokenCheck != null) {
            if(confirmationTokenCheck.getConfirmedAt() != null) {
                confirmationTokenService.deleteTokenById(confirmationTokenCheck.getId());
            } else {
                emailService.sendResetPasswordMessage(user.getUsername(), user.getEmail(), confirmationTokenCheck.getToken(), "auth/email-confirmation", user.getId());
                return;
            }
        }
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailService.sendResetPasswordMessage(user.getUsername(), user.getEmail(), confirmationToken.getToken(), "email-confirmation", user.getId());
    }

    public void verifyConfirmationToken(ConfirmationToken confirmationToken) {
        User user = userRepository.findByEmail(confirmationToken.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void verifyPasswordResetToken(ConfirmationToken confirmationToken, PasswordResetRequest request) {
        User user = userRepository.findByEmail(confirmationToken.getUser().getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request, Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Not found user"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
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

    public ListRespone<UserDTO> getAllUser(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> users = usersPage.getContent();
        //Map a list<news> to list<newsDTO>
        List<UserDTO> userDTOS = users.stream().map(this::mapToDTO).toList();
        ListRespone<UserDTO> listRespone = new ListRespone<UserDTO>();
        listRespone.setContent(userDTOS);
        listRespone.setPageNumber(usersPage.getNumber());
        listRespone.setPageSize(usersPage.getSize());
        listRespone.setTotalElement(usersPage.getTotalElements());
        listRespone.setTotalPage(usersPage.getTotalPages());
        listRespone.setLast(usersPage.isLast());
        return listRespone;
    }

    public Integer getTotalUser() {
        return (int)userRepository.count();
    }

    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()-> new UserNotFoundException("Not found user"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if(!userDTO.getPhoto().isEmpty()) {
            fileUploadUtils.uploadImage(userDTO.getPhoto(), user.getId().toString());
            user.setPhotoUrl("/assets/images/"+ user.getId().toString()+"/"+ StringUtils.cleanPath(userDTO.getPhoto().getOriginalFilename()));
        }
        userRepository.save(user);
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
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

    public boolean confirmOldPassword(String password, Integer id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Not found user"));
        return passwordEncoder.matches(password, user.getPassword());
    }
    public UserDTO getUserById(Integer id) {
        return mapToDTO(userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Not found user")));
    }
}
