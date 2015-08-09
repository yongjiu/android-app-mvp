package android.app.mvp.framework;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public interface IView {

    /**
     * @return Return <code>true</code> to prevent this event from being propagated further,
     * or <code>false</code> to indicate that you have not handled this event and it should continue to be propagated.
     */
    boolean onError(int request, int error, String message);

}