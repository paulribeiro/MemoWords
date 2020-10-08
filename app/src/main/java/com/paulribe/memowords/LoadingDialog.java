package com.paulribe.memowords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;
    private AnimationDrawable animationDrawable;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();


        dialog = builder.create();
        dialog.show();


        final Window window = dialog.getWindow();
        View loadingView = inflater.inflate(R.layout.memoword_progress_dialog, null);

        ImageView loadingImage = loadingView.findViewById(R.id.main_progress);
        TextView loadingMessage = loadingView.findViewById(R.id.loading_message);
        loadingImage.setBackgroundResource(R.drawable.loader_animation);
        animationDrawable = (AnimationDrawable)loadingImage.getBackground();
        animationDrawable.start();
        loadingMessage.setText(message);
        builder.setCancelable(false);
        window.setContentView(loadingView);

        final ConstraintLayout dialogContainer = window.findViewById(R.id.container);
        dialogContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = dialogContainer.getWidth();
                window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                dialogContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void dismissDialog(){
        animationDrawable.stop();
        dialog.dismiss();
    }
}
