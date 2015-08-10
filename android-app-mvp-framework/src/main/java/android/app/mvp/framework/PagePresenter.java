package android.app.mvp.framework;

import android.app.mvp.core.IPage;
import android.app.mvp.core.IResponse;

/**
 * Created by wuyongjiu@gmail.com on 15/8/8.
 */
public abstract class PagePresenter<V extends IView, M extends IModel, R extends IResponse> extends ListPresenter<V, M, R> implements IPage {

    private Integer mPaging;
    private int mPage;
    private int mSize;

    protected PagePresenter(V view) {
        super(view);
    }

    @Override
    public int getPage() {
        return this.mPage;
    }

    @Override
    public int getSize() {
        return this.mSize;
    }

    @Override
    public IPage paged() {
        this.setPage(this.paging()).mPaging = PAGING_RELOAD;
        return this;
    }

    @Override
    public int paging(int paging) {
        this.mPaging = paging;
        return this.paging();
    }

    protected int paging() {
        return this.mPaging == null ? 0 : this.getPage() + this.mPaging;
    }

    protected PagePresenter setPage(int page) {
        this.mPage = page;
        return this;
    }

    protected PagePresenter setSize(int size) {
        this.mSize = size;
        return this;
    }

}