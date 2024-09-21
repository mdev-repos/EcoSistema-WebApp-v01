package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ProductService productService;

    @Transactional
    public void sendAdminEmail(String to, String toName) throws MessagingException {
        List<Product> products = productService.getProductsModifiedInLastWeek();

        List<Product> newProducts = products.stream()
                .filter(product -> "REVISION_INICIAL".equals(product.getStatus().toString()))
                .collect(Collectors.toList());

        List<Product> updateProducts = products.stream()
                .filter(product -> "CAMBIOS_REALIZADOS".equals(product.getStatus().toString()))
                .collect(Collectors.toList());

        // Inicializar explícitamente la colección de imágenes dentro de una transacción
        newProducts.forEach(product -> product.getImagesURLs().size());
        updateProducts.forEach(product -> product.getImagesURLs().size());

        Context context = new Context();
        context.setVariable("newProducts", newProducts);
        context.setVariable("updateProducts", updateProducts);
        context.setVariable("toName", toName);

        String body = templateEngine.process("emails/admin-email", context);

        sendHtmlEmail(to, "Actualización Semanal de ECOSistema", body);
    }

    @Transactional
    public void sendSupplierEmail(String to, String toName) throws MessagingException {
        List<Product> products = productService.getProductsModifiedInLastWeek();

        List<Product> acceptedProducts = products.stream()
                .filter(product -> "ACEPTADO".equals(product.getStatus().toString()))
                .collect(Collectors.toList());

        // Inicializar explícitamente la colección de imágenes dentro de una transacción
        acceptedProducts.forEach(product -> product.getImagesURLs().size());

        Context context = new Context();
        context.setVariable("acceptedProducts", acceptedProducts);
        context.setVariable("toName", toName);

        String body = templateEngine.process("emails/supplier-email", context);

        sendHtmlEmail(to, "¡Nuevos Productos en ECOSistema!", body);
    }

    private void sendHtmlEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        // Adjuntar el logo
        ClassPathResource logo = new ClassPathResource("static/logo.png");
        helper.addInline("logo", logo);


        emailSender.send(message);
    }
}