package time.tracking.system.controller;

import time.tracking.system.model.Project;
import time.tracking.system.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/projects")
public class AdminProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "admin/projects";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new Project());
        return "admin/project_add";
    }

    @PostMapping("/add")
    public String addProject( @ModelAttribute Project project,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "admin/project_add";
        }
        projectRepository.save(project);
        return "redirect:/admin/projects";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        model.addAttribute("project", project);
        return "admin/project_form";
    }

    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id,
                                 @ModelAttribute Project project,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "admin/project_form";
        }
        project.setId(id);
        projectRepository.save(project);
        return "redirect:/admin/projects";
    }


    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/admin/projects";
    }
}
