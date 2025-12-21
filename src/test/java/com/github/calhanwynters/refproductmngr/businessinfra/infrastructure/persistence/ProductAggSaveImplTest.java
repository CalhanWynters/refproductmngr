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
        // 1. Arrange Data
        UUID productId = UUID.randomUUID();
        String validBusinessIdStr = UUID.randomUUID().toString().toUpperCase();
        String category = "Electronics";
        String description = "Latest tech gadget";

        ProductAggregate product = mock(ProductAggregate.class);
        when(product.id()).thenReturn(new ProductIdVO(productId.toString()));
        when(product.businessIdVO()).thenReturn(new BusinessIdVO(validBusinessIdStr));
        when(product.category()).thenReturn(new CategoryVO(category));
        when(product.description()).thenReturn(new DescriptionVO(description));
        when(product.isDeleted()).thenReturn(false);
        when(product.version()).thenReturn(new VersionVO(1));

        // 2. Act
        productCommandRepository.save(product);

        // 3. Capture using ArgumentCaptor for the varargs update method
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> paramsCaptor = ArgumentCaptor.forClass(Object.class);

        // Use any() for the varargs to match the signature jdbcTemplate.update(String, Object...)
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture(), paramsCaptor.capture(),
                paramsCaptor.capture(), paramsCaptor.capture(),
                paramsCaptor.capture(), paramsCaptor.capture());

        // 4. Printout for Confirmation
        System.out.println("=================================================");
        System.out.println("ACTUAL SQL EXECUTED:");
        System.out.println("=================================================");
        System.out.println(sqlCaptor.getValue());
        System.out.println("=================================================");
        System.out.println("PARAMETERS DETECTED:");
        List<Object> capturedParams = paramsCaptor.getAllValues();
        for (int i = 0; i < capturedParams.size(); i++) {
            System.out.println("Param [" + i + "]: " + capturedParams.get(i));
        }
        System.out.println("=================================================");
    }
}
