package com.presidential.elections.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.presidential.elections.Entities.UserVotes;
import com.presidential.elections.Entities.User;
import com.presidential.elections.Repository.VoteRepository;
import com.presidential.elections.Repository.CandidateRepository;
import com.presidential.elections.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    private LocalDate localDate = LocalDate.now();
    private String date = localDate.toString();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByusername(username).orElseThrow(() -> new UsernameNotFoundException("the user is not found"));
    }
    
    public User registerUser(String Username, String Password) {
        if (Username == null && Password == null || Username == null || Password == null) {
           return null;
        }
        else {
		    User user = new User(0, Username, Password);
            user.setNumberVotes(0);
            return userRepository.save(user);
        }
    }

    public User saveUserProfileSetting(String Username, String familyName, String surname, String address, String aboutMe, String city, String state, String zip) {
        Optional<User> optionalUser = userRepository.findByusername(Username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFamily_name(familyName);
            user.setSurname(surname);
            user.setAbout_me(aboutMe);
            user.setAddress(address);
            user.setCity(city);
            user.setState(state);
            user.setZip(zip);
            return userRepository.save(user);
        }
        return null; // Or throw an exception, handle the case where the user does not exist.
    }
    
    public User SaveLinkPhoto(String Username, String LinkPhoto) {
        Optional<User> optionalUser = userRepository.findByusername(Username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLink_photo(LinkPhoto);
            return userRepository.save(user);
        }
        return null;
    }

    public String getUsernameAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return "The user is not authenticated";
    }

    public User saveVotesNumberInUserTableBySelectecUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findByusername(getUsernameAuthenticated());
        Optional<User> votedUser = userRepository.findByuserid(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getUser_id() != userId) {
                if (voteRepository.findById(user.getUser_id()).isEmpty()) {
                    if (votedUser.isPresent()) {
                        User user2 = votedUser.get();
                        user2.setNumberVotes(user2.getNumberVotes() + 1);
                        userRepository.save(user2);
                    }
                }
            }
        }
        return null;
    }

    public UserVotes saveUserVoteDetails(Integer votedUserId) {
        Optional<User> optionalUser = userRepository.findByusername(getUsernameAuthenticated());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();   
            Integer user_id = user.getUser_id();
            Optional<UserVotes> optionalUserVotes = voteRepository.findById(user.getUser_id());
            if (user_id != votedUserId) {
                if (optionalUserVotes.isEmpty()) {
                    UserVotes userVotes = new UserVotes(user_id, votedUserId, date);
                    voteRepository.save(userVotes);
                }
            }
        }
        return null;
    }

    public List<User> getUsersWithCandidates() {
        return candidateRepository.findUsersWithCandidates();
    }
    
    public boolean isAdmin() {
        Optional<User> optionalUser = userRepository.findByusername(getUsernameAuthenticated());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userRole = user.getRole();
            if (userRole != null && userRole.equalsIgnoreCase("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
