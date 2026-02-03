package com.e_commerce.service.impl;

import com.e_commerce.dto.SupplierDto;
import com.e_commerce.entity.Supplier;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.repository.SupplierRepository;
import com.e_commerce.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<SupplierDto> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDto> supplierDtos = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            supplierDtos.add(convertToDto(supplier));
        }
        return supplierDtos;
    }

    @Override
    public SupplierDto getSupplierById(Long id) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        return convertToDto(supplierOptional.get());
    }

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        try {
            System.out.println("Creating supplier: " + supplierDto.getName());
            System.out.println("User ID: " + supplierDto.getUserId());
            System.out.println("Email: " + supplierDto.getEmail());
            System.out.println("Phone: " + supplierDto.getPhone());
            System.out.println("Password: " + supplierDto.getPassword());
            
            Supplier supplier = new Supplier();
            // Don't set userId if null to avoid database constraint issues
            if (supplierDto.getUserId() != null) {
                supplier.setUserId(supplierDto.getUserId());
            }
            supplier.setName(supplierDto.getName());
            supplier.setEmail(supplierDto.getEmail());
            supplier.setPhone(supplierDto.getPhone());
            supplier.setPassword(supplierDto.getPassword());
            // Don't set createdAt manually - let @PrePersist handle it
            
            Supplier savedSupplier = supplierRepository.save(supplier);
            System.out.println("Supplier saved successfully with ID: " + savedSupplier.getId());
            return convertToDto(savedSupplier);
        } catch (Exception e) {
            System.err.println("Error creating supplier: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }

        Supplier supplier = supplierOptional.get();
        supplier.setUserId(supplierDto.getUserId());
        supplier.setName(supplierDto.getName());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setPhone(supplierDto.getPhone());
        if (supplierDto.getPassword() != null) {
            supplier.setPassword(supplierDto.getPassword());
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDto(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierDto convertToDto(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setUserId(supplier.getUserId());
        dto.setName(supplier.getName());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setPassword(supplier.getPassword());
        return dto;
    }
}
