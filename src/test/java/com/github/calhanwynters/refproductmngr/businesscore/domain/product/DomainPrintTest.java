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

public class DomainPrintTest {
    @Test
    void printFeatureBasicEntity() {
        // Arrange
        FeatureIdVO id = FeatureIdVO.generate();
        NameVO name = new NameVO("Sample Feature Name");
        DescriptionVO description = new DescriptionVO("This is a sample feature text for testing purposes.");
        LabelVO label = new LabelVO("Sample Label");
        boolean isUnique = true; // Added for 2025 constructor signature

        FeatureBasicEntity entity = new FeatureBasicEntity(id, name, description, label, isUnique);

        // Act & Print
        System.out.println("=== FeatureBasicEntity Print Test ===");
        System.out.println("ID: " + entity.getId());
        System.out.println("Name: " + entity.getNameVO().value());
        System.out.println("Description: " + entity.getDescription().text());
        System.out.println("Label: " + entity.getLabelVO().value());
        System.out.println("Is Unique: " + entity.isUnique());
        System.out.println("Entity: " + entity);
        System.out.println("======================================\n");

        // Assert
        assertNotNull(entity);
        assertTrue(entity.isUnique());
    }

    @Test
    void printFeatureFixedPriceEntity() {
        // Arrange
        // Using valid UUID format for 2025 FeatureIdVO compatibility
        FeatureIdVO id = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        NameVO name = new NameVO("Gift Wrapping");
        DescriptionVO description = new DescriptionVO("A beautiful gift wrap for your purchase.");
        LabelVO label = new LabelVO("Gift Wrap");
        BigDecimal fixedPrice = new BigDecimal("5.00");
        boolean isUnique = false;

        FeatureFixedPriceEntity entity = new FeatureFixedPriceEntity(id, name, description, label, fixedPrice, isUnique);

        // Act & Print
        System.out.println("=== FeatureFixedPriceEntity Print Test (2025) ===");
        System.out.println("ID: " + entity.getId().value());
        System.out.println("Name: " + entity.getNameVO().value());
        System.out.println("Description: " + (entity.getDescription() != null ? entity.getDescription().text() : "N/A"));
        System.out.println("Label: " + entity.getLabelVO().value());
        System.out.println("Fixed Price: $" + entity.getFixedPrice());
        System.out.println("Is Unique: " + entity.isUnique());
        System.out.println("ToString: " + entity);
        System.out.println("==========================================\n");

        // Assert
        assertNotNull(entity, "Entity should be instantiated successfully");
        // Compare using compareTo to handle potential scale differences (5.00 vs 5.0)
        assertEquals(0, new BigDecimal("5.00").compareTo(entity.getFixedPrice()), "Fixed price should match provided value");
    }

    @Test
    void printFeatureScalingPriceEntity() {
        // Arrange
        FeatureIdVO id = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"); // It's possible to extract method returning 'entity' from a long surrounding method
        NameVO name = new NameVO("Custom Fabric");
        DescriptionVO description = new DescriptionVO("Custom length of fabric for your needs.");
        LabelVO label = new LabelVO("Custom Fabric Length");
        MeasurementUnitVO measurementUnit = new MeasurementUnitVO("meters");
        BigDecimal baseAmount = new BigDecimal("10.00");
        BigDecimal incrementAmount = new BigDecimal("2.50");
        int maxQuantity = 100;
        boolean isUnique = false;

        FeatureScalingPriceEntity entity = new FeatureScalingPriceEntity(
                id, name, description, label, measurementUnit,
                baseAmount, incrementAmount, maxQuantity, isUnique
        );

        // Act & Print
        System.out.println("=== FeatureScalingPriceEntity Print Test (2025) ===");
        System.out.println("ID: " + entity.getId().value());
        System.out.println("Name: " + entity.getNameVO().value());
        System.out.println("Description: " + (entity.getDescription() != null ? entity.getDescription().text() : "N/A"));
        System.out.println("Label: " + entity.getLabelVO().value());
        System.out.println("Measurement Unit: " + entity.getMeasurementUnit().unit());
        System.out.println("Base Amount: $" + entity.getBaseAmount());
        System.out.println("Increment Amount: $" + entity.getIncrementAmount() + " per unit");
        System.out.println("Max Quantity: " + entity.getMaxQuantity());
        System.out.println("Is Unique: " + entity.isUnique());
        System.out.println("\n--- Price Scaling Examples ---");
        System.out.println("1 unit:   $" + entity.calculateTotalPrice(1));
        System.out.println("5 units:  $" + entity.calculateTotalPrice(5));
        System.out.println("10 units: $" + entity.calculateTotalPrice(10));
        System.out.println("25 units: $" + entity.calculateTotalPrice(25));
        System.out.println("50 units: $" + entity.calculateTotalPrice(50));
        System.out.println("\nToString: " + entity);
        System.out.println("============================================\n");

        // Assert
        assertNotNull(entity);
        // Calculation logic check: base(10.00) + (5 * 2.50) = 22.50
        BigDecimal expectedTotal = new BigDecimal("22.50");
        assertEquals(0, expectedTotal.compareTo(entity.calculateTotalPrice(5)), "Total price for 5 units should be 22.50");
    }



