package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import java.util.Objects;

/**
 * Abstract base class for Product Features (Entities).
 * Identity is determined by FeatureIdVO.
 */
public abstract class FeatureAbstractClass {
    private final FeatureIdVO id;
    private final NameVO nameVO;
    private final LabelVO labelVO;
    private final DescriptionVO description; // Optional
    private final boolean isUnique;

    protected FeatureAbstractClass(
            FeatureIdVO id,
            NameVO nameVO,
            LabelVO labelVO,
            DescriptionVO description,
            boolean isUnique) {
        this.id = Objects.requireNonNull(id, "Feature ID must not be null");
        this.nameVO = Objects.requireNonNull(nameVO, "Feature Name VO must not be null");
        this.labelVO = Objects.requireNonNull(labelVO, "Feature Label VO must not be null");
        this.description = description;
        this.isUnique = isUnique; // A boolean value to turn true if feature is unique for use on only 1 variant.
    }

    public FeatureIdVO getId() { return id; }
    public NameVO getNameVO() { return nameVO; }
    public LabelVO getLabelVO() { return labelVO; }
    public DescriptionVO getDescription() { return description; }
    public boolean isUnique() { return isUnique; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureAbstractClass that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
