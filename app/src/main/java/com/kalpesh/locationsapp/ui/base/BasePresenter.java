package com.kalpesh.locationsapp.ui.base;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}
