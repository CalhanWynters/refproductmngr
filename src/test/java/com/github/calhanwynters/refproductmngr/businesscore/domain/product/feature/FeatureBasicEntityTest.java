package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureBasicEntityTest {

    private FeatureIdVO validId;
    private NameVO validName;
    private DescriptionVO validDescription;
    private LabelVO validLabel;
    private Boolean validIsUnique;

    @BeforeEach
    void setUp() {
        validId = FeatureIdVO.generate();
        validName = new NameVO("Basic Feature Name");
        validDescription = new DescriptionVO("A text is at least 10 characters long.");
        validLabel = new LabelVO("Feature Label");
        validIsUnique = true;
    }

    @Test
    void testFeatureBasicEntityCreation_Success() {
        // Arrange & Act
        FeatureBasicEntity entity = new FeatureBasicEntity(
                validId,
                validName,
                validDescription,
                validLabel,
                validIsUnique
        );

        // Assert
        assertNotNull(entity, "The entity should not be null after creation");
        assertEquals(validId, entity.getId(), "ID should match the provided VO.");
        assertEquals(validName, entity.getNameVO(), "Name should match the provided VO.");
        assertEquals(validDescription, entity.getDescription(), "Description VO should match the provided VO.");
        assertEquals(validLabel, entity.getLabelVO(), "Label VO should match the provided VO.");
        assertEquals(validIsUnique, entity.isUnique(), "The isUnique flag should match the provided value.");
    }

    @Test
    void testFeatureBasicEntityCreation_NullId_ThrowsException() {
        // Arrange
        FeatureIdVO nullId = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () ->
                new FeatureBasicEntity(nullId, validName, validDescription, validLabel, validIsUnique));

        assertEquals("Feature ID must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullNameVO_ThrowsException() {
        // Arrange
        NameVO nullName = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () ->
                new FeatureBasicEntity(validId, nullName, validDescription, validLabel, validIsUnique));

        assertEquals("Feature Name VO must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullLabelVO_ThrowsException() {
        // Arrange
        LabelVO nullLabel = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () ->
                new FeatureBasicEntity(validId, validName, validDescription, nullLabel, validIsUnique));

        assertEquals("Feature Label VO must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullDescriptionVO_Succeeds() {
        // Arrange
        DescriptionVO nullDescription = null;

        // Act
        FeatureBasicEntity entity = new FeatureBasicEntity(validId, validName, nullDescription, validLabel, validIsUnique);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getDescription(), "The description getter should return null if a null VO was provided.");
    }

    @Test
    void testEquality_SameId_AreEqual() {
        // Arrange
        // Entities are compared by ID in DDD; other fields can differ
        FeatureBasicEntity entity1 = new FeatureBasicEntity(
                validId,
                new NameVO("Name One"),
                validDescription,
                validLabel,
                true
        );
        FeatureBasicEntity entity2 = new FeatureBasicEntity(
                validId,
                new NameVO("Name Two"),
                validDescription,
                validLabel,
                false
        );

        // Act & Assert
        assertEquals(entity1, entity2, "Entities with the same ID should be equal.");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes should match for equal objects.");
    }

    @Test
    void testEquality_DifferentId_AreNotEqual() {
        // Arrange
        FeatureBasicEntity entity1 = new FeatureBasicEntity(
                validId,
                validName,
                validDescription,
                validLabel,
                validIsUnique
        );

        FeatureIdVO differentId = FeatureIdVO.generate();
        FeatureBasicEntity entity2 = new FeatureBasicEntity(
                differentId,
                validName,
                validDescription,
                validLabel,
                validIsUnique
        );

        // Act & Assert
        assertNotEquals(entity1, entity2, "Entities with different IDs should not be equal.");
        assertNotEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes should not match for unequal objects.");
    }
}
