package com.semillero.ecosistemas.utils;

import com.semillero.ecosistemas.model.Country;
import com.semillero.ecosistemas.model.Province;
import com.semillero.ecosistemas.repository.ICountryRepository;
import com.semillero.ecosistemas.repository.IProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryDataLoader implements CommandLineRunner {

    @Autowired
    private ICountryRepository countryRepository;

    @Autowired
    private IProvinceRepository provinceRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCountryData();
        loadProvinceData();
    }

    private void loadCountryData() {
        if (countryRepository.count() == 0) {
            Country argentina = new Country();
            argentina.setName("Argentina");

            Country bolivia = new Country();
            bolivia.setName("Bolivia");

            Country brazil = new Country();
            brazil.setName("Brazil");

            Country chile = new Country();
            chile.setName("Chile");

            Country colombia = new Country();
            colombia.setName("Colombia");

            Country ecuador = new Country();
            ecuador.setName("Ecuador");

            Country paraguay = new Country();
            paraguay.setName("Paraguay");

            Country peru = new Country();
            peru.setName("Peru");

            countryRepository.save(argentina);
            countryRepository.save(bolivia);
            countryRepository.save(brazil);
            countryRepository.save(chile);
            countryRepository.save(colombia);
            countryRepository.save(ecuador);
            countryRepository.save(paraguay);
            countryRepository.save(peru);
        }
    }

    private void loadProvinceData() {
        if (provinceRepository.count() == 0) {
            Country argentina = countryRepository.findById(1L).orElse(null);
            Country bolivia = countryRepository.findById(2L).orElse(null);
            Country brazil = countryRepository.findById(3L).orElse(null);
            Country chile = countryRepository.findById(4L).orElse(null);
            Country colombia = countryRepository.findById(5L).orElse(null);
            Country ecuador = countryRepository.findById(6L).orElse(null);
            Country paraguay = countryRepository.findById(7L).orElse(null);
            Country peru = countryRepository.findById(8L).orElse(null);

            if (argentina != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Buenos Aires", argentina),
                        new Province("Catamarca", argentina),
                        new Province("Chaco", argentina),
                        new Province("Chubut", argentina),
                        new Province("Córdoba", argentina),
                        new Province("Corrientes", argentina),
                        new Province("Entre Ríos", argentina),
                        new Province("Formosa", argentina),
                        new Province("Jujuy", argentina),
                        new Province("La Pampa", argentina),
                        new Province("La Rioja", argentina),
                        new Province("Mendoza", argentina),
                        new Province("Misiones", argentina),
                        new Province("Neuquén", argentina),
                        new Province("Río Negro", argentina),
                        new Province("Salta", argentina),
                        new Province("San Juan", argentina),
                        new Province("San Luis", argentina),
                        new Province("Santa Cruz", argentina),
                        new Province("Santa Fe", argentina),
                        new Province("Santiago del Estero", argentina),
                        new Province("Tierra del Fuego", argentina),
                        new Province("Tucumán", argentina)
                ));
            }

            if (bolivia != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Chuquisaca", bolivia),
                        new Province("Cochabamba", bolivia),
                        new Province("Beni", bolivia),
                        new Province("La Paz", bolivia),
                        new Province("Oruro", bolivia),
                        new Province("Pando", bolivia),
                        new Province("Potosí", bolivia),
                        new Province("Santa Cruz", bolivia),
                        new Province("Tarija", bolivia)
                ));
            }
            if (brazil != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Acre", brazil),
                        new Province("Alagoas", brazil),
                        new Province("Amapá", brazil),
                        new Province("Amazonas", brazil),
                        new Province("Bahia", brazil),
                        new Province("Ceará", brazil),
                        new Province("Espírito Santo", brazil),
                        new Province("Goiás", brazil),
                        new Province("Maranhão", brazil),
                        new Province("Mato Grosso", brazil),
                        new Province("Mato Grosso do Sul", brazil),
                        new Province("Minas Gerais", brazil),
                        new Province("Pará", brazil),
                        new Province("Paraíba", brazil),
                        new Province("Paraná", brazil),
                        new Province("Pernambuco", brazil),
                        new Province("Piauí", brazil),
                        new Province("Rio de Janeiro", brazil),
                        new Province("Rio Grande do Norte", brazil),
                        new Province("Rio Grande do Sul", brazil),
                        new Province("Rondônia", brazil),
                        new Province("Roraima", brazil),
                        new Province("Santa Catarina", brazil),
                        new Province("São Paulo", brazil),
                        new Province("Sergipe", brazil),
                        new Province("Tocantins", brazil)
                ));
            }

            if (chile != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Arica y Parinacota", chile),
                        new Province("Atacama", chile),
                        new Province("Antofagasta", chile),
                        new Province("Coquimbo", chile),
                        new Province("Valparaíso", chile),
                        new Province("Metropolitana de Santiago", chile),
                        new Province("O'Higgins", chile),
                        new Province("Maule", chile),
                        new Province("Ñuble", chile),
                        new Province("Biobío", chile),
                        new Province("La Araucanía", chile),
                        new Province("Los Ríos", chile),
                        new Province("Los Lagos", chile),
                        new Province("Aysén del General Carlos Ibáñez del Campo", chile),
                        new Province("Magallanes y de la Antártica Chilena", chile)
                ));
            }

            if (colombia != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Amazonas", colombia),
                        new Province("Antioquia", colombia),
                        new Province("Arauca", colombia),
                        new Province("Atlántico", colombia),
                        new Province("Bolívar", colombia),
                        new Province("Boyacá", colombia),
                        new Province("Caldas", colombia),
                        new Province("Caquetá", colombia),
                        new Province("Casanare", colombia),
                        new Province("Cauca", colombia),
                        new Province("Cesar", colombia),
                        new Province("Chocó", colombia),
                        new Province("Córdoba", colombia),
                        new Province("Cundinamarca", colombia),
                        new Province("Guainía", colombia),
                        new Province("Guaviare", colombia),
                        new Province("Guajira", colombia),
                        new Province("Huila", colombia),
                        new Province("La Guajira", colombia),
                        new Province("Magdalena", colombia),
                        new Province("Meta", colombia),
                        new Province("Nariño", colombia),
                        new Province("Norte de Santander", colombia),
                        new Province("Putumayo", colombia),
                        new Province("Quindío", colombia),
                        new Province("Risaralda", colombia),
                        new Province("San Andrés y Providencia", colombia),
                        new Province("Santander", colombia),
                        new Province("Sucre", colombia),
                        new Province("Tolima", colombia),
                        new Province("Valle del Cauca", colombia),
                        new Province("Vaupés", colombia),
                        new Province("Vichada", colombia)
                ));
            }

            if (ecuador != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Azuay", ecuador),
                        new Province("Bolívar", ecuador),
                        new Province("Cañar", ecuador),
                        new Province("Carchi", ecuador),
                        new Province("Chimborazo", ecuador),
                        new Province("Cotopaxi", ecuador),
                        new Province("El Oro", ecuador),
                        new Province("Esmeraldas", ecuador),
                        new Province("Galápagos", ecuador),
                        new Province("Guayas", ecuador),
                        new Province("Imbabura", ecuador),
                        new Province("Loja", ecuador),
                        new Province("Los Ríos", ecuador),
                        new Province("Manabí", ecuador),
                        new Province("Morona Santiago", ecuador),
                        new Province("Napo", ecuador),
                        new Province("Orellana", ecuador),
                        new Province("Pastaza", ecuador),
                        new Province("Pichincha", ecuador),
                        new Province("Sucumbíos", ecuador),
                        new Province("Tungurahua", ecuador),
                        new Province("Zamora-Chinchipe", ecuador)
                ));
            }

            if (paraguay != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Asunción", paraguay),
                        new Province("Central", paraguay),
                        new Province("Concepción", paraguay),
                        new Province("Cordillera", paraguay),
                        new Province("Guairá", paraguay),
                        new Province("Itapúa", paraguay),
                        new Province("Ka'asapa", paraguay),
                        new Province("Misiones", paraguay),
                        new Province("Paraguarí", paraguay),
                        new Province("Presidente Hayes", paraguay),
                        new Province("San Pedro", paraguay),
                        new Province("Ñeembucú", paraguay)
                ));
            }

            if (peru != null) {
                provinceRepository.saveAll(List.of(
                        new Province("Amazonas", peru),
                        new Province("Áncash", peru),
                        new Province("Apurímac", peru),
                        new Province("Arequipa", peru),
                        new Province("Ayacucho", peru),
                        new Province("Cajamarca", peru),
                        new Province("Callao", peru),
                        new Province("Cusco", peru),
                        new Province("Huancavelica", peru),
                        new Province("Huánuco", peru),
                        new Province("Ica", peru),
                        new Province("Junín", peru),
                        new Province("La Libertad", peru),
                        new Province("Lambayeque", peru),
                        new Province("Lima", peru),
                        new Province("Loreto", peru),
                        new Province("Madre de Dios", peru),
                        new Province("Moquegua", peru),
                        new Province("Pasco", peru),
                        new Province("Piura", peru),
                        new Province("Puno", peru),
                        new Province("San Martín", peru),
                        new Province("Tacna", peru),
                        new Province("Tumbes", peru),
                        new Province("Ucayali", peru)
                ));
            }
        }
    }
}