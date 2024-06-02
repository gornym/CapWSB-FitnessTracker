package com.capgemini.wsb.fitnesstracker.user.internal;


import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management System")
class UserController {

    private final UserServiceImpl userService;
    private final TrainingServiceImpl trainingService;
    private final UserMapper userMapper;

    @Operation(summary = "View all users")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
        .stream()
        .map(userMapper::toDto)
        .toList();
    }

    @Operation(summary = "Viewuser with simple information")
    @GetMapping("/simple")
    public List<BasicUserDto> getAllBasicUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::basicToDto)
                .toList();
    }

    @Operation(summary = "View specific user detils")
    @GetMapping("/{id}")
    public UserDto getSingleUserById(@PathVariable Long id) {
        return userService.getUser(id).map(userMapper::toDto).orElseThrow();
    }

    @Operation(summary = "Create new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserDto userDto) throws InterruptedException {
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");
        User user = userMapper.toEntity(userDto);
        return userService.createUser(user);
    }

    @Operation(summary = "Delete specific user")
    @DeleteMapping("/{userid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userid){
        trainingService.deleteTrainingByUserId(userid);
        userService.deleteUser(userid);
    }

    @Operation(summary = "View list of users with partial email")
    @GetMapping("/email/")
    public List<BasicUserDtoEmail> getUsersByPartialEmail(@RequestParam String email) {
        return userService.getUserByPartialEmail(email).stream().map(userMapper::basicToDtoEmail).toList();
    }

    @Operation(summary = "View list of users older than specific age")
    @GetMapping("/older/{time}")
    public List<UserDto> getUserOlderThan(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        return userService.searchUsersByAgeGreaterThan(time)
            .stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }
}