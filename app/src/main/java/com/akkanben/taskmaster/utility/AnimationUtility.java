package com.akkanben.taskmaster.utility;

import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AnimationUtility {

    public static void setupAnimatedBackground(ConstraintLayout constraintLayout) {
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setDither(true);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }
}
