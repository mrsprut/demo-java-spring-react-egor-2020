package org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces;

import org.springframework.stereotype.Service;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;

import java.util.List;

public interface IUserService {
    ResponseModel getAll();
    ResponseModel getByName(String name);
}
