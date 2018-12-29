package com.real0168.rollingball.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BallBean {

    private double showX;
    private double showY;
    private double showZ;
    private float alpha;
    private String content;
    private Bitmap image;
    private int bwidth;
    private int bheight;

    public double getShowX() {
        return showX;
    }

    public double getShowY() {
        return showY;
    }

    public double getShowZ() {
        return showZ;
    }

    public void setShowX(double bx) {
        this.showX = bx;
    }

    public void setShowY(double by) {
        this.showY = by;
    }

    public void setShowZ(double bz) {
        this.showZ = bz;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getBHeight() {
        return bheight;
    }

    public void setBHeight(int bheight) {
        this.bheight = bheight;
    }

    public int getBWidth() {
        return bwidth;
    }

    public void setBWidth(int bwidth) {
        this.bwidth = bwidth;
    }
}
