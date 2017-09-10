package com.f_candy_d.olga.presentation.view_model;

import android.view.View;

import com.f_candy_d.vmvl.ViewModelInterface;

/**
 * Created by daichi on 9/10/17.
 */

public interface HomeViewModelInterface extends ViewModelInterface {

    void onShowSnackBarWithMessage(View view);
}
