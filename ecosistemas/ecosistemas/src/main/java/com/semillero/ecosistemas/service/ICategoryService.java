package com.semillero.ecosistemas.service;

import java.util.List;
import com.semillero.ecosistemas.model.Category;

public interface ICategoryService {
    //Create
    public Category saveCategory(Category category);

    //Read
    public Category findCategoryById(Long id);

    //Get All
    public List<Category> getAllCategories();

    //Update
    public Category uptadeCategory(Long id, Category category);

    //Delete
    public void deleteCategoryById(Long id);
}
