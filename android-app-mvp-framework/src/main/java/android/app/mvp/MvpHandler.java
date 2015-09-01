package android.app.mvp;

/**
 * Mvp Exception Handler
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public class MvpHandler {

    private static MvpHandler mHandler;

    public static void override(MvpHandler handler) {
        mHandler = handler;
    }

    private static MvpHandler getHandler() {
        if (mHandler == null) {
            override(new MvpHandler());
        }
        return mHandler;
    }

    public static boolean caughtException(int request_code, MvpException error) {
        return MvpHandler.getHandler().onExceptionCaught(request_code, error);
    }

    protected boolean onExceptionCaught(int request_code, MvpException error) {
        if (error != null) error.printStackTrace();
        return false;
    }

}