package com.presidential.elections.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.presidential.elections.Entities.CandidateRounds;
import com.presidential.elections.Entities.ClassmentHistory;
import com.presidential.elections.Entities.User;
import com.presidential.elections.Entities.UserCandidate;
import com.presidential.elections.Repository.CandidateRepository;
import com.presidential.elections.Repository.CandidateRoundsRepository;
import com.presidential.elections.Repository.ClassmentHistoryRepository;
import com.presidential.elections.Repository.UserRepository;
import com.presidential.elections.Repository.VoteRepository;
import com.presidential.elections.Service.UserService;

@Controller
public class ElectionController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    CandidateRoundsRepository candidateRoundsRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ClassmentHistoryRepository classmentHistoryRepository;

    private LocalDate localDate = LocalDate.now();
    private String date = localDate.toString();

    @GetMapping("/")
    public String mainPage(Model model) {
        Optional<User> optionalUser = userRepository.findByusername(userService.getUsernameAuthenticated());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("link_photo", user.getLink_photo());
            model.addAttribute("candidate_name", user.getUsername());
            List<User> usersWithCandidates = userService.getUsersWithCandidates();
            List<User> sortedList = user.sortUsersByVotes(usersWithCandidates);
            model.addAttribute("usersWithCandidates", sortedList);
            return "MainPage";
        }
        return "The user is not authenticated";
    }

    @PostMapping("/CandidateProfile")
    public String seeProfile(Model model, @RequestParam("userid") Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("link_photo", user.getLink_photo());
            model.addAttribute("family_name_id", user.getFamily_name());
            model.addAttribute("surname_id", user.getSurname());
            model.addAttribute("about_me_id", user.getAbout_me());
            model.addAttribute("address_id", user.getAddress());
            model.addAttribute("city_id", user.getCity());
            model.addAttribute("state_id", user.getState());
            model.addAttribute("zip_id", user.getZip());
            model.addAttribute("link_photo", user.getLink_photo());
            return "ProfilePage";
        }
        return "The user is not authenticated";
    }
    
    @PostMapping("/vote")
    public String UpdateVoteSystem(@RequestParam("userid") Integer userId) {
        Integer lastRoundSaved = candidateRoundsRepository.findMaxId();
        Optional<CandidateRounds> candidateRounds = candidateRoundsRepository.findById(lastRoundSaved);
        if (candidateRounds.isPresent()) {
            CandidateRounds candidate = candidateRounds.get();
            if (candidate.getEnd_datetime() == null) {
                userService.saveVotesNumberInUserTableBySelectecUser(userId);  
                userService.saveUserVoteDetails(userId);  
            }
        }
        return "redirect:/";
    }

    @GetMapping("/candidate")
    public String UpdateCandidateColumn() {
        Optional<User> optionalUser = userRepository.findByusername(userService.getUsernameAuthenticated());
        Integer lastRoundSaved = candidateRoundsRepository.findMaxId();
        if (lastRoundSaved != null) {
            Optional<CandidateRounds> candidateRounds = candidateRoundsRepository.findById(lastRoundSaved);
            if (candidateRounds.isPresent()) {
                CandidateRounds candidate = candidateRounds.get();
                if (candidate.getEnd_datetime() == null) {
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        Integer userId = user.getUser_id();
                        UserCandidate userCandidate = new UserCandidate(userId);
                        candidateRepository.save(userCandidate);
                    }
                }
            }
            
        }else if (lastRoundSaved == null) {
            CandidateRounds candidateRounds2 = new CandidateRounds(1, date, null);
            candidateRoundsRepository.save(candidateRounds2);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Integer userId = user.getUser_id();
                UserCandidate userCandidate = new UserCandidate(userId);
                candidateRepository.save(userCandidate);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/startRound")
    public String startRound() {
        Integer lastRoundSaved = candidateRoundsRepository.findMaxId();
        if (lastRoundSaved != null) {
            CandidateRounds candidateRounds = new CandidateRounds(lastRoundSaved += 1, date, null);
            candidateRoundsRepository.save(candidateRounds);
        }
        List<UserCandidate> userCandidates = candidateRepository.findAll();
        for (UserCandidate userCandidate : userCandidates) {
            Integer userId = userCandidate.getCandidateId();
            Optional<User> optionalUser = userRepository.findByuserid(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setNumberVotes(0);
                userRepository.save(user);
            }
        }
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
        return "redirect:/";
    }

    @GetMapping("/stopRound")
    public String stopRound() {
        Integer lastRoundSaved = candidateRoundsRepository.findMaxId();
        List<UserCandidate> userCandidates = candidateRepository.findAll();
        for (UserCandidate userCandidate : userCandidates) {
            Integer userId = userCandidate.getCandidateId();
            Optional<User> optionalUser = userRepository.findByuserid(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                ClassmentHistory classmentHistory = new ClassmentHistory(lastRoundSaved, userId, user.getNumberVotes());
                classmentHistoryRepository.save(classmentHistory);
            }
        }
        Optional<CandidateRounds> candidateRound = candidateRoundsRepository.findById(lastRoundSaved);
        if (candidateRound.isPresent()) {
            CandidateRounds candidateRounds = candidateRound.get();
            candidateRounds.setEnd_datetime(date);
            candidateRoundsRepository.save(candidateRounds);
        }
        return "redirect:/";
    }
}
