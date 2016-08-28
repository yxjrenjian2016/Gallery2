package com.app.base;

import android.app.Activity;
import android.os.Bundle;
import com.app.presenter.BasePresenter;


/**
 * BaseActivity for do something.
 */
public abstract class BaseActivity<T extends BasePresenter> extends Activity {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( mPresenter != null){
            mPresenter.clear();
        }
    }

}
