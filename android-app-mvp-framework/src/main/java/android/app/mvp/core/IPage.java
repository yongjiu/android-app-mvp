package android.app.mvp.core;

/**
 * Created by wuyongjiu@gmail.com on 15/8/8.
 */
public interface IPage extends IList {

    /**
     * Load latest page
     */
    public static final Integer PAGING_REFRESH  = null;

    /**
     * Load current page
     */
    public static final Integer PAGING_RELOAD   = 0;

    /**
     * Load the previous page
     */
    public static final Integer PAGING_PREV     = -1;

    /**
     * Load the next page
     */
    public static final Integer PAGING_NEXT     = 1;

    /**
     * get current page index
     */
    int getPage();

    /**
     * get current page size
     */
    int getSize();

    /**
     * get new page index
     */
    int paging(int paging);

    /**
     * finish paged and update page index
     */
    IPage paged();

}