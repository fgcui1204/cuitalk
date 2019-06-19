package com.fgcui.blog.repository;

import com.fgcui.blog.po.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);


    @Query("select c from Category c")
    List<Category> findTopCategory(Pageable pageable);
}
