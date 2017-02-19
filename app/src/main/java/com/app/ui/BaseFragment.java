package com.app.ui;


import android.support.v4.app.Fragment;

import com.app.presenter.BasePresenter;

/**
 * Created by yixijun on 2017/2/19.
 */
public class BaseFragment<T extends BasePresenter> extends Fragment {

    protected T mPresenter;


    @Override
    public void onDetach() {
        super.onDetach();
        if(mPresenter != null){
            mPresenter.clear();
        }
    }
}
