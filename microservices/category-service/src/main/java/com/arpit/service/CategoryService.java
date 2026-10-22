package com.arpit.service;

import com.arpit.payload.dto.SalonDTO;
import com.arpit.modal.Category;

import java.util.Set;

public interface CategoryService {


    Category createCategory(Category category, SalonDTO salonDTO);

    Set<Category> getAllCategoriesBySalon(Long id);

    Category getCategoryById(Long id) throws Exception;

    void deleteCategoryById(Long id, Long salonId) throws Exception;

    Category findByIdAndSalonId(Long id,Long salonId) throws Exception;
}
