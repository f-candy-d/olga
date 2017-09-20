package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;

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
        return new FormFragment[] {new SummaryFormFragment()};
    }

    @Override
    public void onDataInput(Bundle data, String simpleClassName) {

    }

    @Override
    protected void onSave() {

    }
}
