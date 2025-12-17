package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureBasicEntityTest {

    private FeatureIdVO validId;
    private NameVO validName;
    private DescriptionVO validDescription;
    private LabelVO validLabel;

    @BeforeEach
    void setUp() {
        validId = FeatureIdVO.generate();
        validName = new NameVO("Basic Feature Name");
        // Ensure this description meets the assumed minimum length validation of DescriptionVO
        // (Assuming DescriptionVO is validated elsewhere to be a valid VO)
        validDescription = new DescriptionVO("A description is at least 10 characters long.");
        validLabel = new LabelVO("Feature Label");
    }

    @Test
    void testFeatureBasicEntityCreation_Success() {
        // Arrange & Act
        FeatureBasicEntity entity = new FeatureBasicEntity(
                validId,
                validName,
                validDescription,
                validLabel
        );

        // Assert
        assertNotNull(entity, "The entity should not be null after creation");
        assertEquals(validId, entity.getId(), "ID should match the provided VO.");
        assertEquals(validName, entity.getNameVO(), "Name should match the provided VO.");
        assertEquals(validDescription, entity.getDescription(), "Description VO should match the provided VO.");
        assertEquals(validLabel, entity.getLabelVO(), "Label VO should match the provided VO.");
    }

    @Test
    void testFeatureBasicEntityCreation_NullId_ThrowsException() {
        // Arrange
        FeatureIdVO nullId = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(nullId, validName, validDescription, validLabel));
        // This message assumes the check is in FeatureAbstractClass constructor
        assertEquals("Feature ID must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullNameVO_ThrowsException() {
        // Arrange
        NameVO nullName = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(validId, nullName, validDescription,validLabel));
        // This message assumes the check is in FeatureAbstractClass constructor
        assertEquals("Feature Name VO must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullLabelVO_ThrowsException() {
        // Arrange
        LabelVO nullLabel = null;

        // Act & Assert
        var exception = assertThrows(NullPointerException.class, () -> new FeatureBasicEntity(validId, validName, validDescription, nullLabel));
        // Assert the expected error message for the null label check
        assertEquals("Feature Label VO must not be null", exception.getMessage());
    }

    @Test
    void testFeatureBasicEntityCreation_NullDescriptionVO_Succeeds() {
        // FIX: The FeatureAbstractClass allows null descriptions. We test that this works as intended.
        // Arrange
        DescriptionVO nullDescription = null;

        // Act & Assert
        assertDoesNotThrow(() -> new FeatureBasicEntity(validId, validName, nullDescription, validLabel),
                "Feature creation should allow a null description VO.");

        FeatureBasicEntity entity = new FeatureBasicEntity(validId, validName, nullDescription, validLabel);
        assertNull(entity.getDescription(), "The description getter should return null if a null VO was provided.");
    }

    @Test
    void testEquality_SameId_AreEqual() {
        // Arrange
        // Create two entities with different VO instances for Name/Label, but the SAME ID
        FeatureBasicEntity entity1 = new FeatureBasicEntity(
                validId,
                new NameVO("Name One"),
                validDescription,
                validLabel
        );
        FeatureBasicEntity entity2 = new FeatureBasicEntity(
                validId, // Same ID
                new NameVO("Name Two"),
                validDescription,
                validLabel
        );

        // Act & Assert
        // Assuming equality is based solely on the 'id' field within FeatureAbstractClass
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
                validLabel
        );

        FeatureIdVO differentId = FeatureIdVO.generate();
        FeatureBasicEntity entity2 = new FeatureBasicEntity(
                differentId, // Different ID
                validName,
                validDescription,
                validLabel
        );

        // Act & Assert
        assertNotEquals(entity1, entity2, "Entities with different IDs should not be equal.");
        assertNotEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes should not match for unequal objects.");
    }
}
