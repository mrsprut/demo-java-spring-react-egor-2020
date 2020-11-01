package org.tyaa.demo.spring.springreactmysqlmongo.test.junit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Role;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.RoleModel;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.RoleRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.services.AuthService;
import org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.IAuthService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/*
 * Набор модульных тестов для класса AuthService
 * */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    // Внедрение экземпляра UserRepository
    // для дальнейшего использования службой AuthService
    @Mock
    private UserRepository userDAO;
    @Mock
    private RoleRepository roleDAO;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IAuthService authServiceMock;

    // Внедрение экземпляра AuthService для его дальнейшего тестирования -
    // так, что при создании в него внедрятся все необходимые зависимости,
    // помеченные в классе тестов аннтацией @Mock
    @InjectMocks
    private AuthService authService;

    ArgumentCaptor<Role> roleArgument =
            ArgumentCaptor.forClass(Role.class);

    /* Кейс, проверяющий работу метода getAllRoles -
    * его создает тестировщик,
    * используя интерфейсный макет тестируемой службы */
    @Test
    void shouldGetAllRolesReturnSuccess () {
        // Обучаем макет:
        // вернуть что? - результат, равный ...
        doReturn(
                ResponseModel.builder()
                        .status(ResponseModel.SUCCESS_STATUS)
                        .data(Arrays.asList(new RoleModel(1L, "r1"),
                                new RoleModel(2L, "r2"),
                                new RoleModel(3L, "r3")))
                        .build()
        ).when(authServiceMock) // откуда? - из объекта authServiceMock
        .getAllRoles(); // как результат вызова какого метода? - getAllRoles
        // вызов настроенного выше метода макета, полученного из интерфейса
        ResponseModel responseModel =
                authServiceMock.getAllRoles();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(3, ((List)responseModel.getData()).size());
    }

    /* Кейс, проверяющий работу метода getAllRoles -
     * его создает тестировщик,
     * используя интерфейсный макет тестируемой службы */
    @Test
    void shouldGetAllRolesReturnImpSuccess () {
        doReturn(
                Arrays.asList(new Role(1L, "r1",null),
                        new Role(2L, "r2", null),
                        new Role(3L, "r3", null))
        ).when(roleDAO)
                .findAll(Sort.by("name").ascending());
        ResponseModel responseModel =
                authService.getAllRoles();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(3, ((List)responseModel.getData()).size());
    }

    @Test
    void shouldGetAllRolesCausedFindAll () {
        ResponseModel responseModel =
                authService.getAllRoles();
        assertNotNull(responseModel);
        // Проверка, что в результате вызванного выше метода (getAllRoles)
        // минимум 1 раз был вызван метод findAll
        verify(roleDAO, atLeast(1))
                .findAll(Sort.by("name").ascending());
    }

    @Test
    void shouldCreateRoleCausedSave () {
        final RoleModel roleModel =
                RoleModel.builder()
                        .name("ROLE_DEMO")
                        .build();
        ResponseModel responseModel =
                authService.createRole(roleModel);
        assertNotNull(responseModel);
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (getAllRoles)
        // минимум 1 раз был вызван метод findAll
        verify(roleDAO, atLeast(1))
                .save(roleArgument.capture());
    }
}
