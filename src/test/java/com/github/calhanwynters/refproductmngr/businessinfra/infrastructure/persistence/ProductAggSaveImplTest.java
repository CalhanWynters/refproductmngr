package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class ProductAggSaveImplTest {

    private JdbcTemplate jdbcTemplate;
    private ProductCommandRepositoryImpl productCommandRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        productCommandRepository = new ProductCommandRepositoryImpl(
                jdbcTemplate,
                objectMapper,
                namedParameterJdbcTemplate
        );
    }

    @Test
    void testUpsertProduct() {
        // 1. Arrange: Prepare 2025 compliant test data
        UUID productId = UUID.randomUUID();
        String validBusinessIdStr = UUID.randomUUID().toString().toUpperCase();
        String category = "Electronics";
        String description = "Latest tech gadget";
        boolean isDeleted = false;
        int version = 1;

        ProductAggregate product = mock(ProductAggregate.class);
        when(product.id()).thenReturn(new ProductIdVO(productId.toString()));
        when(product.businessIdVO()).thenReturn(new BusinessIdVO(validBusinessIdStr));
        when(product.category()).thenReturn(new CategoryVO(category));
        when(product.description()).thenReturn(new DescriptionVO(description));
        when(product.isDeleted()).thenReturn(isDeleted);
        when(product.version()).thenReturn(new VersionVO(version));

        // 2. Act
        productCommandRepository.save(product);

        // 3. Capture: Grab the exact SQL and every Vararg parameter
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> paramsCaptor = ArgumentCaptor.forClass(Object.class);

        // We capture exactly 6 parameters corresponding to the 6 '?' in your SQL
        verify(jdbcTemplate).update(
                sqlCaptor.capture(),
                paramsCaptor.capture(), // id
                paramsCaptor.capture(), // business_id_vo
                paramsCaptor.capture(), // category
                paramsCaptor.capture(), // description
                paramsCaptor.capture(), // is_deleted
                paramsCaptor.capture()  // schema_version
        );

        // 4. Printout: Output for your confirmation
        System.out.println("\n" + "=".repeat(50));
        System.out.println("2025 REPOSITORY EXECUTION PRINTOUT");
        System.out.println("=".repeat(50));
        System.out.println("ACTUAL SQL:");
        System.out.println(sqlCaptor.getValue());
        System.out.println("-".repeat(50));
        System.out.println("PARAMETERS DETECTED:");

        List<Object> capturedParams = paramsCaptor.getAllValues();
        String[] paramLabels = {"ID", "Business ID", "Category", "Description", "Is Deleted", "Version"};

        for (int i = 0; i < capturedParams.size(); i++) {
            System.out.printf("Param [%d] (%s): %s%n", i, paramLabels[i], capturedParams.get(i));
        }
        System.out.println("=".repeat(50) + "\n");
    }
}
