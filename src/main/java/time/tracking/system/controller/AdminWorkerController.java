package time.tracking.system.controller;


import time.tracking.system.model.User;
import time.tracking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/workers")
public class AdminWorkerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String listWorkers(Model model) {
        // Fetch users with the role "WORKER"
        List<User> workers = userRepository.findByRole("WORKER");
        model.addAttribute("workers", workers);
        return "admin/workers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/worker_add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/worker_add";
        }
        user.setRole("WORKER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/workers";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "admin/worker_form";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User formUser,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "admin/worker_form";
        }


        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        dbUser.setName(formUser.getName());
        dbUser.setSurname(formUser.getSurname());
        dbUser.setHourlyRate(formUser.getHourlyRate());
        dbUser.setStatus(formUser.getStatus());

        userRepository.save(dbUser);

        return "redirect:/admin/workers";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/workers";
    }
}
