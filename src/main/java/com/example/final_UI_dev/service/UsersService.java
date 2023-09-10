package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.Cart;
import com.example.final_UI_dev.entity.Tokens;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.CartRepository;
import com.example.final_UI_dev.repository.TokenRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;
import java.util.Date;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.webjars.NotFoundException;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenRepository tokenRepository;

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Autowired
    private CartRepository cartRepository;

    public Users updateUserEmail(int userId, String newEmail) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        user.setEmail(newEmail);
        return usersRepository.save(user);
    }

    public ResponseEntity<?> saveNewUser(Users userEntity) {
        Users existingUser = usersRepository.findByEmailIgnoreCase(userEntity.getEmail());
        if (existingUser != null) {
            return ResponseEntity.ok("This email already exists!");

        } else {
            userEntity.setRoles("ROLE_CUSTOMER");
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            usersRepository.save(userEntity);
            Users user = usersRepository.findByEmailIgnoreCase(userEntity.getEmail());
         /*   Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);*/
            Tokens tokens=new Tokens();
            tokens.setUser(user);
            tokenRepository.save(tokens);


            //  ConfirmationToken = new ConfirmationToken(userEntity);
            //  confirmationTokenRepository.save(confirmationToken);

           // SimpleMailMessage mailMessage = new SimpleMailMessage();
           // mailMessage.setTo(userEntity.getEmail());
           // mailMessage.setSubject("Complete Registration!");
           // mailMessage.setText("To confirm your account, please click here : "
           //         + "http://localhost:8080/categories");
          //  emailService.sendEmail(mailMessage);
        }
        return ResponseEntity.ok("Registration successful!");

    }


    public void generateOneTimePassword(Users user)
            throws UnsupportedEncodingException, MessagingException {
        // String OTP = RandomString.make(8);
        Random random = new Random();
        int otpValue = random.nextInt(900000) + 100000; // Generates a random number between 100000 and 999999
        String OTP = String.valueOf(otpValue);
        String encodedOTP = passwordEncoder.encode(OTP);
        clearOTP(user);
        user.setOneTimePassword(encodedOTP);
        user.setOtpRequestedTime(new Date());

        usersRepository.save(user);

        sendOTPEmail(user, OTP);
    }

    public void sendOTPEmail(Users customer, String OTP)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@shopme.com", "Bazaar Support");
        helper.setTo(customer.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = "<p>Hello " + customer.getEmail() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to reset your password:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void clearOTP(Users customer) {
        customer.setOneTimePassword(null);
        customer.setOtpRequestedTime(null);
        usersRepository.save(customer);
    }

    public boolean verifyOTP(Users user, String enteredOTP) {
        String storedOTP = user.getOneTimePassword();
        System.out.println(enteredOTP);
        System.out.println(storedOTP);
        System.out.println(passwordEncoder.encode(enteredOTP));
        System.out.println(passwordEncoder.encode(enteredOTP) == storedOTP);
        return passwordEncoder.matches(enteredOTP, storedOTP);
    }


    public boolean resetPassword(Users user,String newPassword)
            throws UnsupportedEncodingException, MessagingException {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            usersRepository.save(user);
            return true;

    }
/*
    public boolean changePassword(String email, String old, String nw)
            throws UnsupportedEncodingException, MessagingException {
        Optional<Users> user = usersRepository.findByEmail(email);
        if (user.isPresent()) {
            String storedpass = user.get().getPassword();
            if(passwordEncoder.matches(storedpass,old)){
                System.out.println(passwordEncoder.matches(storedpass,old));
                String enc=passwordEncoder.encode(nw);
                user.get().setPassword(enc);
                usersRepository.save(user.get());
                return true;
            }
            else
                return false;

            }


        return false;
    }

 */
public boolean changePassword(String email, String old, String nw)
        throws UnsupportedEncodingException, MessagingException {
    Optional<Users> optionalUser = usersRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
        Users user = optionalUser.get();
        String storedPass = user.getPassword();

        if (passwordEncoder.matches(old, storedPass)) {
            String encodedNewPassword = passwordEncoder.encode(nw);
            user.setPassword(encodedNewPassword);
            usersRepository.save(user);
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}


}
