package com.llkj.cm.restfull.exception;

/**
 * Thrown to indicate that a compulsory parameter is missing.
 * 
 * @author Foxykeep
 */
public class ParsingException extends Exception {

    private static final long serialVersionUID = -6031863210486494461L;

    /**
     * Constructs a new {@link ParsingException} that includes the current stack
     * trace.
     */
    public ParsingException() {
        super();
    }

    /**
     * Constructs a new {@link ParsingException} that includes the current stack
     * trace, the specified detail message and the specified cause.
     * 
     * @param detailMessage the detail message for this exception.
     * @param throwable the cause of this exception.
     */
    public ParsingException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@link ParsingException} that includes the current stack
     * trace and the specified detail message.
     * 
     * @param detailMessage the detail message for this exception.
     */
    public ParsingException(final String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@link ParsingException} that includes the current stack
     * trace and the specified cause.
     * 
     * @param throwable the cause of this exception.
     */
    public ParsingException(final Throwable throwable) {
        super(throwable);
    }

}
