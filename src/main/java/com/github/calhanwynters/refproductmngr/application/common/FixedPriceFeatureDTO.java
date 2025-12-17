package com.github.calhanwynters.refproductmngr.application.common;

import java.math.BigDecimal;

public record FixedPriceFeatureDTO(String id, String name, String description, String label, BigDecimal fixedPrice) implements FeatureDTO {}

