package android.app.mvp;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public class MvpException extends Exception {

    /**
     * UncaughtExceptionHandler
     */
    public static int Uncaught = 100000000;

    private final int code;

    public int getCode() {
        return this.code;
    }

    public MvpException(Exception e) {
        this(Uncaught, e);
    }

    public MvpException(int code, Exception e) {
        super(e.getMessage(), e.getCause());
        this.code = code;
    }

    public MvpException(int code, String error) {
        super(error);
        this.code = code;
    }

}