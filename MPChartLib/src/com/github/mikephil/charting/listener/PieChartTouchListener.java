
package com.github.mikephil.charting.listener;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.Highlight;

/**
 * Touchlistener for the PieChart.
 * 
 * @author Philipp Jahoda
 */
public class PieChartTouchListener extends SimpleOnGestureListener implements OnTouchListener {

    Matrix mMatrix = new Matrix();
    Matrix mSavedMatrix = new Matrix();

    PointF mMid = new PointF();

    private PieChart mChart;

    private GestureDetector mGestureDetector;

    public PieChartTouchListener(PieChart ctx) {
        this.mChart = ctx;

        mGestureDetector = new GestureDetector(ctx.getContext(), this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
              
        if (mGestureDetector.onTouchEvent(e))
            return true;

        // if rotation by touch is enabled
        if (mChart.isRotationEnabled()) {

            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mChart.setStartAngle(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mChart.updateRotation(x, y);
                    mChart.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }

        return true;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    @Override
    public void onLongPress(MotionEvent me) {
        // todo
    };

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    /** reference to the last highlighted object */
    private Highlight mLastHighlight = null;

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        float distance = mChart.distanceToCenter(e.getX(), e.getY());

        // check if a slice was touched
        if (distance < mChart.getRadius() / 2 || distance > mChart.getRadius()) {

            // if no slice was touched, highlight nothing
            mChart.highlightValues(null);
            mLastHighlight = null;
        } else {

            int index = mChart.getIndexForAngle(mChart.getAngleForPoint(e.getX(), e.getY()));
            int dataSetIndex = mChart.getDataSetIndexForIndex(index);

            Highlight h = new Highlight(index, dataSetIndex);

            if (h.equalTo(mLastHighlight)) {

                mChart.highlightTouch(null);
                mLastHighlight = null;
            } else {

                mChart.highlightTouch(h);
                mLastHighlight = h;
            }
        }

        return true;
    }
}
