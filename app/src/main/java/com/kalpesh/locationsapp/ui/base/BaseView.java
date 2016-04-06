package com.kalpesh.locationsapp.ui.base;

import android.content.Context;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public interface BaseView {
    Context getContext();

    void setShowLoading(boolean show);

    void showErrorMessage(String message);
}
