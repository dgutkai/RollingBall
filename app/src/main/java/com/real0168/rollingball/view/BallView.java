package com.real0168.rollingball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import com.real0168.rollingball.R;
import com.real0168.rollingball.bean.BallBean;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 使用SurfaceView绘制的球形布局，布局中的元素不能够点击。
 */
public class BallView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final int VIEW_COUNT = 30;
    private Context mContext;
    // SurfaceHolder
    private SurfaceHolder mSurfaceHolder;
    // 画布
    private Canvas mCanvas;
    // 子线程标志位
    private boolean isDrawing;
    private boolean isTouch;
    // 画笔
    Paint mPaint;
    // 画笔
    Paint mPaint2;
    // 路径
    Path mPath;
    private float mLastX, mLastY;//上次的坐标

    public VelocityTracker velocityTracker;


    public BallView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init(){
        //初始化 SurfaceHolder mSurfaceHolder
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);

        // 设置屏幕常亮
//        this.setKeepScreenOn(true);

        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(5f);
        mPaint.setTextSize(48);
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint2 = new Paint();
        mPaint2.setStrokeWidth(1f);
        mPaint2.setTextSize(36);
        mPaint2.setStyle(Paint.Style.STROKE);
        //路径
        mPath = new Path();

        for (int i = 0; i < VIEW_COUNT; i++){
//            theta[i] = (float) (Math.random() * 2 * Math.PI);
//            phi[i] = (float) (Math.random() * Math.PI);
            phi[i] = (double) Math.acos(-1+(2*i+1.0f)/VIEW_COUNT);
            theta[i] = (double) (Math.sqrt(VIEW_COUNT*Math.PI)*phi[i]);
            BallBean bb1 = new BallBean();
            bb1.setShowX((float) (400 * Math.cos(theta[i]) * Math.sin(phi[i])));
            bb1.setShowY((float) (400 * Math.sin(theta[i]) * Math.sin(phi[i])));
            bb1.setShowZ((float) (400 * Math.cos(phi[i])));
            bb1.setAlpha(0);
            bb1.setContent(i+"");
            bb1.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round));
            ballBeans[i] = bb1;
        }
        velocityTracker=VelocityTracker.obtain();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawBG();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing){
//            drawing();
            drawBG();
        }
    }

    BallBean[] ballBeans = new BallBean[VIEW_COUNT];
    double[] theta = new double[VIEW_COUNT];
    double[] phi = new double[VIEW_COUNT];
    float a = 0;
    float b = 0;

    synchronized private void drawBG(){
//        Log.e("TAG", "开始");
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
//            mCanvas.drawArc(200, 400, 1000, 1200, 0, 360, true, mPaint2);
            float a1 = a;
            float b1 = b;
            if (!isTouch) {
                a *= 0.99;
                b *= 0.99;
                if (Math.abs(a) < 0.001 && Math.abs(b) < 0.001){
                    isDrawing = false;
                }
            }
            for (int i = 0; i < VIEW_COUNT; i++) {

                BallBean bb1 = ballBeans[i];

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
                float per= (float) (800/(800+rz2));

                per= (float) ((per-0.4)*(10/6));
                bb1.setAlpha(per);

            }
            Arrays.sort(ballBeans, new Comparator<BallBean>() {
                @Override
                public int compare(BallBean o1, BallBean o2) {
                    if (o1.getAlpha() > o2.getAlpha()){
                        return 1;
                    }else if (o1.getAlpha() < o2.getAlpha()){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
            for (int j = 0; j < ballBeans.length; j++){
                BallBean bb2 = ballBeans[j];
                float per = bb2.getAlpha();
                int alpha = Math.min((int) (per * 256), 255);
//                int alpha = Math.min((int) (Math.sin(theta[i]) * 128) + 138, 255);
                mPaint.setAlpha(alpha);
                mPaint.setTextSize((float) (Math.ceil(12*per)+20));
//                mCanvas.drawRect((float)(bb2.getX() + 600 - (20 + 20 * per)),
//                        (float)(bb2.getY() + 800 - (20 + 20 * per)),
//                        (float)(bb2.getX() + 600 + (20 + 20 * per)),
//                        (float)(bb2.getY() + 800 + (20 + 20 * per)), mPaint);
//                mCanvas.drawText(bb2.getContent(), (float)(bb2.getX() + 600), (float)(bb2.getY() +820), mPaint);
                int nw = (int) (bb2.getImage().getWidth() * per);
                int nh = (int) (bb2.getImage().getHeight() * per);
                mCanvas.drawBitmap(imageScale(bb2.getImage(), nw, nh), (float)(bb2.getShowX() + 600 - nw/2), (float)(bb2.getShowY() +820 - nh/2), mPaint);
            }
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
//            Log.e("TAG", "结束");
        }
    }

    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
//        matrix.preSkew(0.2f, 1f);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }
    /**
     * 绘制
     */
    private void drawing() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath,mPaint);
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
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
                new Thread(this).start();//开启线程
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);

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
                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                if (dx >= 3 || dy >= 3) {
                    mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
                }


                float mouseX=velocityTracker.getXVelocity();
                float mouseY=velocityTracker.getYVelocity();
                if (mouseX == 0 && mouseY == 0){
                    break;
                }
                Log.e("TAG", "mX=" + mouseX + "; mY=" + mouseY);
                mLastX = x;
                mLastY = y;
//                mouseX*=10;
//                mouseY*=10;
                a = (mouseY / 400) * 4;

                b = (-mouseX / 400) * 4;

                a = (float) (a * Math.PI / 180);
                b = (float) (b * Math.PI / 180);
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


}
