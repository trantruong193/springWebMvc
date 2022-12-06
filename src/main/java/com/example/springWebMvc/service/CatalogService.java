package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> getAll();
    List<Catalog> getCatalogByCatId(Long catId);
    Catalog getCatalogById(Long catalogId);
}
