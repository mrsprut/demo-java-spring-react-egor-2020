package org.tyaa.demo.spring.springreactmysqlmongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tyaa.demo.spring.springreactmysqlmongo.models.CategoryModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.services.CategoryService;
import org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.ICategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private ICategoryService service;

    @GetMapping("/categories")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<ResponseModel> create(@RequestBody CategoryModel category) {
        return new ResponseEntity<>(service.create(category), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/category/{id}")
    public ResponseEntity<ResponseModel> update(@PathVariable Long id, @RequestBody CategoryModel category) {
        category.setId(id);
        return new ResponseEntity<>(service.update(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/category/{id}")
    public ResponseEntity<ResponseModel> deleteCategory(@PathVariable Long id) {
        ResponseModel responseModel = service.delete(id);
        System.out.println(responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
