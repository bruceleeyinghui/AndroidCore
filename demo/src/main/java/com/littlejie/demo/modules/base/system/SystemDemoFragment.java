package com.littlejie.demo.modules.base.system;

import android.os.Bundle;

import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/1/24.
 */

public class SystemDemoFragment extends BaseListFragment {

    public static SystemDemoFragment newInstance() {

        Bundle args = new Bundle();

        SystemDemoFragment fragment = new SystemDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(ClipboardActivity.class);
        mLstItem.add(ShareIntentActivity.class);
        notifyDataChanged();
    }
}