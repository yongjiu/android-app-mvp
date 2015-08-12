package android.app.mvp.framework;

import android.app.mvp.MvpException;
import android.app.mvp.MvpHandler;
import android.app.mvp.core.IRequest;
import android.app.mvp.core.IResponse;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public class Presenter implements IPresenter {

    public static final int DEFAULT_REQUEST_CODE = 0;

    private RequestAsyncTask mRequestAsyncTask;

    protected RequestAsyncTask getAsyncTask() {
        return this.mRequestAsyncTask;
    }

    protected Presenter() {
        this.mRequestAsyncTask = null;
    }

    protected void onCancelled(int request_code) {
        // TODO
    }

    protected <T extends IResponse> T onRequest(int request_code, IRequest<T> request) {
        return request.onRequest(request_code);
    }

    protected <T extends IResponse> void onRespond(int request_code, IRequest<T> request, T response) {
        if (response == null || response.isSuccess()) {
            this.onSuccess(request_code, request, response);
        } else {
            this.onError(request_code, request, new MvpException(response.getCode(), response.getError()));
        }
    }

    protected <T extends IResponse> void onSuccess(int request_code, IRequest<T> request, T response) {
        if (request == null) return;
        request.onSuccess(request_code, response);
    }

    protected <T extends IResponse> void onError(int request_code, IRequest<T> request, MvpException error) {
        if (request != null && request.onError(request_code, error)) return;
        this.onCaughtException(request_code, error); // catch all exceptions
    }

    protected void onCaughtException(int request_code, MvpException error) {
        MvpHandler.caughtException(error);
    }

    protected <T extends IResponse> void performRequest(IRequest<T> request, boolean async) {
        this.performRequest(request, DEFAULT_REQUEST_CODE, async);
    }

    public <T extends IResponse> void performRequest(IRequest<T> request, int request_code, boolean async) {
        if (request == null) return;

        if (async) {
            this.cancelRequest(true); // attempts to cancel this task if it has not completed.
            this.mRequestAsyncTask = new RequestAsyncTask(request_code, request, this);
            this.mRequestAsyncTask.execute();
            return;
        }

        try {
            T response = this.onRequest(request_code, request);
            this.onRespond(request_code, request, response);
        } catch (Exception e) {
            this.onError(request_code, request, new MvpException(e));
        }
    }

    public boolean cancelRequest(boolean mayInterruptIfRunning) {
        if (this.mRequestAsyncTask == null) return true;

        boolean cancelled = this.mRequestAsyncTask.cancel(mayInterruptIfRunning);
        this.mRequestAsyncTask = null;

        return cancelled;
    }

    static class RequestAsyncTask<T extends IResponse> extends android.os.AsyncTask<Object, Object, T> {

        private final int mRequestCode;
        private final IRequest<T> mRequest;
        private final Presenter mPresenter;
        private MvpException mError;

        RequestAsyncTask(int request_code, IRequest<T> request, Presenter presenter) {
            this.mRequestCode = request_code;
            this.mRequest = request;
            this.mPresenter = presenter;
            this.mError = null;
        }

        @Override
        protected T doInBackground(Object... params) {
            try {
                return this.mPresenter.onRequest(this.mRequestCode, this.mRequest);
            } catch (Exception e) {
                this.mError = new MvpException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(T response) {
            super.onPostExecute(response);
            if (this.isCancelled()) {
                this.mPresenter.onCancelled(this.mRequestCode);
                return;
            }
            if (this.mError == null) {
                this.mPresenter.onRespond(this.mRequestCode, this.mRequest, response);
            } else {
                this.mPresenter.onError(this.mRequestCode, this.mRequest, this.mError);
            }
        }

    }

}