package com.lifeops.truthservice.exception;

public class TruthAnalysisException extends RuntimeException {
    public TruthAnalysisException(String message) {
        super(message);
    }

    public TruthAnalysisException(String message, Throwable cause){
        super(message, cause);
    }
}
