package com.lifeops.documentservice.common;

public final class CorrelationConstants {
    public final static String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    private CorrelationConstants() {
    }
}
