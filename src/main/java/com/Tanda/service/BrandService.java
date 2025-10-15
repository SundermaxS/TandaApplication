package com.Tanda.service;

import com.Tanda.entity.Brand;
import com.Tanda.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public Brand getById(Long id) {
        return brandRepository.findById(id).orElseThrow();
    }

    public Brand create(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand update(Long id, Brand brand) {
        Brand existing = getById(id);
        existing.setName(brand.getName());
        return brandRepository.save(existing);
    }

    public void delete(Long id) {
        brandRepository.deleteById(id);
    }
}
