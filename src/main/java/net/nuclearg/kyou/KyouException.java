package net.nuclearg.kyou;

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
        super(message, cause);
    }

    /**
     * Kyou加载失败时抛出此异常
     */
    public KyouException(Throwable cause) {
        super(cause);
    }

}
