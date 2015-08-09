package android.app.mvp.core;

import android.app.mvp.MvpException;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public interface IRequest<T extends IResponse> {

    void onSuccess(int request_code, T response);

    boolean onError(int request_code, MvpException error);

    T onRequest(int request_code);

}