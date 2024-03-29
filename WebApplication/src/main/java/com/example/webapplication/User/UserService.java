package com.example.webapplication.User;

import com.example.webapplication.Administrator.AdminRepository;
import com.example.webapplication.Message.Message;
import com.example.webapplication.Message.messageDTO;
import com.example.webapplication.Role.Role;
import com.example.webapplication.Role.RoleRepository;
import com.example.webapplication.WebConfiguration.AuthenticatedUser;
import com.example.webapplication.WebConfiguration.JWTs.JWTutils;
import com.example.webapplication.WebConfiguration.JWTs.JwtResponse;
import com.example.webapplication.WebConfiguration.RefreshToken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

/*
* (1): Since we want to implement the D.I. pattern , we have to instantiate ( with anotation: @Service) the UserService class since we pass
* a refrence of this class to UserController constructor!
*/

/* The service layer uses the repository interface to retrive data from tha database!*/
@Service // (1)
public class UserService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    JWTutils jwTutils;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       BCryptPasswordEncoder passwordEncoder , RoleRepository roleRepository,
                       AdminRepository adminRepository,RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.adminRepository=adminRepository;
        this.refreshTokenService=refreshTokenService;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /* Save a specific User to the data base! */
    public User registerUserToBase(User userForRegistration){
        return userRepository.save(userForRegistration);
    }

    public Optional<User>  getUserByUserName(String userName){
        return userRepository.findByUsername(userName);
    }

    public ResponseEntity<?> login(String userName,String userPassword) {
        try {
            System.out.println(userName);
            Optional<User> user= userRepository.findByUsername(userName);
            if (!(user.isPresent()))
                return new ResponseEntity<>("There is no such user", HttpStatus.BAD_REQUEST);
            if(!user.get().isRegistered())
                return new ResponseEntity<>("Administrator has to accept this user first...", HttpStatus.BAD_REQUEST);

//            System.out.println(user.get().toString());
            System.out.println(userName);
            System.out.println(userPassword);

            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();

            String jwt = jwTutils.generateJwtToken(userDetails);

            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

            List<String> roles = authenticatedUser.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, authenticatedUser.getUsername(), authenticatedUser.getEmail(), roles,refreshToken.getToken()));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> signUp(@RequestBody User user){

        // add check for username exists in a DB
        if(userRepository.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        // add check for email exists in DB
        if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        //first we need to convert the password to a bcrypt password type!
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /* Assign permissions to new user!*/
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER");
        Role sellerRole = roleRepository.findByName("SELLER");
        Role bidderRole = roleRepository.findByName("BIDDER");
        roles.add(bidderRole);
        roles.add(sellerRole);
        roles.add(userRole);
        user.setRoles(roles);

        // register User to database!
        userRepository.save(user);

        return new ResponseEntity<>("This registration request needs to be authenticated first by the administrator!", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAllUsers(){
        userRepository.deleteAll();
        return new ResponseEntity<>("All users have been deleted!", HttpStatus.OK);
    }


    public ResponseEntity<?> refreshtoken(TokenRefreshRequest request) {

        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwTutils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public ResponseEntity<?> getUserId(String userName){
//        System.out.println(userRepository.findByUsername(userName).get().getUserId());
        Optional<User> user = userRepository.findByUsername(userName);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get().getUserId(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getInbox(Long id){
        Set<Message> messages = new HashSet<>();
        messages = userRepository.findById(id).get().getInbox();
        List<messageDTO> newMessages = new ArrayList<>();
        for(Message i:messages ){
            messageDTO newMessage = new messageDTO(i.getBackupSender(), i.getBackupReceiver(), i.getMessage(), i.getDateTime());
            newMessages.add(newMessage);
        }
        return new ResponseEntity<>(newMessages, HttpStatus.OK);
    }

    public ResponseEntity<?> getOutbox(Long id){
        Set<Message> messages = new HashSet<>();
        messages = userRepository.findById(id).get().getOutbox();
        List<messageDTO> newMessages = new ArrayList<>();
        for(Message i:messages ){
            messageDTO newMessage = new messageDTO(i.getBackupSender(), i.getBackupReceiver(), i.getMessage(), i.getDateTime());
            newMessages.add(newMessage);
        }
        return new ResponseEntity<>(newMessages, HttpStatus.OK);
    }

}
