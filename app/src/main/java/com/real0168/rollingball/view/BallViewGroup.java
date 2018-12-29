package com.real0168.rollingball.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.real0168.manager.ThreadPoolManager;
import com.real0168.rollingball.bean.BallBean;

/**
 * 继承自ViewGroup的球形布局，自控件可以点击。
 */
public class BallViewGroup extends ViewGroup implements Runnable {
    private Context mContext;

    private
    float radius;
    BallBean[] bollBeans; // = new BollBean[VIEW_COUNT];
    double[] theta; // = new double[VIEW_COUNT];
    double[] phi; // = new double[VIEW_COUNT];
    float changeA = 0;
    float changeB = 0;



    public VelocityTracker velocityTracker;
    // 子线程标志位
    private boolean isDrawing;
    private boolean isTouch;

    public BallViewGroup(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BallViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public BallViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BallViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
    }

    private void init(){
        setChildrenDrawingOrderEnabled(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker=VelocityTracker.obtain();
        velocityTracker.addMovement(event);

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.clear();
                velocityTracker.recycle();
                break;
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                isDrawing = true ;//每次开始将标记设置为ture
                ThreadPoolManager.getNormalThreadPoolProxy().execute(this);
//                mLastX = x;
//                mLastY = y;
//                mPath.moveTo(mLastX, mLastY);

//                float mouseX=event.getX()-600;
//                float mouseY=event.getY()-800;
//
//                mouseX/=5;
//                mouseY/=5;
//
//                a = (-Math.min( Math.max( -mouseY, -350 ), 350 ) / 400 ) * 2;
//
//                b = (Math.min( Math.max( -mouseX, -350 ), 350 ) / 400 ) * 2;
//
//                a = (float) (a * Math.PI / 180);
//                b = (float) (b * Math.PI / 180);

                return true;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.computeCurrentVelocity(100);   //计算每1000ms手指移动的像素
//                float dx = Math.abs(x - mLastX);
//                float dy = Math.abs(y - mLastY);
//                if (dx >= 3 || dy >= 3) {
//                    mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
//                }


                float mouseX=velocityTracker.getXVelocity();
                float mouseY=velocityTracker.getYVelocity();
                if (mouseX == 0 && mouseY == 0){
                    break;
                }

//                mLastX = x;
//                mLastY = y;
//                mouseX*=10;
//                mouseY*=10;
                changeA = (mouseY / 400) * 4;

                changeB = (-mouseX / 400) * 4;

                changeA = (float) (changeA * Math.PI / 180);
                changeB = (float) (changeB * Math.PI / 180);
                isTouch = true;
//                drawBG();

                return true;
            case MotionEvent.ACTION_UP:
                isTouch = false;
//                float mouseX2=velocityTracker.getXVelocity();
//                float mouseY2=velocityTracker.getYVelocity();
//                Log.e("TAG", "mX2=" + mouseX2 + "; mY2=" + mouseY2);
//                mLastX = x;
//                mLastY = y;
//                mouseX2*=5;
//                mouseY2*=5;
//                a = (-Math.min(Math.max(-mouseY2, -350), 350) / 400) * 4;
//
//                b = (Math.min(Math.max(-mouseX2, -350), 350) / 400) * 4;
//
//                a = (float) (a * Math.PI / 180);
//                b = (float) (b * Math.PI / 180);
                velocityTracker.clear();
                velocityTracker.recycle();
//                isDrawing = false;
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int cCount = getChildCount();

//            mCanvas.drawArc(200, 400, 1000, 1200, 0, 360, true, mPaint2);
            float a1 = changeA;
            float b1 = changeB;
            if (!isTouch) {
                changeA *= 0.99;
                changeB *= 0.99;
                if (Math.abs(changeA) < 0.001 && Math.abs(changeB) < 0.001){
                    isDrawing = false;
                }
            }
            for (int i = 0; i < cCount; i++) {

                BallBean bb1 = bollBeans[i];

//                Log.e("TAG", "("+a + ", " + b + ")");
                double rx1 = bb1.getShowX();
                double ry1= bb1.getShowY()*Math.cos(a1)+bb1.getShowZ()*(-Math.sin(a1));
                double rz1= bb1.getShowY()*Math.sin(a1)+bb1.getShowZ()*Math.cos(a1);

//              log.e("TAG", "("+a + ", " + b + ")");
                double rx2= rx1*Math.cos(b1)+rz1*Math.sin(b1);
                double ry2=ry1;
                double rz2= rx1*(-Math.sin(b1))+rz1*Math.cos(b1);

                bb1.setShowX(rx2);
                bb1.setShowY(ry2);
                bb1.setShowZ(rz2);
                float per= (float) ((radius*2)/(radius*2+rz2)/2);
//                per= (float) ((per-0.4)*(10/6));
                bb1.setAlpha(per);

            }
//            Arrays.sort(bollBeans, new Comparator<BollBean>() {
//                @Override
//                public int compare(BollBean o1, BollBean o2) {
//                    if (o1.getAlpha() > o2.getAlpha()){
//                        return 1;
//                    }else if (o1.getAlpha() < o2.getAlpha()){
//                        return -1;
//                    }else{
//                        return 0;
//                    }
//                }
//            });
            for (int j = 0; j < bollBeans.length; j++){
                View childView = getChildAt(j);
                BallBean bb2 = bollBeans[j];
                float per = bb2.getAlpha();
                int alpha = Math.min((int) (per * 256), 255);
//                int alpha = Math.min((int) (Math.sin(theta[i]) * 128) + 138, 255);
                childView.setAlpha(per);
//                mPaint.setAlpha(alpha);
//                mPaint.setTextSize((float) (Math.ceil(12*per)+20));
//                mCanvas.drawRect((float)(bb2.getX() + 600 - (20 + 20 * per)),
//                        (float)(bb2.getY() + 800 - (20 + 20 * per)),
//                        (float)(bb2.getX() + 600 + (20 + 20 * per)),
//                        (float)(bb2.getY() + 800 + (20 + 20 * per)), mPaint);
//                mCanvas.drawText(bb2.getContent(), (float)(bb2.getX() + 600), (float)(bb2.getY() +820), mPaint);
                float cWidth = bb2.getBWidth() * per;
                float cHeight = bb2.getBHeight() * per;
//                Log.e("TAG", per + " (width="+ bb2.getBWidth() + ", " + bb2.getBHeight() + ")-(width="+ cWidth + ", " + cHeight + ")");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    childView.setZ(per);
                }
                childView.layout((int)(bb2.getShowX() + radius - cWidth/2 + 40), (int) (bb2.getShowY() + radius - cHeight / 2 + 40), (int)(bb2.getShowX() + radius + cWidth/2  + 40), (int)(bb2.getShowY() + radius + cHeight/2 + 40));
//                int nw = (int) (bb2.getImage().getWidth() * per);
//                int nh = (int) (bb2.getImage().getHeight() * per);
//                mCanvas.drawBitmap(imageScale(bb2.getImage(), nw, nh), (float)(bb2.getBX() + 600 - nw/2), (float)(bb2.getBY() +820 - nh/2), mPaint);
            }


    }

