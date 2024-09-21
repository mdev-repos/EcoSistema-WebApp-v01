package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.model.Supplier;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledEmailService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ISupplierService supplierService;


    public void sendWeeklyEmails() {
        sendEmailsToAppUsers();
    }

    private void sendEmailsToAppUsers(){
        // Emails para Admins
        List<Admin> allAdmins = adminService.getAllAdmins();

        for(Admin admin : allAdmins){
            try{
                emailSenderService.sendAdminEmail(admin.getEmail(), admin.getName());
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }

        // Emails para Suppliers
        List<Supplier> allSuppliers = supplierService.getAllSuppliers();

        for(Supplier supplier : allSuppliers){
            try{
                emailSenderService.sendSupplierEmail(supplier.getEmail(), supplier.getName());
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }

    }
}