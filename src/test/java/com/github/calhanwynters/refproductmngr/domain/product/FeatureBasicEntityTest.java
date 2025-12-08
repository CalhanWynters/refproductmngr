package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureBasicEntityTest {

    private FeatureIdVO id;
    private NameVO nameVO;
    private DescriptionVO description;
    private LabelVO labelVO;

    @BeforeEach
    void setUp() {
        id = new FeatureIdVO("1");
        nameVO = new NameVO("Color");
        description = new DescriptionVO("A feature representing color.");
        labelVO = new LabelVO("Blue");
    }

    @Test
    void testFeatureBasicEntityCreation() {
        // Act
        FeatureBasicEntity feature = new FeatureBasicEntity(id, nameVO, description, labelVO);

        // Assert
        assertNotNull(feature);
        assertSame(id, feature.getId());
        assertSame(nameVO, feature.getNameVO());
        assertSame(description, feature.getDescription());
        assertSame(labelVO, feature.getLabelVO());
    }

    @Test
    void testFeatureBasicEntityEquality() {
        // Arrange
        FeatureBasicEntity feature1 = new FeatureBasicEntity(id, nameVO, description, labelVO);
        FeatureBasicEntity feature2 = new FeatureBasicEntity(new FeatureIdVO("1"), nameVO, description, labelVO);

        // Assert
        assertEquals(feature1, feature2); // should be equal since they have the same ID.
    }

    @Test
    void testConstructorWithNullValues() {
        // Act & Assert for NameVO Null
        assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(id, null, description, labelVO));

        // Act & Assert for LabelVO Null
        assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(id, nameVO, description, null));

        // Act & Assert for ID Null
        assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(null, nameVO, description, labelVO));
    }

    @Test
    void testFeatureBasicEntityInequality() {
        // Arrange
        FeatureBasicEntity feature1 = new FeatureBasicEntity(id, nameVO, description, labelVO);
        FeatureBasicEntity feature2 = new FeatureBasicEntity(new FeatureIdVO("2"), new NameVO("Shape"), new DescriptionVO("A feature representing shape."), new LabelVO("Square"));

        // Assert
        assertNotNull(feature1);
        assertNotNull(feature2);
        assertNotEquals(feature1, feature2); // Assert they are NOT equal
    }
}
