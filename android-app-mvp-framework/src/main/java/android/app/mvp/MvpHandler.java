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

    public static void caughtException(MvpException error) {
        MvpHandler.getHandler().onExceptionCaught(error);
    }

    protected void onExceptionCaught(MvpException error) {
        if (error == null) return;
        error.printStackTrace();
    }

}