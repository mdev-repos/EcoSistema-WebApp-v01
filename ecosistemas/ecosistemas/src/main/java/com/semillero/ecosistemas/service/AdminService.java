package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.jwt.JwtService;
import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.repository.IAdminRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService{
    @Autowired
    IAdminRepository adminRepository;
    @Autowired
    JwtService jwtService;

    //CREATE
    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    //FIND
    @Override
    public Optional<Admin> findAdminById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    //READ
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    //UPDATE
    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        return null;
    }

    @Override
    public void deactivateAdmin(Admin admin) {
        admin.setDeleted(true); // Set deleted TRUE --> Account deactivation
    }

    @Override
    public Long extractAdminIdFromToken(String token) {
        Claims claims = jwtService.extractClaims(token);
        return claims.get("id", Long.class);
    }
}