package org.tyaa.demo.spring.springreactmysqlmongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.UserModel;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.IUserService;

import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseModel getAll() {

        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .data(
                userRepository.findAll().stream()
                    .map(user -> UserModel.builder()
                        .name(user.getName())
                        .password(user.getPassword())
                        .roleName(user.getRole().getName())
                        .build()
                    )
                .collect(Collectors.toList())
            )
            .build();
    }

    @Override
    public ResponseModel getByName(String name) {
        return null;
    }
}
