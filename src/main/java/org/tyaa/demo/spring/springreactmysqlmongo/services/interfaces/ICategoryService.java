package org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces;

import org.tyaa.demo.spring.springreactmysqlmongo.models.CategoryModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;

public interface ICategoryService {
    ResponseModel create(CategoryModel categoryModel);

    ResponseModel update(CategoryModel categoryModel);

    ResponseModel getAll();

    ResponseModel delete(Long id);
}
