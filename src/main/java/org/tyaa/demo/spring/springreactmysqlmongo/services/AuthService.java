package org.tyaa.demo.spring.springreactmysqlmongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Role;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.RoleModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.UserModel;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.RoleRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.IAuthService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService implements IAuthService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleDao;

    @Autowired
    private UserRepository userDao;

    @Override
    public ResponseModel createRole(RoleModel roleModel) {
        Role role = Role.builder().name(roleModel.name).build();
        roleDao.save(role);
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .message(String.format("%s Role Created", role.getName()))
            .build();
    }

    @Override
    public ResponseModel createUser(UserModel userModel) {
        User user =
            User.builder()
                .name(userModel.getName().trim())
                .password(passwordEncoder.encode(userModel.getPassword().trim()))
                .role(roleDao.findRoleByName("ROLE_USER"))
                .build();
        userDao.save(user);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("%s User Created", user.getName()))
                .build();
    }

    @Override
    public ResponseModel getAllRoles() {
        List<Role> roles =
                roleDao.findAll(Sort.by("name").ascending());
        List<RoleModel> roleModels =
            roles.stream()
                .map((r) -> RoleModel.builder()
                    .id(r.getId())
                    .name(r.getName())
                    .build())
                .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Role List Retrieved Successfully"))
                .data(roleModels)
                .build();
    }

    @Override
    @Transactional
    public ResponseModel getRoleUsers(Long roleId) {
        Optional<Role> roleOptional = roleDao.findById(roleId);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<UserModel> userModels =
                role.getUsers().stream().map(user ->
                    UserModel.builder()
                        .name(user.getName())
                        .roleId(user.getRole().getId())
                        .build()
                )
                .collect(Collectors.toList());
            return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("List of %s Role Users Retrieved Successfully", role.getName()))
                .data(userModels)
                .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("No Users: Role #%d Not Found", roleId))
                .build();
        }
    }

    @Override
    public ResponseModel deleteRole(Long id) {
        roleDao.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Role Deleted")
                .build();
    }

    @Override
    public ResponseModel deleteUser(Long id) {
        userDao.deleteById(id);
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .message(String.format("User #%d Deleted", id))
            .build();
    }

    @Override
    public ResponseModel check(Authentication authentication) {
        ResponseModel response = new ResponseModel();
        if (authentication != null && authentication.isAuthenticated()) {
            UserModel userModel = UserModel.builder()
                .name(authentication.getName())
                .roleName(
                    authentication.getAuthorities().stream()
                        .findFirst()
                        .get()
                        .getAuthority()
                )
                .build();
            response.setStatus(ResponseModel.SUCCESS_STATUS);
            response.setMessage(String.format("User %s Signed In", userModel.name));
            response.setData(userModel);
        } else {
            response.setStatus(ResponseModel.SUCCESS_STATUS);
            response.setMessage("User is a Guest");
        }
        return response;
    }

    @Override
    public ResponseModel onSignOut() {
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .message("Signed out")
            .build();
    }

    @Override
    public ResponseModel onError() {
        return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message("Auth error")
                .build();
    }

    @Override
    public ResponseModel makeUserAdmin(Long id) throws Exception {
        // Получаем из БД объект роли администратора
        Role role = roleDao.findRoleByName("ROLE_ADMIN");
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setRole(role);
            userDao.save(user);
            return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Admin %s created successfully", user.getName()))
                .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("User #%d Not Found", id))
                .build();
        }
    }
}
