package android.app.mvp.framework;

import android.app.mvp.MvpException;
import android.app.mvp.core.IRequest;
import android.app.mvp.core.IResponse;

import java.lang.ref.WeakReference;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public abstract class AbstractPresenter<V extends IView, M extends IModel, R extends IResponse> extends Presenter implements IRequest<R> {

    protected abstract M getModel();

    private final WeakReference<V> mView;

    protected V getView() {
        return this.mView.get();
    }

    protected AbstractPresenter(V view) {
        this.mView = new WeakReference<V>(view);
    }

    protected void performRequest(boolean async) {
        this.performRequest(this, async);
    }

    protected void performRequest(int request_code, boolean async) {
        this.performRequest(this, request_code, async);
    }

    @Override
    public boolean onError(int request_code, MvpException error) {
        V view = this.getView();
        return view != null && view.onError(request_code, error.getCode(), error.getMessage());
    }

}