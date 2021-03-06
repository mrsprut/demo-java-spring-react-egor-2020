package org.tyaa.demo.spring.springreactmysqlmongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.RoleModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.UserModel;
import org.tyaa.demo.spring.springreactmysqlmongo.services.AuthService;
import org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.IAuthService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Получение ссылки на пользовательскую службу безопасности
    @Autowired
    private IAuthService authService;

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/roles")
    public ResponseEntity<ResponseModel> getAllRoles() {
        return new ResponseEntity<>(authService.getAllRoles(), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/role")
    public ResponseEntity<ResponseModel> createRole(@RequestBody RoleModel roleModel) {
        return new ResponseEntity<>(authService.createRole(roleModel), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/admin/role/{id}")
    public ResponseEntity<ResponseModel> deleteRole(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteRole(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/role/{id}/users")
    public ResponseEntity<ResponseModel> getUsersByRole(@PathVariable Long id) {
        ResponseModel responseModel =
                authService.getRoleUsers(id);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                        ? HttpStatus.OK
                        : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseModel> createUser(@RequestBody UserModel userModel) {
        ResponseModel responseModel =
                authService.createUser(userModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : responseModel.getMessage().contains("name")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY
        );
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteUser(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/user/check")
    @ResponseBody
    public ResponseEntity<ResponseModel> checkUser(Authentication authentication) {
        ResponseModel responseModel = authService.check(authentication);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                        ? HttpStatus.OK
                        : HttpStatus.UNAUTHORIZED
        );
    }

    @GetMapping("/user/signedout")
    public ResponseEntity<ResponseModel> signedOut(HttpSession httpSession) {
        return new ResponseEntity<>(authService.onSignOut(), HttpStatus.OK);
    }

    @GetMapping("/user/onerror")
    public ResponseEntity<ResponseModel> onError() {
        return new ResponseEntity<>(authService.onError(), HttpStatus.UNAUTHORIZED);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping(value = "/user/{id}/makeadmin")
    public ResponseEntity<ResponseModel> makeUserAdmin(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(authService.makeUserAdmin(id), HttpStatus.OK);
    }
}
