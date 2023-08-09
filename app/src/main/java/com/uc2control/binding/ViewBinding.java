package com.uc2control.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class ViewBinding
{
    @BindingAdapter("setVisibilityToView")
    public static void setVisibilityToView(View textView, boolean visible)
    {
        if (textView == null)
            return;
        if(textView.getVisibility() == View.VISIBLE && visible)
            return;
        if(textView.getVisibility() == View.GONE && !visible)
            return;
        if(visible)
            textView.setVisibility(View.VISIBLE);
        else
            textView.setVisibility(View.GONE);
    }
}