    private int oldSizeWidth;
    private int oldSizeHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*
         *   获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (sizeHeight == oldSizeHeight && oldSizeWidth == sizeWidth){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        oldSizeWidth = sizeWidth;
        oldSizeHeight = sizeHeight;
        radius = Math.min(sizeHeight, sizeWidth)/2 - 40;
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int cCount = getChildCount();
        bollBeans = new BallBean[cCount];
        theta = new double[cCount];
        phi = new double[cCount];
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

//            theta[i] = (float) (Math.random() * 2 * Math.PI);
//            phi[i] = (float) (Math.random() * Math.PI);
            phi[i] = (double) Math.acos(-1+(2*i+1.0f)/cCount);
            theta[i] = (double) (Math.sqrt(cCount*Math.PI)*phi[i]);
            BallBean bb1 = new BallBean();
            bb1.setShowX((float) (radius * Math.cos(theta[i]) * Math.sin(phi[i])));
            bb1.setShowY((float) (radius * Math.sin(theta[i]) * Math.sin(phi[i])));
            bb1.setShowZ((float) (radius * Math.cos(phi[i])));
            bb1.setBWidth(cWidth);
            bb1.setBHeight(cHeight);
            bb1.setAlpha(0);
            bb1.setContent(i+"");
            bollBeans[i] = bb1;
        }

        /*
        如果是wrap_content设置为我们计算的值
        否则：直接设置为父容器计算的值
        */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : 800, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : 800);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void run() {
        while (isDrawing){
//            drawing();
//            this.postInvalidate();
            Handler handler = new Handler(mContext.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float oldX;
    private float oldY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldX = ev.getX();
                oldY = ev.getY();
                break;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            float newX = ev.getX();
            float newY = ev.getY();
            if (Math.abs(newX - oldX) > 10 || Math.abs(newY - oldY) > 10){
                if (!isDrawing) {
                    isTouch = true;
                    isDrawing = true;//每次开始将标记设置为ture
                    ThreadPoolManager.getNormalThreadPoolProxy().execute(this);
                }
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
