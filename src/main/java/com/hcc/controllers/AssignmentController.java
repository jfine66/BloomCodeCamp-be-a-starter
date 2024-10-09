package com.hcc.controllers;

import com.hcc.entities.Assignment;
import com.hcc.entities.User;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.repositories.AssignmentRepository;
import com.hcc.repositories.UserRepository;
import com.hcc.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @GetMapping
    public List<Assignment> getAssignmentsByUser(@RequestBody User user) {
        User userFromDb = (User) userDetailService.loadUserByUsername(user.getUsername());
        return assignmentRepository.findByUser(userFromDb);
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find Assignment with that id"));
    }

    @PutMapping("/{id}")
    public void updateAssignmentById(@PathVariable int id, @RequestBody Assignment assignmentPassIn) {
        Assignment assignment = getAssignmentById(id);
        User user = (User) userDetailService.loadUserByUsername(assignmentPassIn.getUser().getUsername());

        assignment.setStatus(assignmentPassIn.getStatus());
        assignment.setNumber(assignmentPassIn.getNumber());
        assignment.setGithubUrl(assignmentPassIn.getGithubUrl());
        assignment.setBranch(assignmentPassIn.getBranch());
        assignment.setReviewVideoUrl(assignmentPassIn.getReviewVideoUrl());
        assignment.setUser(user);
        assignment.setCodeReviewer(assignmentPassIn.getCodeReviewer());

        assignmentRepository.save(assignment);
    }

    @PostMapping
    public void createNewAssignment(@RequestBody Assignment assignment) {
        User user = (User) userDetailService.loadUserByUsername(assignment.getUser().getUsername());
        assignment.setUser(user);

        assignmentRepository.save(assignment);
    }
}
