package com.app.ecommere.controller;

import com.app.ecommere.model.UserDTO;
import com.app.ecommere.payload.response.UserResponse;
import com.app.ecommere.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.app.ecommere.utils.AppConstants.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public UserResponse getAllUser(@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                   @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                   @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return userService.getAllUser(pageNo,pageSize,sortBy,sortDir);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getPostById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updatePost(@Valid @RequestBody UserDTO userDTO, @PathVariable(name = "id") Integer id) {
        UserDTO postResponse = userService.updateUser(userDTO, id);

        return new ResponseEntity<>(postResponse, OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Integer id) {
        userService.deleteUserById(id);

        return new ResponseEntity<>("User entity deleted successfully", OK);
    }
}
