package net.nuclearg.kyou;

import org.apache.commons.lang.SystemUtils;

/**
 * kyou抛出的异常
 * 
 * @author ng
 */
public class KyouException extends RuntimeException {
    private static final long serialVersionUID = 6852585512405820943L;

    /**
     * Kyou加载失败时抛出此异常
     */
    public KyouException() {
        super();
    }

    /**
     * Kyou加载失败时抛出此异常
     */
    public KyouException(String message) {
        super(message);
    }

    /**
     * Kyou加载失败时抛出此异常
     */
    public KyouException(String message, Throwable cause) {
        // super(message,cause);
        super(buildMessage(message, cause), findRootCause(cause));
    }

    /**
     * Kyou加载失败时抛出此异常
     */
    public KyouException(Throwable cause) {
        // super(cause);
        super(cause.getMessage(), findRootCause(cause));
    }

    private static String buildMessage(String message, Throwable cause) {
        if (cause instanceof KyouException)
            if (cause.getCause() != null)
                return buildMessage(message + SystemUtils.LINE_SEPARATOR + " >> " + cause.getMessage(), cause.getCause());
            else
                return message + SystemUtils.LINE_SEPARATOR + " >> " + cause.getMessage();
        else
            return cause.getMessage();
    }

    private static Throwable findRootCause(Throwable cause) {
        if (cause instanceof KyouException)
            return (cause.getCause() != null && cause.getCause() != cause) ? findRootCause(cause.getCause()) : cause;
        else
            return cause;
    }
}
