package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.DashboardDTO;
import com.semillero.ecosistemas.model.*;
import com.semillero.ecosistemas.repository.IProductRepository;
import com.semillero.ecosistemas.repository.IPublicationRepository;
import com.semillero.ecosistemas.repository.ISupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService implements IDashboardService {
    @Autowired
    private IPublicationRepository publicationRepository;
    @Autowired
    private ISupplierRepository supplierRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    public Map<String, Object> getDashboardInfo() {
        Map<String, Long> providersByCategory = getSupplierCountByCategory();
        List<DashboardDTO> lastFivePublications = getLastFivePublications();
        List<DashboardDTO> fiveMostViewedPublications = getFiveMostViewedPublications();
        Map<String, Long> productsData = getCreatedProducts();

        Map<String, Object> dashboardInfo = new LinkedHashMap<>();
        dashboardInfo.put("Nuevos productos creados", productsData);
        dashboardInfo.put("Proveedores por categoria", providersByCategory);
        dashboardInfo.put("Publicaciones (5 últimas subidas)", lastFivePublications);
        dashboardInfo.put("Publicaciones (5 más vistas)", fiveMostViewedPublications);

        return dashboardInfo;
    }

    // Obtener las 5 Ultimas Publicaciones (Titulo, Fecha, Views)
    @Override
    public List<DashboardDTO> getLastFivePublications() {
        List<Publication> publicationList = publicationRepository.findByDeletedFalse();
        List<DashboardDTO> dashboardDTOList = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();

        List<Publication> filteredPublicationByMonth = publicationList.stream()
                .filter(publication -> {
                    LocalDate creationDate = publication.getCreationDate();
                    return YearMonth.from(creationDate).equals(currentMonth);
                })
                .sorted(Comparator.comparing(Publication::getCreationDate).reversed())
                .limit(5)
                .collect(Collectors.toList());

        for (Publication item : filteredPublicationByMonth) {
            DashboardDTO newDashboardDTOItem = DashboardDTO.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .creationDate(item.getCreationDate())
                    .views(item.getViewCount())
                    .build();

            dashboardDTOList.add(0, newDashboardDTOItem); // Agrega al inicio de la lista (orden descendente)
        }
        return dashboardDTOList;
    }

    // Obtener las 5 Publicaciones (Titulo, Fecha, Views) con mas vistas
    @Override
    public List<DashboardDTO> getFiveMostViewedPublications() {
        List<Publication> publicationList = publicationRepository.findByDeletedFalse()
                .stream()
                .filter(publication -> publication.getViewCount() > 0) // No toma en cuenta publicaciones con views = 0
                .sorted((publi1, publi2) -> publi2.getViewCount().compareTo(publi1.getViewCount()))
//                .limit(5)
                .collect(Collectors.toList());

        List<DashboardDTO> dashboardDTOList = new ArrayList<>();

        for (Publication item : publicationList) {
            DashboardDTO newDashboardDTOItem = DashboardDTO.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .creationDate(item.getCreationDate())
                    .views(item.getViewCount())
                    .build();

            dashboardDTOList.add(newDashboardDTOItem);
        }

        return dashboardDTOList;
    }

    // Proveedores por categoria
    @Override
    public Map<String, Long> getSupplierCountByCategory() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .flatMap(supplier -> supplier.getProductList().stream())
                .collect(Collectors.groupingBy(product -> product.getCategory().getName(), Collectors.counting()));
    }

    // Nuevos productos creados, aprobados, en revision, denegados
    @Override
    public Map<String, Long> getCreatedProducts() {
        List<Product> products = productRepository.findByDeletedFalse();

        Map<Status, Long> statusCounts = products.stream()
                .collect(Collectors.groupingBy(Product::getStatus, Collectors.counting()));

        Map<String, Long> productInfo = new LinkedHashMap<>();

        // CONTADOR TOTAL DE PRODUCTOS EN REVISION
        long revisionTotalCount = statusCounts.getOrDefault(Status.REVISION_INICIAL, 0L) +
                statusCounts.getOrDefault(Status.REQUIERE_CAMBIOS, 0L) +
                statusCounts.getOrDefault(Status.CAMBIOS_REALIZADOS, 0L);

        productInfo.put("Total", (long) products.size());
        productInfo.put("Aceptado", statusCounts.getOrDefault(Status.ACEPTADO, 0L));
        productInfo.put("En revisión", revisionTotalCount);
        productInfo.put("Denegado", statusCounts.getOrDefault(Status.DENEGADO, 0L));

        return productInfo;
    }
}
