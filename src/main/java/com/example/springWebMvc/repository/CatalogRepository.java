package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    List<Catalog> getCatalogByCategory_CatId(Long catId);
}
