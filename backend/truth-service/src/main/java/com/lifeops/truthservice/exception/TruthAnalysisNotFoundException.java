package com.lifeops.truthservice.exception;

import java.util.UUID;

public class TruthAnalysisNotFoundException extends RuntimeException {
    public TruthAnalysisNotFoundException(UUID id) {
        super("Truth analysis not found with id: "+id);
    }
}
