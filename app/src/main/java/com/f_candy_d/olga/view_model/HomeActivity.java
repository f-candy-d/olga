package com.f_candy_d.olga.view_model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.f_candy_d.olga.view_logic.HomeViewLogic;
import com.f_candy_d.vmvl.ActivityViewLogicInterface;
import com.f_candy_d.vmvl.ViewModelActivity;

public class HomeActivity extends ViewModelActivity implements HomeViewModelInterface {

    private HomeViewLogicInterface mViewLogic;

    @Override
    protected ActivityViewLogicInterface onCreateViewLogic() {
        mViewLogic = new HomeViewLogic(this);
        return mViewLogic;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * region; HomeViewModelInterface implementation
     */

    @Override
    public void onShowSnackBarWithMessage(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
