package com.example.weatherclock.views.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.weatherclock.R;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class ClockView extends View {


    private static final String TAG = "ClockView";

    private Canvas canvas;
    private Paint paintBig;
    private Paint paintSmall;
    private Paint paintLine;
    private Paint paintLineHighlighted;
    private Bitmap[] weather;
    private float lines[] = new float[48];




    public ClockView(Context context) {
        super(context);

        init(null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

        paintBig = new Paint();
        paintBig.setAntiAlias(true);
        paintBig.setColor(getResources().getColor(R.color.colorBigCircle));

        paintSmall = new Paint();
        paintSmall.setAntiAlias(true);
        paintSmall.setColor(getResources().getColor(R.color.colorSmallCircle));

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(getResources().getColor(R.color.colorBlack));
        paintLine.setStrokeWidth(5);

        paintLineHighlighted = new Paint();
        paintLineHighlighted.setAntiAlias(true);
        paintLineHighlighted.setColor(getResources().getColor(R.color.colorBigCircle));
        paintLineHighlighted.setStrokeWidth(7);

        weather = new Bitmap[12];
        for(int i=0; i<12;i++){
            weather[i] = BitmapFactory.decodeResource(getResources(), R.drawable.weather);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
       // canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        float cx,cy, bigRadius, smallRadius, pictureRadius;
        bigRadius = (float) (getWidth()/2*0.915);
        smallRadius = (float) (getWidth()/2*0.915);//0.778
        pictureRadius = (float) (getWidth()/2*0.65);
        cx = getWidth()/2;
        cy = getHeight()/2;
        canvas.drawCircle(cx,cy,bigRadius, paintBig);
        canvas.drawCircle(cx,cy,smallRadius, paintSmall);
        double degree = 0;

        for (int i=0; i<12;i++){
            float edgeX = (float) (cx+smallRadius*Math.cos(Math.toRadians(degree-90)));
            float edgeY = (float) (cy+smallRadius*Math.sin(Math.toRadians(degree-90)));

            lines[i*4] = cx;
            lines[i*4+1] = cy;
            lines[i*4+2] = edgeX;
            lines[i*4+3] = edgeY;

            float pictureX = (float) (cx+pictureRadius*Math.cos(Math.toRadians(degree-90+15)));
            float pictureY = (float) (cy+pictureRadius*Math.sin(Math.toRadians(degree-90+15)));
            //canvas.drawBitmap(weather[i],pictureX-weather[i].getWidth()/2,pictureY-weather[i].getHeight()/2,null);

            degree += 30;
        }

        drawLines();
    }

    private void drawLines() {
        Log.d(TAG, "drawLines");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        for (int i = 0; i<12; i++) {
            if (hour == i || hour+1 == i || (hour==11 && i ==0)) {
                canvas.drawLine(lines[i*4],lines[i*4+1],lines[i*4+2],lines[i*4+3],paintLineHighlighted);
            }else{
                canvas.drawLine(lines[i*4],lines[i*4+1],lines[i*4+2],lines[i*4+3],paintLine);
            }
        }

    }

    public void reDraw() {
        this.invalidate();
    }
}
