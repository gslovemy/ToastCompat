package com.example.gs.toast;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gs on 2017/4/13.
 * 10:42
 * <p>
 * com.example.gs.toastcompat
 * 自定义的toast
 */

public class CustomToast implements IToast {

    private static Handler mHandler = new Handler();

    /**
     * 维护toast的队列
     */
    private static BlockingQueue<CustomToast> mQueue = new LinkedBlockingDeque<>();

    /**
     * 原子操作：判断当前是否在读取{@linkplain #mQueue 队列}来显示toast
     */
    private static AtomicInteger mAtomicInteger = new AtomicInteger(0);
    private WindowManager mWindowManager;
    private long mDurationMillis;
    private WindowManager.LayoutParams mParams;
    private View mView;

    /**
     * @param iBuilder
     */
    public CustomToast(IBuilder iBuilder) {
        Builder mbuilder = (Builder) iBuilder;
//        this.mView = mbuilder.mBuilderView;
        this.mWindowManager = mbuilder.mBuilderWindowManager;
        this.mDurationMillis = mbuilder.mBuilderDurationMillis;
        this.mParams = mbuilder.mBuilderParams;
    }


    @Override
    public void show() {
        // 1. 将本次需要显示的toast加入到队列中
        mQueue.offer(this);

        Log.e("setDefaultStyle:", mQueue.size() + "");
        // 2. 如果队列还没有激活，就激活队列，依次展示队列中的toast
        if (0 == mAtomicInteger.get()) {
            Log.e("setDefaultStyle:", "尽量了");
            mAtomicInteger.incrementAndGet();
            mHandler.post(mActivite);
        }
    }

    @Override
    public void cancel() {
        // 1. 如果队列已经处于非激活状态或者队列没有toast了，就表示队列没有toast正在展示了，直接return
        if (0 == mAtomicInteger.get() && mQueue.isEmpty()) return;

        // 2. 当前显示的toast是否为本次要取消的toast，如果是的话
        // 2.1 先移除之前的队列逻辑
        // 2.2 立即暂停当前显示的toast
        // 2.3 重新激活队列
        if (this.equals(mQueue.peek())) {
            mHandler.removeCallbacks(mActivite);
            mHandler.post(mHide);
            mHandler.post(mActivite);
        }
    }

    private void handleShow() {
        if (mView != null) {
            if (mView.getParent() != null) {
                mWindowManager.removeView(mView);
            }
            mWindowManager.addView(mView, mParams);
        }
    }

    private void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWindowManager.removeView(mView);
                // 同时从队列中移除这个toast
                mQueue.poll();
            }
            mView = null;
        }
    }

    private static void activeQueue() {
        CustomToast toast = mQueue.peek();
        if (toast == null) {
            // 如果不能从队列中获取到toast的话，那么就表示已经暂时完所有的toast了
            // 这个时候需要标记队列状态为：非激活读取
            mAtomicInteger.decrementAndGet();
            Log.e("setDefaultStyle:", "出来了");
        } else {
            // 如果还能从队列中获取到toast的话，那么就表示还有toast没有展示
            // 1. 展示队首的toast
            // 2. 设置一定时间后主动采取toast消失措施
            // 3. 设置展示完毕之后再次执行本逻辑，以展示下一个toast
            mHandler.post(toast.mShow);
            mHandler.postDelayed(toast.mHide, toast.mDurationMillis);
            mHandler.postDelayed(mActivite, toast.mDurationMillis);
            if (toast.mView == null) {
                mAtomicInteger.decrementAndGet();
                mHandler.removeCallbacksAndMessages(null);
            }
        }

    }


    public static class Builder implements IBuilder {

        private WindowManager mBuilderWindowManager;
        private View mBuilderView;

        private WindowManager.LayoutParams mBuilderParams;
        private boolean mBuilderIsSingle;  //是否单列
        private long mBuilderDurationMillis = Toast.LENGTH_SHORT; //时间
        private String mBuilderContent;   //弹出内容
        private Context mContext;
        private static IToast mIToast;

        /**
         * 默认的构造函数进行数据初始化
         */
        public Builder(Context context) {
            this.mContext = context;
            this.mBuilderWindowManager = (WindowManager) mContext.getSystemService(Context
                    .WINDOW_SERVICE);
            mBuilderParams = new WindowManager.LayoutParams();
            mBuilderParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mBuilderParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mBuilderParams.format = PixelFormat.TRANSLUCENT;
            mBuilderParams.windowAnimations = android.R.style.Animation_Toast;
            mBuilderParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mBuilderParams.setTitle("Toast");
            mBuilderParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager
                    .LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            // 默认居中
            mBuilderParams.gravity = Gravity.CENTER;
        }

        /**
         * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
         *
         * @param view 传入view
         * @return 自身对象
         */
        @Override
        public IBuilder setView(View view) {
            mBuilderView = view;
            return this;
        }


        @Override
        public CustomToast build() {
            if (mBuilderIsSingle) {
                if (mIToast == null)
                    mIToast = new CustomToast(this);
            } else {
                mIToast = new CustomToast(this);
                setGravity(Gravity.CENTER, 0, 0);
            }
            ((CustomToast) mIToast).mView = mBuilderView;
            return (CustomToast) mIToast;
        }

        /**
         * @param text 字符串
         * @return 自身对象
         */
        @Override
        public IBuilder setText(String text) {
            // 模拟Toast的布局文件 com.android.internal.R.layout.transient_notification
            // 虽然可以手动用java写，但是不同厂商系统，这个布局的设置好像是不同的，因此我们自己获取原生Toast的view进行配置
            View view = Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView();
            if (view != null) {
                TextView tv = (TextView) view.findViewById(android.R.id.message);
                tv.setText(text);
                setView(view);
            }
            return this;
        }

        @Override
        public IBuilder setMargin(float horizontalMargin, float verticalMargin) {
            mBuilderParams.horizontalMargin = horizontalMargin;
            mBuilderParams.verticalMargin = verticalMargin;
            return this;
        }

        /**
         * Set the location at which the notification should appear on the screen.
         *
         * @param gravity
         * @param xOffset
         * @param yOffset
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public IBuilder setGravity(int gravity, int xOffset, int yOffset) {

            // We can resolve the Gravity here by using the Locale for getting
            // the layout direction
            final int finalGravity;
            if (Build.VERSION.SDK_INT >= 14) {
                final Configuration config = mContext.getResources()
                        .getConfiguration();
                finalGravity = Gravity.getAbsoluteGravity(gravity, config.getLayoutDirection());
            } else {
                finalGravity = gravity;
            }
            mBuilderParams.gravity = finalGravity;
            if ((finalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                mBuilderParams.horizontalWeight = 1.0f;
            }
            if ((finalGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                mBuilderParams.verticalWeight = 1.0f;
            }
            mBuilderParams.y = yOffset;
            mBuilderParams.x = xOffset;
            return this;
        }

        @Override
        public IBuilder setDuration(long durationMillis) {
            if (durationMillis < 0) {
                mBuilderDurationMillis = 0;
            }
            if (durationMillis == Toast.LENGTH_SHORT) {
                mBuilderDurationMillis = 2000;
            } else if (durationMillis == Toast.LENGTH_LONG) {
                mBuilderDurationMillis = 3500;
            } else {
                mBuilderDurationMillis = durationMillis;
            }
            return this;
        }

        @Override
        public IBuilder setIsSingle(boolean isStandard) {
            mBuilderIsSingle = isStandard;
            return this;
        }


    }

    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

    private final static Runnable mActivite = new Runnable() {
        @Override
        public void run() {
            activeQueue();
        }
    };


}
