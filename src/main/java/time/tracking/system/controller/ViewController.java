package time.tracking.system.controller;

import time.tracking.system.model.User;
import time.tracking.system.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@Controller
public class ViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }


    @GetMapping("/dashboard/admin")
    public String adminDash(Principal principal, Model m) {
        m.addAttribute("username", principal.getName());
        return "admin-dashboard";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }


    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user, HttpServletRequest request) {
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        String role = String.valueOf(user.getRole());
        System.out.println("role this is "+role);
        if ("ADMIN".equalsIgnoreCase(role)) return "redirect:/dashboard/admin";
        if ("WORKER".equalsIgnoreCase(role)) return "redirect:/worker/dashboard";
        if ("ACCOUNTANT".equalsIgnoreCase(role)) return "redirect:/dashboard/accountant";
        return "redirect:/";
    }

}