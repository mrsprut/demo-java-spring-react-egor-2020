package org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces;

import org.springframework.security.core.Authentication;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.RoleModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.UserModel;

import javax.transaction.Transactional;

public interface IAuthService {

    ResponseModel createRole(RoleModel roleModel);

    ResponseModel createUser(UserModel userModel);

    ResponseModel getAllRoles();

    @Transactional
    ResponseModel getRoleUsers(Long roleId);

    ResponseModel deleteRole(Long id);

    ResponseModel deleteUser(Long id);

    ResponseModel check(Authentication authentication);

    ResponseModel onSignOut();

    ResponseModel onError();

    ResponseModel makeUserAdmin(Long id) throws Exception;
}