    @Test
    void printVariantEntity() {
        // Arrange
        VariantIdVO id = VariantIdVO.generate();
        SkuVO sku = new SkuVO("SKU-12345");
        PriceVO basePrice = new PriceVO(new BigDecimal("10.00"), 2, Currency.getInstance("USD"));
        PriceVO currentPrice = new PriceVO(new BigDecimal("9.99"), 2, Currency.getInstance("USD"));

        // Create sample features with 2025 isUnique property
        FeatureBasicEntity feature1 = new FeatureBasicEntity(
                FeatureIdVO.generate(),
                new NameVO("Extra Padding"),
                new DescriptionVO("Additional comfort padding for extended wear."),
                new LabelVO("Padding"),
                false // isUnique
        );

        FeatureFixedPriceEntity feature2 = new FeatureFixedPriceEntity(
                new FeatureIdVO("b1ffbc99-9c0b-4ef8-bb6d-6bb9bd380a22"),
                new NameVO("Gift Box"),
                new DescriptionVO("Premium gift box packaging with ribbon."),
                new LabelVO("Gift Box"),
                new BigDecimal("3.50"),
                true // isUnique
        );

        Set<FeatureAbstractClass> features = Set.of(feature1, feature2);

        // Replaced mocks with concrete VOs for better print output
        CareInstructionVO careInstructions = new CareInstructionVO("* Dry clean only.");
        WeightVO weight = new WeightVO(new BigDecimal("0.75"), WeightUnitEnums.KILOGRAM);
        VariantStatusEnums status = VariantStatusEnums.ACTIVE;

        VariantEntity entity = new VariantEntity(
                id, sku, basePrice, currentPrice, features, careInstructions, weight, status
        );

        // Act & Print
        System.out.println("=== VariantEntity Print Test (2025-12-20) ===");
        System.out.println("ID: " + entity.id().value());
        System.out.println("SKU: " + entity.sku().sku());
        System.out.println("Base Price: " + entity.basePrice().value() + " " + entity.basePrice().currency().getCurrencyCode());
        System.out.println("Current Price: " + entity.currentPrice().value() + " " + entity.currentPrice().currency().getCurrencyCode());
        System.out.println("Status: " + entity.status());
        System.out.println("Is Active: " + entity.isActive());
        System.out.println("Weight: " + entity.weight().amount() + " " + entity.weight().unit());
        System.out.println("Care Instructions: " + entity.careInstructions().instructions());
        System.out.println("\n--- Features (" + entity.getFeatures().size() + ") ---");
        entity.getFeatures().forEach(feature ->
                System.out.println("  - " + feature.getLabelVO().value() + " [Unique: " + feature.isUnique() + "]"));
        System.out.println("\nFull Entity Record: " + entity);
        System.out.println("=====================================\n");

        // Assert
        assertNotNull(entity);
        assertTrue(entity.isActive());
        assertEquals(2, entity.getFeatures().size());
    }

    @Test
    void printProductAggregate() {
        // Arrange
        ProductIdVO productId = new ProductIdVO(UUID.randomUUID().toString());
        BusinessIdVO businessId = BusinessIdVO.random();
        CategoryVO category = new CategoryVO("Outdoor Apparel");
        DescriptionVO description = new DescriptionVO("Premium Cotton T-Shirt with custom options");
        VersionVO version = new VersionVO(1); // Record using 'num'
        boolean isDeleted = false;

        List<ImageUrlVO> images = List.of(
                new ImageUrlVO("https://example.com/product-front.jpg"),
                new ImageUrlVO("https://example.com/product-back.jpg"),
                new ImageUrlVO("https://example.com/product-detail.jpg")
        );
        GalleryVO gallery = new GalleryVO(images);

        // Shared attributes for concrete print
        CareInstructionVO care = new CareInstructionVO("* Machine wash cold.");
        WeightVO weight = new WeightVO(new BigDecimal("0.25"), WeightUnitEnums.KILOGRAM);

        VariantEntity variant1 = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-SMALL-BLUE"),
                new PriceVO(new BigDecimal("25.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("19.99"), 2, Currency.getInstance("USD")),
                Set.of(),
                care,
                weight,
                VariantStatusEnums.ACTIVE
        );

        VariantEntity variant2 = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-LARGE-RED"),
                new PriceVO(new BigDecimal("27.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("27.00"), 2, Currency.getInstance("USD")),
                Set.of(),
                care,
                weight,
                VariantStatusEnums.DRAFT
        );

        Set<VariantEntity> variants = Set.of(variant1, variant2);

        ProductAggregate aggregate = new ProductAggregate(
                productId, businessId, category, description, gallery, variants, version, isDeleted
        );

        // Act & Print
        System.out.println("=== ProductAggregate Print Test (2025-12-20) ===");
        System.out.println("Product ID: " + aggregate.id().value());
        System.out.println("Business ID: " + aggregate.businessIdVO().value());
        System.out.println("Category: " + aggregate.category().value());
        System.out.println("Description: " + aggregate.description().text());
        System.out.println("Schema/Business Version: " + aggregate.version().num()); // num() for record
        System.out.println("Is Deleted: " + aggregate.isDeleted());
        System.out.println("\n--- Gallery (" + aggregate.gallery().images().size() + " images) ---");
        aggregate.gallery().images().forEach(img -> System.out.println("  - " + img.url()));
        System.out.println("\n--- Variants (" + aggregate.variants().size() + ") ---");
        aggregate.variants().forEach(variant ->
                System.out.println("  - SKU: " + variant.sku().sku() + " | Status: " + variant.status() + " | Current Price: " + variant.currentPrice().value())
        );
        System.out.println("\n--- Business Logic Verification ---");
        System.out.println("Has Minimum Images (>=3): " + aggregate.hasMinimumImages());
        System.out.println("Has Active Variants: " + aggregate.hasActiveVariants());
        System.out.println("Is Publishable (Active + Imgs + Not Deleted): " + aggregate.isPublishable());
        System.out.println("\nFull Aggregate Snapshot: " + aggregate);
        System.out.println("========================================\n");

        // Assert
        assertNotNull(aggregate);
        assertFalse(aggregate.isDeleted());
        assertEquals(1, aggregate.version().num());
        assertEquals(2, aggregate.variants().size());
        assertEquals(3, aggregate.gallery().images().size());
    }

}
