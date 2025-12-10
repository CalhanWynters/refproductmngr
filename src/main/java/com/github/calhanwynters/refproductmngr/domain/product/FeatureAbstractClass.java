package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * Abstract base class for all product features, updated to use NameVO and LabelVO.
 */
public abstract class FeatureAbstractClass {
    private final FeatureIdVO id;
    private final NameVO nameVO;      // Changed from String name
    private final DescriptionVO description;
    private final LabelVO labelVO;    // Changed from String label

    /**
     * Constructor enforces non-null checks for ID, NameVO, and LabelVO.
     * Description can be null.
     */
    public FeatureAbstractClass(FeatureIdVO id, NameVO nameVO, DescriptionVO description, LabelVO labelVO) {
        this.id = Objects.requireNonNull(id, "Feature ID must not be null");
        this.nameVO = Objects.requireNonNull(nameVO, "Feature Name VO must not be null");
        this.labelVO = Objects.requireNonNull(labelVO, "Feature Label VO must not be null");

        this.description = description;
    }

    // Getters
    public FeatureIdVO getId() { return id; }
    public NameVO getNameVO() { return nameVO; }       // Updated getter name
    public DescriptionVO getDescription() { return description; }
    public LabelVO getLabelVO() { return labelVO; }   // Updated getter name

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureAbstractClass that = (FeatureAbstractClass) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
