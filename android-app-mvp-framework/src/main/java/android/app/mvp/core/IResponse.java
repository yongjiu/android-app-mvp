package android.app.mvp.core;

/**
 * Created by wuyongjiu@gmail.com on 15/8/1.
 */
public interface IResponse {

    boolean isSuccess();

    int getCode();

    String getError();

}