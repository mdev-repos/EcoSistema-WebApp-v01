package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.model.Supplier;
import com.semillero.ecosistemas.model.User;

import java.util.List;
import java.util.Optional;

public interface IAdminService {
    //CREATE
    public Admin saveAdmin(Admin admin);

    //FIND
    public Optional<Admin> findAdminById(Long id);
    public Optional<Admin> findAdminByEmail(String email);

    //READ
    public List<Admin> getAllAdmins();

    //UPDATE
    public Admin updateAdmin(Long id, Admin admin);
    public void deactivateAdmin(Admin admin);

    //EXTRACT TOKEN
    public Long extractAdminIdFromToken(String token);
}