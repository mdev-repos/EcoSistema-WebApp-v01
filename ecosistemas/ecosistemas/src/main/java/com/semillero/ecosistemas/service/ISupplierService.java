package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Supplier;
import com.semillero.ecosistemas.model.User;

import java.util.List;
import java.util.Optional;

public interface ISupplierService {
    //CREATE
    public Supplier saveSupplier(Supplier supplier);

    //FIND
    public Optional<Supplier> findSupplierById(Long id);
    public Optional<Supplier> findSupplierByEmail(String email);

    //READ
    public List<Supplier> getAllSuppliers();

    //UPDATE
    public Supplier updateSupplier(Long id, Supplier supplier);
    public void deactivateSupplier(Supplier supplier);
}