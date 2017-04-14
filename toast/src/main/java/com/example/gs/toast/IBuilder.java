package com.example.gs.toast;

import android.view.View;

/**
 * 这样设置的目的是参数设置为先,模拟okhttp
 */
public interface IBuilder {

    IBuilder setGravity(int gravity, int xOffset, int yOffset);

    IBuilder setDuration(long durationMillis);

    /**
     * @param isStandard
     * @return 是否支持单列
     */
    IBuilder setIsSingle(boolean isStandard);

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    IBuilder setView(View view);

    /**
     * @param horizontalMargin
     * @param verticalMargin
     * @return
     * @link
     */
    IBuilder setMargin(float horizontalMargin, float verticalMargin);

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    IBuilder setText(String text);



    IToast
    build();
}