package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.*;
import com.example.final_UI_dev.repository.TokenRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import com.example.final_UI_dev.service.JwtService;
import com.example.final_UI_dev.service.UsersService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class
UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TokenRepository tokenRepository;


    @PatchMapping("/{userId}")
    public ResponseEntity<Users> updateUserEmail(@PathVariable int userId, @RequestParam String newEmail) {
        Users updatedUser = usersService.updateUserEmail(userId, newEmail);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        try {
            List<Users> users = usersService.getAllUsers();
            return ResponseEntity.ok(users);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateAndGetToken(@RequestBody LoginRequest request) {
        try {
            System.out.println(request);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getFirstname(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(request.getFirstname());
                Optional<Users> use=usersRepository.findByEmail(request.getFirstname());
                if(use.isPresent()) {
                    Optional<Tokens> tok = tokenRepository.getTokenByUser(use.get());
                    if(tok.isPresent()) {
                        tok.get().setToken(token);
                        tok.get().setCreation(new Date());
                        tokenRepository.save(tok.get());
                        System.out.println(token);

                    }
                    else
                    {
                        Tokens tokens=new Tokens();
                        tokens.setUser(use.get());
                        tokens.setToken(token);
                        tokens.setCreation(new Date());
                        tokenRepository.save(tokens);
                        System.out.println(token);


                    }
                }
                LoginResponse response = new LoginResponse(token,use.get().getId());
                return ResponseEntity.ok(response);

            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword) {
       Users user = usersRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            // Handle user not found error
        } else {
            try {
               if(usersService.resetPassword(user,newPassword))
                return ResponseEntity.ok("Password reset successful");
                else
                    return ResponseEntity.ok("failed");
            } catch (UnsupportedEncodingException | MessagingException e) {
                // Handle email sending or other errors
            }
        }

        // Handle any other errors and return an appropriate response
        return null;
    }
    /*
    @GetMapping("/verify")
    public ResponseEntity<?> verifyotp(@RequestBody otpverify otpv){
        String otp=otpv.getOtp();
        Optional<Users> user=usersRepository.findByEmail(otpv.getEmail());
        if(user.isPresent()) {
            boolean response = usersService.verifyOTP(user.get(),otp);
            if(response){
                return ResponseEntity.ok(true);
            }
            else
                return ResponseEntity.ok(false);
        }
        else
            return ResponseEntity.ok(false);

    }

     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam("otp") String otp,
                                       @RequestParam("email") String email){
        Optional<Users> user=usersRepository.findByEmail(email);
        if(user.isPresent()) {
            boolean response = usersService.verifyOTP(user.get(),otp);
            if(response){
                return ResponseEntity.ok(true);
            }
            else
                return ResponseEntity.ok(false);
        }
        else
            return ResponseEntity.ok(false);

    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> otp(@RequestParam("email") String email){
        try {
            Users user = usersRepository.findByEmailIgnoreCase(email);

            if (user == null) {
                // Handle user not found error
                return ResponseEntity.badRequest().body("Email does not exist!");
            } else {
                try {
                    usersService.generateOneTimePassword(user);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    // Handle email sending or other errors
                    return ResponseEntity.internalServerError().body("could not serve your request");
                }
            }
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("User with his email doesnot exist");
        }
        return ResponseEntity.ok("OTP sent");

        }



    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody Users users){
        usersService.saveNewUser(users);
      // return ResponseEntity.ok("Signup successfull");
        return ResponseEntity.ok().body("{\"message\": \"Signup successful\"}");
    }
    /*
@GetMapping("/expire")
public ResponseEntity<?> expiretime(@RequestBody Token token){
        String tok=token.getToken();
       Date response= jwtService.extractExpiration(tok);
       return ResponseEntity.ok(response);
}
*/
    @GetMapping("/expire")
    public ResponseEntity<?> expireTime(@RequestBody Token token) {
        String tok = token.getToken();
        try {
            boolean expirationDate = jwtService.isTokenExpired(tok);
            return ResponseEntity.ok(expirationDate);
        }
        catch (Exception e){
            return ResponseEntity.ok(true);
        }
    }
@PostMapping("/change")
    public ResponseEntity<?> changepassword(
        @RequestParam("email") String email,
        @RequestParam("old") String old,
        @RequestParam("nw") String nw) throws MessagingException, UnsupportedEncodingException {
       if(usersService.changePassword(email,old,nw))
          return  ResponseEntity.ok("Password changed successfully");
       else
          return ResponseEntity.badRequest().body("Either email or password is incorrect");
}
}

