package com.github.calhanwynters.refproductmngr.application.common;

public record BasicFeatureDTO(String id, String name, String description, String label) implements FeatureDTO {}