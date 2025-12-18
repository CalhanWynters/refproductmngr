package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductQueryDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductIdVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// Added the missing class wrapper
class ProductQueryMapperTest {

    @Test
    @DisplayName("Should map to specialized DTO using mocks to bypass full constructor")
    void testToDTO() {
        // Arrange
        String productId = UUID.randomUUID().toString();
        String businessId = "BUSINESS-123";

        // Create a mock to avoid the 7-argument constructor requirement
        ProductAggregate aggregate = mock(ProductAggregate.class);

        // Map the mock behavior to the VO values
        // Ensure aggregate.id() matches the method name in ProductAggregate
        when(aggregate.id()).thenReturn(new ProductIdVO(productId));
        when(aggregate.businessIdVO()).thenReturn(new BusinessIdVO(businessId));

        // Act
        ProductQueryDTO result = ProductQueryMapper.toDTO(aggregate);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals(businessId, result.businessId());
    }
}
