package com.lansman.allproject.StatueCallBack;


import com.kingja.loadsir.callback.Callback;
import com.lansman.allproject.R;

/**
 * Author：zixin on 2017/10/8 16:03
 * E-mail：lanshenming@linghit.com
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.loading_layout;
    }
}
