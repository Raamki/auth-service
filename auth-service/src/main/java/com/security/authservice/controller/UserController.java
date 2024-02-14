package com.security.authservice.controller;

import com.security.authservice.dto.request.PasswordRequest;
import com.security.authservice.dto.request.PermissionRequest;
import com.security.authservice.dto.request.RoleRequest;
import com.security.authservice.dto.request.UserRequest;
import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserManager userManager;

    @GetMapping("/{username}")
    public ResponseEntity getUser(@PathVariable String username){
        UserResponse userResponse = userManager.getUser(username);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addUser",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity addUser(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userManager.addUser(userRequest);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addGuestUser")
    public ResponseEntity addGuestUser(){
        UserResponse userResponse = userManager.addGuestUser();
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/modifyUserRole",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity modifyUserRole(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userManager.modifyUserRole(userRequest);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/modifyPassword",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity modifyPassword(@RequestBody @Valid PasswordRequest request){
        UserResponse userResponse = userManager.updatePassword(request);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/role",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity addRole(@RequestBody @Valid RoleRequest roleRequest){
        UserResponse userResponse = userManager.addRole(roleRequest);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/role",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity modifyRole(@RequestBody @Valid RoleRequest roleRequest){
        UserResponse userResponse = userManager.modifyRolePermission(roleRequest);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/permission",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity addPermission(@RequestBody @Valid PermissionRequest request){
        UserResponse userResponse = userManager.addPermission(request);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/permission",  consumes = {MediaType.ALL_VALUE})
    public ResponseEntity modifyPermission(@RequestBody @Valid PermissionRequest request){
        UserResponse userResponse = userManager.modifyPermission(request);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/activate/{username}")
    public ResponseEntity activateUser(@PathVariable String username){
        UserResponse userResponse = userManager.activateUser(username);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/deactivate/{username}")
    public ResponseEntity deactivateUser(@PathVariable String username){
        UserResponse userResponse = userManager.deActivateUser(username);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "role/activate/{role}")
    public ResponseEntity activateRole(@PathVariable String role){
        UserResponse userResponse = userManager.activateRole(role);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "role/deactivate/{role}")
    public ResponseEntity deactivateRole(@PathVariable String role){
        UserResponse userResponse = userManager.deActivateRole(role);
        if (userResponse.getStatus().getCode() == 200){
            return new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
