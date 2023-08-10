package com.uc2control.binding;

import android.graphics.Bitmap;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
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

    @BindingAdapter("setImageToImageView")
    public static void setImageToImageView(AppCompatImageView textView, Bitmap bitmap)
    {
        if (textView != null && bitmap != null)
            textView.setImageBitmap(bitmap);
    }
}
