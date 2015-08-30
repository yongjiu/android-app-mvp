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

    @Override
    public void onError(int request_code, MvpException error) {
        V view = this.getView();
        if (view == null) return;
        view.onError(request_code, error.getCode(), error.getMessage());
    }

    public AbstractPresenter performRequest(boolean async) {
        this.performRequest(this, async);
        return this;
    }

    public AbstractPresenter performRequest(int request_code, boolean async) {
        this.performRequest(this, request_code, async);
        return this;
    }

    @Deprecated
    public AbstractPresenter sync() {
        this.performRequest(false);
        return this;
    }

    @Deprecated
    public AbstractPresenter sync(int request_code) {
        this.performRequest(request_code, false);
        return this;
    }

    @Deprecated
    public <T extends IResponse> AbstractPresenter sync(IRequest<T> request) {
        this.performRequest(request, false);
        return this;
    }

    @Deprecated
    public <T extends IResponse> AbstractPresenter sync(IRequest<T> request, int request_code) {
        this.performRequest(request, request_code, false);
        return this;
    }

    @Deprecated
    public AbstractPresenter async() {
        this.performRequest(true);
        return this;
    }

    @Deprecated
    public AbstractPresenter async(int request_code) {
        this.performRequest(request_code, true);
        return this;
    }

    @Deprecated
    public <T extends IResponse> AbstractPresenter async(IRequest<T> request) {
        this.performRequest(request, true);
        return this;
    }

    @Deprecated
    public <T extends IResponse> AbstractPresenter async(IRequest<T> request, int request_code) {
        this.performRequest(request, request_code, true);
        return this;
    }

}