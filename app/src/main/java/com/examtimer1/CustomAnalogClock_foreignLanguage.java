package com.examtimer1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.examtimer1.examtimer.R;

public class CustomAnalogClock_foreignLanguage extends View {

    /** height, width of the clock's view */
    private int mHeight, mWidth = 0;

    /** numeric numbers to denote the hours */
    private int[] mClockHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    /** spacing and padding of the clock-hands around the clock round */
    private int mPadding = 0;
    private int mNumeralSpacing = 0;

    /** truncation(잘림) of the heights of the clock-hands,
     hour clock-hand will be smaller comparetively to others */
    private int mHandTruncation, mHourHandTruncation = 0;

    /** others attributes to calculate the locations of hour-points */
    private int mRadius = 0;

    private int start_hour=5;
    private int start_minute=0;
    private int start_second=0;
    private int current_hour, current_minute;
    private Paint mPaint;
    private Rect mRect = new Rect();
    private int pause_hour=0, pause_minute=0, pause_second=0, hour=0, minute=0, second=0;
    private long currentTime=0, pauseTime=0, pauseTimeInMillis=0, tempPauseTime=0;
    private boolean isInit;  // it will be true once the clock will be initialized
    private long startTimeInMillis=System.currentTimeMillis();
    private int cnt_btn_pressed=0;
    private int current_subject=1, tmp=0;
    private boolean[] isTimeSet;
    private boolean isPauseTimeAdded;
    private boolean isTestEnd;
    private boolean isBreakTimeEnd;
    private boolean isExamEnd;

    public boolean isExamEnd() {
        return isExamEnd;
    }

    public void setExamEnd(boolean examEnd) {
        isExamEnd = examEnd;
    }

    private boolean isStart=false;

    public boolean isTestEnd() {
        return isTestEnd;
    }

    public void setTestEnd(boolean testEnd) {
        isTestEnd = testEnd;
    }

    public boolean isBreakTimeEnd() {
        return isBreakTimeEnd;
    }

    public void setBreakTimeEnd(boolean breakTimeEnd) {
        isBreakTimeEnd = breakTimeEnd;
    }

    public int getCurrent_subject() {
        return current_subject;
    }

    public void setCurrent_subject(int current_subject) {
        this.current_subject = current_subject;
    }

    public int getCnt_btn_pressed() {
        return cnt_btn_pressed;
    }

    public void setCnt_btn_pressed(int cnt_btn_pressed) {
        this.cnt_btn_pressed = cnt_btn_pressed;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public int getStart_second() {
        return start_second;
    }

    public void setStart_second(int start_second) {
        this.start_second = start_second;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public void setStart_minute(int start_minute) {
        this.start_minute = start_minute;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public int getStart_minute() {
        return start_minute;
    }

//    public int getState() {
//        return state;
//    }
//    public void setState(int state) {
//        this.state = state;
//    }
//    private int state=0;

    public CustomAnalogClock_foreignLanguage(Context context){
        super(context);
        Log.d("로그", "CustomAnalogClock(Context context) 호출");
    }

    public CustomAnalogClock_foreignLanguage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("로그", "CustomAnalogClock(Context context, AttributeSet attrs) 호출");
//        btn_start_analogClock=findViewById(R.id.btn_start_analogClock);
//        btn_next_analogClock=findViewById(R.id.btn_next_analogClock);
    }

    public CustomAnalogClock_foreignLanguage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("로그", "CustomAnalogClock(Context context, AttributeSet attrs, int defStyleAttr)");
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: Implementation of the analog-clock UI in runtime goes here.

        isTimeSet=new boolean[12];
        for(int i=0;i<12;i++){
            isTimeSet[i]=false;
        }

        Log.d("로그", "onDraw 호출");
        Log.d("TAG", "startHour: "+getStart_hour());
        Log.d("TAG", "startMinute: "+getStart_minute());

        /** initialize necessary values */
        if (!isInit) {
            mPaint = new Paint();
            mHeight = getHeight();
            mWidth = getWidth();
            mPadding = mNumeralSpacing + 50;  // spacing from the circle border
            int minAttr = Math.min(mHeight, mWidth);
            mRadius = minAttr / 2 - mPadding;

            // for maintaining different heights among the clock-hands
            mHandTruncation = minAttr / 20;
            mHourHandTruncation = minAttr / 10;

            isInit = true;  // set true once initialized
        }

        /** draw the canvas-color */
        canvas.drawColor(Color.WHITE);

        /** circle border */
        mPaint.reset();
        mPaint.setColor(R.color.colorPrimaryDark);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mPadding - 10, mPaint);

        /** clock-center */
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);  // the 03 clock hands will be rotated from this center point.

