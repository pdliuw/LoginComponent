package com.air.logincomponent.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.logincomponent.R;
import com.air.logincomponent.di.glide.GlideApp;

/**
 * @author pd_liu on 2018/3/13.
 * <p>
 * 加载框
 * </p>
 */

public class AppLoadingDialogFragment extends AppCompatDialogFragment {

    private TextView mDescription;

    public static AppLoadingDialogFragment newInstance() {
        return new AppLoadingDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.from(getContext()).inflate(R.layout.app_loading_dialog_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //No title.
        setCancelable(false);
        setStyle(STYLE_NO_TITLE, getTheme());
        mDescription = view.findViewById(R.id.loading_description);

        ImageView gifImg = view.findViewById(R.id.loading_gif_img);

        GlideApp.with(this)
                .asGif()
                .load(R.drawable.app_dialog_loading)
                .into(gifImg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //request a window without the title.
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * 此方法解决
     * illegalstateexception fragment already added dialogfragment
     *
     * @param manager
     * @param tag
     */
    public void showDialog(FragmentManager manager, String tag) {
        if (manager.isDestroyed()) {
            //避免，当Activity退出后台时添加Fragment导致的问题
            return;
        }
        //调用系统的
        show(manager, tag);
        //加入此句话
        manager.executePendingTransactions();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    public void setLoadingDescription(String description) {
        mDescription.setText(description);
    }
}
