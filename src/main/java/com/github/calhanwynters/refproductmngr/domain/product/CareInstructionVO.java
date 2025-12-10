package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * Domain value object representing the specific care instructions for a product.
 */
public record CareInstructionVO(String instructions) {
    public CareInstructionVO {
        Objects.requireNonNull(instructions, "Instructions cannot be null");
        if (instructions.isBlank()) {
            throw new IllegalArgumentException("Instructions cannot be empty or blank");
        }
        if (instructions.length() > 500) { // Example maximum length
            throw new IllegalArgumentException("Instructions cannot exceed 500 characters");
        }
        String[] forbiddenWords = {"forbiddenWord1", "forbiddenWord2"};

        for (String forbidden : forbiddenWords) {
            if (instructions.toLowerCase().contains(forbidden)) {
                throw new IllegalArgumentException("Instructions contain forbidden words.");
            }
        }

        if (!instructions.startsWith("-") && !instructions.startsWith("1.")) {
            throw new IllegalArgumentException("Instructions should start with a bullet point or numbering.");
        }

        if (instructions.contains("<") || instructions.contains(">")) {
            throw new IllegalArgumentException("Instructions must not contain HTML tags.");
        }



    }

    public String display() {
        return instructions;
    }
}