package com.web.springmvc.web_tin_tuc.service;

//import com.web.springmvc.newsweb.model.UserDetailsImpl;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.exception.UserAlreadyExistsException;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.Role;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserDTO createUser(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exist");
        }
        userDTO.setStatus(1);
        User user = mapToEntity(userDTO);
        userRepository.save(user);
        return userDTO;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()-> new UserNotFoundException("Not found user"));
        user.setPassword(userDTO.getPassword());
        user.setUsername(passwordEncoder.encode(userDTO.getUsername()));
        user.setStatus(userDTO.getStatus());
        userRepository.save(user);
        return userDTO;
    }

    public UserDTO register(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.USER);
        user.setStatus(1);
        userRepository.save(user);
        return mapToDTO(user);
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Not found user"));
        return mapToDTO(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private User mapToEntity(UserDTO userDTO) {
        User user = new User();
        if(userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }
        user.setUsername(userDTO.getUsername());
        if(userDTO.getRole().equals("USER")) {
            user.setRole(Role.USER);
        } else {
            user.setRole(Role.ADMIN);
        }
        user.setStatus(userDTO.getStatus());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setStatus(user.getStatus());
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }

}
