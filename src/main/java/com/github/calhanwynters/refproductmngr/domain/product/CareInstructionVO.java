package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Domain value object representing the specific care instructions for a product.
 * Uses whitelisting for enhanced cybersecurity.
 */
public record CareInstructionVO(String instructions) {

    // Define allowed characters for robust security (whitelisting):
    // Allows: letters (upper/lower), numbers, spaces, common punctuation (.,:;!?-),
    // newlines (\n), bullet points (*•), and numbering notation.
    private static final String ALLOWED_CHARS_REGEX = "[a-zA-Z0-9 .,:;!\\-?\\n*•\\d()\\[\\]]+";
    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile(ALLOWED_CHARS_REGEX);
    private static final int MAX_LENGTH = 500;


    public CareInstructionVO {
        Objects.requireNonNull(instructions, "Instructions cannot be null");
        if (instructions.isBlank()) {
            throw new IllegalArgumentException("Instructions cannot be empty or blank");
        }
        if (instructions.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Instructions cannot exceed " + MAX_LENGTH + " characters");
        }

        // --- Cybersecurity Enhancement: Whitelisting ---
        // Ensure ALL characters in the input match the safe pattern.
        if (!ALLOWED_CHARS_PATTERN.matcher(instructions).matches()) {
            throw new IllegalArgumentException("Instructions contain forbidden characters. Only letters, numbers, spaces, and common punctuation are allowed.");
        }
        // ----------------------------------------------

        // Semantic/Formatting checks are still valid business rules:
        if (!instructions.startsWith("-") && !instructions.startsWith("*") && !instructions.startsWith("1.")) {
            // Added '*' to startWith check as it's a common bullet point character
            throw new IllegalArgumentException("Instructions should start with a bullet point (* or -) or numbering (1.).");
        }

        // Remove the blacklist/HTML checks as the whitelist already covers them implicitly.
    }

    public String display() {
        return instructions;
    }
}
