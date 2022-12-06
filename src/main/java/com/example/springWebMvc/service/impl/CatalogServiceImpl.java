package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Catalog;
import com.example.springWebMvc.repository.CatalogRepository;
import com.example.springWebMvc.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl (CatalogRepository catalogRepository){
        this.catalogRepository = catalogRepository;
    }
    @Override
    public List<Catalog> getAll() {

        return catalogRepository.findAll();
    }

    @Override
    public List<Catalog> getCatalogByCatId(Long catId) {
        return catalogRepository.getCatalogByCategory_CatId(catId);
    }

    @Override
    public Catalog getCatalogById(Long catalogId) {
        return catalogRepository.findById(catalogId).orElse(null);
    }
}
