package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public record ImageUrlVO(String url) {
    public ImageUrlVO {
        Objects.requireNonNull(url, "URL cannot be null");

        if (!url.startsWith("http") && !url.startsWith("https")) {
            throw new IllegalArgumentException("Invalid URL scheme; must start with http or https.");
        }

        try {
            new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format", e);
        }

        if (url.length() > 2048) {
            throw new IllegalArgumentException("URL is too long.");
        }
    }
}
