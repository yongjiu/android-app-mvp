package android.app.mvp.framework;

/**
 * Created by yongjiu on 15/8/8.
 */
public interface IPageModel extends IModel {

    IPageModel setPage(long page, int size);

}