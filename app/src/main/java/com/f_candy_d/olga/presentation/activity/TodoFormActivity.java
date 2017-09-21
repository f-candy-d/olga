package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.olga.presentation.fragment.SummaryFormFragment;

/**
 * Created by daichi on 9/20/17.
 */

public class TodoFormActivity extends FormActivity {

    @Override
    protected void onInitWithContentId(long contentId) {

    }

    @Override
    protected void onInit() {

    }

    @Override
    protected FormFragment[] getFormFragments() {
        return new FormFragment[] {new SummaryFormFragment(), new SummaryFormFragment()};
    }

    @Override
    public void onDataInput(Bundle data, String simpleFragmentClassName) {

    }

    @Override
    protected void onSave() {

    }

    @NonNull
    @Override
    public Style getStyle() {
        Style style = super.getStyle();
        style.colorPrimary = Utils.getColor(R.color.color_cream_blue);
        style.colorSecondary = Utils.getColor(R.color.color_green_dark);
        return style;
    }
}
