package com.llkj.cm.restfull.exception;

/**
 * Thrown to indicate that a compulsory parameter is missing.
 * 
 * @author Foxykeep
 */
public class CompulsoryParameterException extends RuntimeException {

    private static final long serialVersionUID = -6031863210486494461L;

    /**
     * Constructs a new {@link CompulsoryParameterException} that includes the
     * current stack trace.
     */
    public CompulsoryParameterException() {
        super();
    }

    /**
     * Constructs a new {@link CompulsoryParameterException} that includes the
     * current stack trace, the specified detail message and the specified
     * cause.
     * 
     * @param detailMessage the detail message for this exception.
     * @param throwable the cause of this exception.
     */
    public CompulsoryParameterException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@link CompulsoryParameterException} that includes the
     * current stack trace and the specified detail message.
     * 
     * @param detailMessage the detail message for this exception.
     */
    public CompulsoryParameterException(final String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@link CompulsoryParameterException} that includes the
     * current stack trace and the specified cause.
     * 
     * @param throwable the cause of this exception.
     */
    public CompulsoryParameterException(final Throwable throwable) {
        super(throwable);
    }

}
