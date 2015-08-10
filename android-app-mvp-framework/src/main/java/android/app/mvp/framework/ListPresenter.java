package android.app.mvp.framework;

import android.app.mvp.core.IList;
import android.app.mvp.core.IResponse;

/**
 * Created by wuyongjiu@gmail.com on 15/8/8.
 */
public abstract class ListPresenter<V extends IView, M extends IModel, R extends IResponse> extends AbstractPresenter<V, M, R> implements IList {

    private int mTotal;

    protected ListPresenter(V view) {
        super(view);
    }

    @Override
    public int getTotal() {
        return this.mTotal;
    }

    protected ListPresenter setTotal(int total) {
        this.mTotal = total;
        return this;
    }

}