        /** border of hours */

        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);  // set font size (optional)

        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect);  // for circle-wise bounding

            // find the circle-wise (x, y) position as mathematical rule
            double angle = Math.PI / 6 * (hour - 3);
            int x = (int) (mWidth / 2 + Math.cos(angle) * mRadius - mRect.width() / 2);
            int y = (int) (mHeight / 2 + Math.sin(angle) * mRadius + mRect.height() / 2);

            canvas.drawText(String.valueOf(hour), x, y, mPaint);  // you can draw dots to denote hours as alternative
        }

        /** draw clock hands to represent the every single time */
        if(isStart()){
            if(!isPauseTimeAdded) pauseTimeInMillis+=tempPauseTime;
            isPauseTimeAdded=true;
            currentTime=System.currentTimeMillis();
            hour= (int) ((currentTime-startTimeInMillis-pauseTimeInMillis)/1000/60/60);
            minute=(int) ((currentTime-startTimeInMillis-pauseTimeInMillis)/1000/60);
            second=(int) ((currentTime-startTimeInMillis-pauseTimeInMillis)/1000%60);
        }
        else if(!isStart){
            if(getCnt_btn_pressed()>=1){
                pauseTime=currentTime;
                tempPauseTime=System.currentTimeMillis()-pauseTime;
                isPauseTimeAdded=false;
            }
            else{
                tmp++;
                if(tmp==1) currentTime=System.currentTimeMillis();
                pauseTime=currentTime;
                tempPauseTime=System.currentTimeMillis()-pauseTime;
                isPauseTimeAdded=false;
            }
        }
        Log.d("TAG", "시간: "+hour+"\n분: "+minute+"\n초: "+second);

        int timeSpentInMinute=hour*60+minute;
        Log.d("tag_Time", "timeSpentInMInute= "+timeSpentInMinute);

        if(timeSpentInMinute==40&&second==0) {
            setExamEnd(true);
        }

//        else if(timeSpentInMinute)
   /*     if(getCurrent_subject()==2&&(!isTimeSet[0])){
            startTimeInMillis=currentTime;
            isTimeSet[0]=true;
            setStart(false);
        }*/
//        if(1<=hour*60+minute&&hour*60+minute<2) setState(1);
//        else if(2<=hour*60+minute&&hour*60+minute<3) setState(2);
//        else if(3<=hour*60+minute&&hour*60+minute<4) setState(3);
//        Log.d("TAG", "STATE: "+getState());
        drawHandLine_hour(canvas, getStart_hour()+hour, getStart_minute()+minute); // draw hours
        drawHandLine(canvas, getStart_minute()+minute, false, false); // draw minutes
        drawHandLine(canvas, getStart_second()+second, false, true); // draw seconds

        Log.d("TAG", "isStart?: "+isStart());
        /** invalidate the appearance for next representation of time  */
        postInvalidateDelayed(500);
        invalidate();
    }

    @SuppressLint("ResourceAsColor")
    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius = isHour ? mRadius - mHandTruncation - mHourHandTruncation : mRadius - mHandTruncation;
        if (isSecond) mPaint.setColor(Color.RED);
        canvas.drawLine(mWidth / 2, mHeight / 2, (float) (mWidth / 2 + Math.cos(angle) * handRadius), (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }

    private void drawHandLine_hour(Canvas canvas, double hour, double minute) {
        Log.d("tag", "drawHandLine_hour() 호출");
        if(minute>=60){
            minute-=60;
            hour++;
        }
        double angle = Math.PI * hour / 6 +minute/360*Math.PI - Math.PI / 2;
        int handRadius = mRadius - mHandTruncation - mHourHandTruncation;
        canvas.drawLine(mWidth / 2, mHeight / 2, (float) (mWidth / 2 + Math.cos(angle) * handRadius), (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }

}
