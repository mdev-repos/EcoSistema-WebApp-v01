package com.semillero.ecosistemas.ChatBot.Service;

import com.semillero.ecosistemas.ChatBot.Model.CategoryC;

import java.util.List;

public interface ICategoryCService {

    List<CategoryC> getAllCategoryC();

    CategoryC getCategoryCById(Long id);

    //Delete
    public void deleteCategoryById(Long id);
}
