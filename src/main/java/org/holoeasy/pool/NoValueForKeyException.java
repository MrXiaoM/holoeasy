package org.holoeasy.pool;

import java.util.UUID;

public class NoValueForKeyException extends IllegalStateException {
    public NoValueForKeyException(UUID key) {
        super("No value for id '" + key + "'");
    }
}
