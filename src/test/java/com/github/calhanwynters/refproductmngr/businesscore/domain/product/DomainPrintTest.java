package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DomainPrintTest {
    @Test
    void printFeatureBasicEntity() {
        // Arrange
        FeatureIdVO id = FeatureIdVO.generate();
        NameVO name = new NameVO("Sample Feature Name");
        DescriptionVO description = new DescriptionVO("This is a sample feature description for testing purposes.");
        LabelVO label = new LabelVO("Sample Label");

        FeatureBasicEntity entity = new FeatureBasicEntity(id, name, description, label);

        // Act & Print
        System.out.println("=== FeatureBasicEntity Print Test ===");
        System.out.println("ID: " + entity.getId());
        System.out.println("Name: " + entity.getNameVO());
        System.out.println("Description: " + entity.getDescription());
        System.out.println("Label: " + entity.getLabelVO());
        System.out.println("Entity: " + entity);
        System.out.println("======================================\n");

        // Assert - just verify it was created successfully
        assertNotNull(entity);
    }

    @Test
    void printFeatureFixedPriceEntity() {
        // Arrange
        FeatureIdVO id = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        NameVO name = new NameVO("Gift Wrapping");
        DescriptionVO description = new DescriptionVO("A beautiful gift wrap for your purchase.");
        LabelVO label = new LabelVO("Gift Wrap");
        BigDecimal fixedPrice = new BigDecimal("5.00");

        FeatureFixedPriceEntity entity = new FeatureFixedPriceEntity(id, name, description, label, fixedPrice);

        // Act & Print
        System.out.println("=== FeatureFixedPriceEntity Print Test ===");
        System.out.println("ID: " + entity.getId());
        System.out.println("Name: " + entity.getNameVO());
        System.out.println("Description: " + entity.getDescription());
        System.out.println("Label: " + entity.getLabelVO());
        System.out.println("Fixed Price: " + entity.getFixedPrice());
        System.out.println("Entity: " + entity);
        System.out.println("==========================================\n");

        // Assert - just verify it was created successfully
        assertNotNull(entity);
        assertEquals(new BigDecimal("5.00"), entity.getFixedPrice());
    }

    @Test
    void printFeatureScalingPriceEntity() {
        // Arrange
        FeatureIdVO id = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        NameVO name = new NameVO("Custom Fabric");
        DescriptionVO description = new DescriptionVO("Custom length of fabric for your needs.");
        LabelVO label = new LabelVO("Custom Fabric Length");
        MeasurementUnitVO measurementUnit = new MeasurementUnitVO("meters");
        BigDecimal baseAmount = new BigDecimal("10.00");
        BigDecimal incrementAmount = new BigDecimal("2.50");
        int maxQuantity = 100;

        FeatureScalingPriceEntity entity = new FeatureScalingPriceEntity(
                id, name, description, label, measurementUnit,
                baseAmount, incrementAmount, maxQuantity
        );

        // Act & Print
        System.out.println("=== FeatureScalingPriceEntity Print Test ===");
        System.out.println("ID: " + entity.getId());
        System.out.println("Name: " + entity.getNameVO());
        System.out.println("Description: " + entity.getDescription());
        System.out.println("Label: " + entity.getLabelVO());
        System.out.println("Measurement Unit: " + entity.getMeasurementUnit());
        System.out.println("Base Amount: $" + entity.getBaseAmount());
        System.out.println("Increment Amount: $" + entity.getIncrementAmount() + " per unit");
        System.out.println("Max Quantity: " + entity.getMaxQuantity());
        System.out.println("\n--- Price Scaling Examples ---");
        System.out.println("1 unit:   $" + entity.calculateTotalPrice(1));
        System.out.println("5 units:  $" + entity.calculateTotalPrice(5));
        System.out.println("10 units: $" + entity.calculateTotalPrice(10));
        System.out.println("25 units: $" + entity.calculateTotalPrice(25));
        System.out.println("50 units: $" + entity.calculateTotalPrice(50));
        System.out.println("\nEntity: " + entity);
        System.out.println("============================================\n");

        // Assert - just verify it was created successfully
        assertNotNull(entity);
        assertEquals(new BigDecimal("22.50"), entity.calculateTotalPrice(5));
    }

    @Test
    void printVariantEntity() {
        // Arrange
        VariantIdVO id = VariantIdVO.generate();
        SkuVO sku = new SkuVO("SKU-12345");
        PriceVO basePrice = new PriceVO(new BigDecimal("10.00"), 2, Currency.getInstance("USD"));
        PriceVO currentPrice = new PriceVO(new BigDecimal("9.99"), 2, Currency.getInstance("USD"));

        // Create some sample features
        FeatureBasicEntity feature1 = new FeatureBasicEntity(
                FeatureIdVO.generate(),
                new NameVO("Extra Padding"),
                new DescriptionVO("Additional comfort padding for extended wear."),
                new LabelVO("Padding")
        );

        FeatureFixedPriceEntity feature2 = new FeatureFixedPriceEntity(
                new FeatureIdVO("b1ffbc99-9c0b-4ef8-bb6d-6bb9bd380a22"),
                new NameVO("Gift Box"),
                new DescriptionVO("Premium gift box packaging with ribbon."),
                new LabelVO("Gift Box"),
                new BigDecimal("3.50")
        );

        Set<FeatureAbstractClass> features = Set.of(feature1, feature2);

        CareInstructionVO careInstructions = mock(CareInstructionVO.class);
        WeightVO weight = mock(WeightVO.class);
        VariantStatusEnums status = VariantStatusEnums.ACTIVE;

        VariantEntity entity = new VariantEntity(
                id, sku, basePrice, currentPrice, features, careInstructions, weight, status
        );

        // Act & Print
        System.out.println("=== VariantEntity Print Test ===");
        System.out.println("ID: " + entity.id());
        System.out.println("SKU: " + entity.sku());
        System.out.println("Base Price: " + entity.basePrice());
        System.out.println("Current Price: " + entity.currentPrice());
        System.out.println("Status: " + entity.status());
        System.out.println("Is Active: " + entity.isActive());
        System.out.println("Weight: " + entity.weight());
        System.out.println("Care Instructions: " + entity.careInstructions());
        System.out.println("\n--- Features (" + entity.features().size() + ") ---");
        entity.features().forEach(feature -> System.out.println("  - " + feature));
        System.out.println("\nEntity: " + entity);
        System.out.println("=====================================\n");

        // Assert - just verify it was created successfully
        assertNotNull(entity);
        assertTrue(entity.isActive());
        assertEquals(2, entity.features().size());
    }

    @Test
    void printProductAggregate() {
        // Arrange
        ProductIdVO productId = new ProductIdVO(UUID.randomUUID().toString());
        BusinessIdVO businessId = new BusinessIdVO("B456");
        CategoryVO category = new CategoryVO("C789");
        DescriptionVO description = new DescriptionVO("Premium Cotton T-Shirt with custom options");
        VersionVO version = new VersionVO(1);

        // Create gallery with images
        List<ImageUrlVO> images = List.of(
                new ImageUrlVO("https://example.com/product-front.jpg"),
                new ImageUrlVO("https://example.com/product-back.jpg"),
                new ImageUrlVO("https://example.com/product-detail.jpg")
        );
        GalleryVO gallery = new GalleryVO(images);

        // Create variants
        VariantEntity variant1 = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-SMALL-BLUE"),
                new PriceVO(new BigDecimal("25.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("19.99"), 2, Currency.getInstance("USD")),
                Set.of(),
                mock(CareInstructionVO.class),
                mock(WeightVO.class),
                VariantStatusEnums.ACTIVE
        );

        VariantEntity variant2 = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-LARGE-RED"),
                new PriceVO(new BigDecimal("27.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("27.00"), 2, Currency.getInstance("USD")),
                Set.of(),
                mock(CareInstructionVO.class),
                mock(WeightVO.class),
                VariantStatusEnums.DRAFT
        );

        Set<VariantEntity> variants = Set.of(variant1, variant2);

        ProductAggregate aggregate = new ProductAggregate(
                productId, businessId, category, description, gallery, variants, version
        );

        // Act & Print
        System.out.println("=== ProductAggregate Print Test ===");
        System.out.println("Product ID: " + aggregate.id());
        System.out.println("Business ID: " + aggregate.businessIdVO());
        System.out.println("Category: " + aggregate.category());
        System.out.println("Description: " + aggregate.description());
        System.out.println("Version: " + aggregate.version());
        System.out.println("\n--- Gallery (" + aggregate.gallery().images().size() + " images) ---");
        aggregate.gallery().images().forEach(img -> System.out.println("  - " + img));
        System.out.println("\n--- Variants (" + aggregate.variants().size() + ") ---");
        aggregate.variants().forEach(variant -> System.out.println("  - SKU: " + variant.sku() + " | Status: " + variant.status() + " | Price: " + variant.currentPrice()));
        System.out.println("\n--- Business Rules ---");
        System.out.println("Has Minimum Images: " + aggregate.hasMinimumImages());
        System.out.println("Has Active Variants: " + aggregate.hasActiveVariants());
        System.out.println("Is Publishable: " + aggregate.isPublishable());
        System.out.println("All Variants Are Draft: " + aggregate.allVariantsAreDraft());
        System.out.println("\nAggregate: " + aggregate);
        System.out.println("========================================\n");

        // Assert - just verify it was created successfully
        assertNotNull(aggregate);
        assertTrue(aggregate.isPublishable());
        assertEquals(2, aggregate.variants().size());
        assertEquals(3, aggregate.gallery().images().size());
    }
}
