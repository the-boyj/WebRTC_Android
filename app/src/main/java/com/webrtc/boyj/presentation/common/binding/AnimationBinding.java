package com.webrtc.boyj.presentation.common.binding;

import android.content.res.Resources;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;

import com.webrtc.boyj.presentation.common.view.BoyjSurfaceView;

public class AnimationBinding {
    @BindingAdapter({"callAnimation"})
    public static void callAnimation(@NonNull final BoyjSurfaceView view,
                                     final boolean isCalling) {
        if (isCalling) {
            view.startAnimation(new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.width = dp2Px(80);
                    params.height = dp2Px(120);
                    params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
                    params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
                    params.setMargins(dp2Px(24), dp2Px(24), 0, 0);
                    view.setLayoutParams(params);
                }
            });
        }
    }

    private static int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
