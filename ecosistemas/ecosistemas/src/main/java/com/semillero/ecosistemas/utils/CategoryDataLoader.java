package com.semillero.ecosistemas.utils;

import com.semillero.ecosistemas.model.Category;
import com.semillero.ecosistemas.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCategoryData();
    }

    private void loadCategoryData() {
        if (categoryRepository.count() == 0) {
            Category bienestar = new Category();
            bienestar.setName("Bienestar");

            Category capacitaciones = new Category();
            capacitaciones.setName("Capacitaciones");

            Category construccion = new Category();
            construccion.setName("Construcción");

            Category cultivos = new Category();
            cultivos.setName("Cultivos");

            Category gastronomia = new Category();
            gastronomia.setName("Gastronomía");

            Category indumentaria = new Category();
            indumentaria.setName("Indumentaria");

            Category merchandising = new Category();
            merchandising.setName("Merchandising");

            Category mueblesdeco = new Category();
            mueblesdeco.setName("Muebles/Deco");

            Category reciclaje = new Category();
            reciclaje.setName("Reciclaje");

            Category tecnologia = new Category();
            tecnologia.setName("Tecnología");

            Category transporte = new Category();
            transporte.setName("Transporte");

            categoryRepository.save(bienestar);
            categoryRepository.save(capacitaciones);
            categoryRepository.save(construccion);
            categoryRepository.save(cultivos);
            categoryRepository.save(gastronomia);
            categoryRepository.save(indumentaria);
            categoryRepository.save(merchandising);
            categoryRepository.save(mueblesdeco);
            categoryRepository.save(reciclaje);
            categoryRepository.save(tecnologia);
            categoryRepository.save(transporte);

        }
    }
}