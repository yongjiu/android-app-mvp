package android.app.mvp.framework;

import android.app.mvp.core.IRequest;
import android.app.mvp.core.IResponse;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public interface IPresenter {

    <T extends IResponse> void performRequest(IRequest<T> request, int request_code, boolean async);

    boolean cancelRequest(boolean mayInterruptIfRunning);

}