package android.app.mvp.framework;

import android.app.mvp.MvpException;
import android.app.mvp.MvpHandler;
import android.app.mvp.core.IRequest;
import android.app.mvp.core.IResponse;

import java.util.*;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public class Presenter implements IPresenter {

    public static final int DEFAULT_REQUEST_CODE = 0;

    private final Map<Integer, RequestAsyncTask> mRequestAsyncTasks;

    protected List<RequestAsyncTask> taskList() {
        return new ArrayList<RequestAsyncTask>(this.tasks());
    }

    protected synchronized Collection<RequestAsyncTask> tasks() {
        return this.mRequestAsyncTasks.values();
    }

    protected synchronized RequestAsyncTask task(int request_code) {
        return this.mRequestAsyncTasks.containsKey(request_code) ? this.mRequestAsyncTasks.get(request_code) : null;
    }

    protected Presenter() {
        this.mRequestAsyncTasks = new HashMap<Integer, RequestAsyncTask>();
    }

    protected void onCancelled(int request_code, RequestAsyncTask task) {
        // TODO
    }

    protected boolean onCaughtException(int request_code, MvpException error) {
        return MvpHandler.caughtException(request_code, error);
    }

    protected <T extends IResponse> void onError(int request_code, IRequest<T> request, MvpException error) {
        if (this.onCaughtException(request_code, error) || request == null) return; // catch common exceptions
        request.onError(request_code, error);
    }

    protected <T extends IResponse> void onSuccess(int request_code, IRequest<T> request, T response) {
        if (request == null) return;
        request.onSuccess(request_code, response);
    }

    protected <T extends IResponse> void onRespond(int request_code, IRequest<T> request, T response) {
        if (response == null) throw new IllegalArgumentException("response");

        if (response.isSuccess()) {
            this.onSuccess(request_code, request, response);
        } else {
            this.onError(request_code, request, new MvpException(response.getCode(), response.getError()));
        }
    }

    protected <T extends IResponse> T onRequest(int request_code, IRequest<T> request) {
        return request.onRequest(request_code);
    }

    protected <T extends IResponse> Presenter performRequest(IRequest<T> request, boolean async) {
        return this.performRequest(request, DEFAULT_REQUEST_CODE, async);
    }

    @Override
    public <T extends IResponse> Presenter performRequest(IRequest<T> request, int request_code, boolean async) {
        if (request == null) return this;

        if (async) {
            this.cancelRequest(request_code, true); // attempts to cancel this task if it has not completed.

            RequestAsyncTask task = new RequestAsyncTask(request_code, request, this);

            synchronized (this.mRequestAsyncTasks) {
                this.mRequestAsyncTasks.put(request_code, task);
            }

            task.execute();
            return this;
        }

        try {
            T response = this.onRequest(request_code, request);
            this.onRespond(request_code, request, response);
        } catch (Exception e) {
            this.onError(request_code, request, new MvpException(e));
        }

        return this;
    }

    @Override
    public boolean cancelRequest(boolean mayInterruptIfRunning) {
        if (this.mRequestAsyncTasks.isEmpty()) return true;

        boolean cancelled = false;
        for (int request_code : this.mRequestAsyncTasks.keySet()) {
            cancelled &= this.cancelRequest(request_code, mayInterruptIfRunning);
        }

        return cancelled;
    }

    protected synchronized boolean cancelRequest(int request_code, boolean mayInterruptIfRunning) {
        if (!this.mRequestAsyncTasks.containsKey(request_code)) return false;

        RequestAsyncTask task = this.mRequestAsyncTasks.get(request_code);
        boolean cancelled = task.cancel(mayInterruptIfRunning);
        this.mRequestAsyncTasks.remove(request_code);

        return cancelled;
    }

    protected <T extends IResponse> void onRespond(RequestAsyncTask<T> task, int request_code, IRequest<T> request, T response, MvpException error) {
        this.mRequestAsyncTasks.remove(request_code);

        if (task.isCancelled()) {
            this.onCancelled(request_code, task);
            return;
        }

        if (error == null) {
            this.onRespond(request_code, request, response);
        } else {
            this.onError(request_code, request, error);
        }
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
            this.mPresenter.onRespond(this, this.mRequestCode, this.mRequest, response, this.mError);
        }

    }

}