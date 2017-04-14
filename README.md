
##   ToastCompat ##
   欢迎各位提出改进意见及完善,欢迎star.

* 做这个的出发点是不管是测试还是本身的项目需求,需要搞一个toast,满足各式各样的需要,大概有这些,于是就有写了
 - 有需求的单列的toast
 - 有需求可以自定义弹出时间的toast
 - 有需求在关闭系统的通知权限后照样可以弹出toast
 - 有需求可以弹出自定义view
* 参考资料
  - 参考toast的源码,系统维护是一个队列,同时也用到了aidl
  
  `

		public void show() {

        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        INotificationManager service = getService();
        String pkg = mContext.getOpPackageName();
        TN tn = mTN;
        tn.mNextView = mNextView;

        try {
            service.enqueueToast(pkg, tn, mDuration);
        } catch (RemoteException e) {
            // Empty
        }
    }
`

* 参考okhttp源码,以前没有太在意,后来发现,不管是okhttp中的builder还是dialog中的,他们的功能都是设置参数,这种思想非常好
* 使用方法
  - 直接拷贝源码到项目中,简单暴力直接,有点重口味了,同时源码也不多。   
  ![](http://i.imgur.com/V3FmFFS.png)
  - 直接引入库  
   compile 'com.gs:ToastCompat:1.0.0'
* 效果展示  
  ![](http://i.imgur.com/vdeEWfN.gif)
* Thanks  
  [rongwu]([https://github.com/rongwu/ToastCompat](https://github.com/rongwu/ToastCompat "rongwu")
  
  
