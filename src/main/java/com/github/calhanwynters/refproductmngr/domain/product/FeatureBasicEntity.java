package com.github.calhanwynters.refproductmngr.domain.product;

/**
 * A simple feature with no price implications (e.g., "Color: Blue").
 * This entity is immutable, inheriting its structure and validation
 * from the abstract base class, using the new NameVO and LabelVO types.
 */
public class FeatureBasicEntity extends FeatureAbstractClass {

    /**
     * Constructor delegates all parameter handling and validation
     * up to the FeatureAbstractClass constructor.
     */
    public FeatureBasicEntity(
            FeatureIdVO id,
            NameVO nameVO,        // Changed from String name
            DescriptionVO description,
            LabelVO labelVO       // Changed from String label
    ) {
        // Pass the new Value Objects up to the abstract superclass constructor
        super(id, nameVO, description, labelVO);
    }
}
