package org.epam.secrettask.entity;

import java.time.Instant;

public class Secret {

    private final String secretText;
    private final Long timestamp;

    public Secret(String secretText) {
        this.secretText = secretText;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public String getSecretText() {
        return secretText;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
