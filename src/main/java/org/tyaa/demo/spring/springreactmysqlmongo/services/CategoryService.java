package org.tyaa.demo.spring.springreactmysqlmongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Category;
import org.tyaa.demo.spring.springreactmysqlmongo.models.CategoryModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.CategoryHibernateDAO;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.ProductHibernateDAO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService implements org.tyaa.demo.spring.springreactmysqlmongo.services.interfaces.ICategoryService {

    @Autowired
    private CategoryHibernateDAO categoryDao;

    @Autowired
    private ProductHibernateDAO productDao;

    @Override
    public ResponseModel create(CategoryModel categoryModel) {
        Category category =
            Category.builder().name(categoryModel.getName().trim()).build();
        categoryDao.save(category);
        // Demo Logging
        System.out.println(String.format("Category %s Created", category.getName()));
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .message(String.format("Category %s Created", category.getName()))
            .build();
    }

    @Override
    public ResponseModel update(CategoryModel categoryModel) {
        Category category =
            Category.builder()
                .id(categoryModel.getId())
                .name(categoryModel.getName())
                .build();
        categoryDao.save(category);
        // Demo Logging
        System.out.println(String.format("Category %s Updated", category.getName()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Category %s Updated", category.getName()))
                .build();
    }

    @Override
    public ResponseModel getAll() {
        List<Category> categories = categoryDao.findAll(Sort.by("id").descending());
        List<CategoryModel> categoryModels =
            categories.stream()
            .map(c ->
                CategoryModel.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .build()
            )
            .collect(Collectors.toList());
        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .data(categoryModels)
            .build();
    }

    @Override
    public ResponseModel delete(Long id) {
        Optional<Category> categoryOptional = categoryDao.findById(id);
        if (categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            System.out.println(productDao.countProductsByCategory(category) == 0);
            if(productDao.countProductsByCategory(category) == 0) {
                categoryDao.delete(category);
                return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Category #%s Deleted", category.getName()))
                    .build();
            } else {
                return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Can't delete the category #%s. There are some products in this category.", category.getName()))
                    .build();
            }
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("Category #%d Not Found", id))
                .build();
        }
    }
}
