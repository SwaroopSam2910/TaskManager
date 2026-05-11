package com.example.product_backend.user.Service;

import com.example.product_backend.user.DTO.loginRequest;
import com.example.product_backend.user.DTO.RegisterRequest;
import com.example.product_backend.user.DTO.loginResponse;
import com.example.product_backend.user.ENUM.UserRole;
import com.example.product_backend.user.ENUM.UserStatus;
import com.example.product_backend.common.JWT.jwtUtils;
import com.example.product_backend.user.Repository.userRepo;
import com.example.product_backend.user.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class authService {

    @Autowired
    private userRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtils jwtUtils;

    public String registerUser(RegisterRequest request){

        Optional<user>exists = userRepo.findByEmail(request.getEmail());

        if(exists.isPresent()){
           throw new RuntimeException("User already exist");
        }

        user newUser = new user();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setStatus(UserStatus.PENDING);
        newUser.setRole(UserRole.USER);
        userRepo.save(newUser);

        return "Please wait for your approval from admin";
    }

    public loginResponse loginUser(loginRequest request){

        user loggedInUser = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("Invalid Username or Password"));

        if(!passwordEncoder.matches(request.getPassword(), loggedInUser.getPassword())){
            throw new RuntimeException("Invalid Username or Password");
        }

        if(loggedInUser.getStatus()!=UserStatus.ACTIVE){
            throw new RuntimeException("User not approved yet");
        }

        String token = jwtUtils.generateToken(
                loggedInUser.getId(),
                loggedInUser.getEmail(),
                loggedInUser.getRole().name()
        );
        return new loginResponse(token,loggedInUser.getRole().name());
    }
}
