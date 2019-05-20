package com.example.labthreecalendar;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;

import java.util.Calendar;

public class DayButton extends android.support.v7.widget.AppCompatButton {
    private int numberOfDay;
    private int numberOfMonth;
    private int numberOfYear;
    private boolean isCurrentMonth;

    public DayButton(Context context, int numberOfDay, int numberOfMonth, int numberOfYear, boolean isCurrentMonth) {
        super(context);
        this.numberOfDay = numberOfDay;
        this.numberOfMonth = numberOfMonth;
        this.numberOfYear = numberOfYear;
        this.isCurrentMonth = isCurrentMonth;
    }

    public void setNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
    }

    public void setNumberOfMonth(int numberOfMonth) {
        this.numberOfMonth = numberOfMonth;
    }

    public void setNumberOfYear(int numberOfYear) {
        this.numberOfYear = numberOfYear;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public int getNumberOfDay() {
        return numberOfDay;
    }

    public int getNumberOfMonth() {
        return numberOfMonth;
    }

    public int getNumberOfYear() {
        return numberOfYear;
    }

    public DayButton(Context context) {
        super(context);
    }

    public DayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, numberOfYear);
        calendar.set(Calendar.MONTH, numberOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, numberOfDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    public boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long today = calendar.getTime().getTime();

        calendar.set(Calendar.YEAR, numberOfYear);
        calendar.set(Calendar.MONTH, numberOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, numberOfDay);

        long calendarMillis = calendar.getTime().getTime();

        return today == calendarMillis;
    }

    public void setBackgroundEvents(int count) {
        setBackgroundResource(R.drawable.day);
        if (isCurrentMonth()) {
            if (count >= 1 && count <= 3) {
                setBackgroundResource(R.drawable.daybusy13);
            } else if (count <= 5) {
                setBackgroundResource(R.drawable.daybusy35);
            } else if (count <= 7) {
                setBackgroundResource(R.drawable.daybusy57);
            } else if (count <= 9) {
                setBackgroundResource(R.drawable.daybusy79);
            } else {
                setBackgroundResource(R.drawable.daybusy9);
            }
        } else {
            if (count >= 1 && count <= 3) {
                setBackgroundResource(R.drawable.daybusy13light);
            } else if (count <= 5) {
                setBackgroundResource(R.drawable.daybusy35light);
            } else if (count <= 7) {
                setBackgroundResource(R.drawable.daybusy57light);
            } else if (count <= 9) {
                setBackgroundResource(R.drawable.daybusy79light);
            } else {
                setBackgroundResource(R.drawable.daybusy9light);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setMeasuredDimension(width, height + 10);
        else
            setMeasuredDimension(width, height + 5);
    }
}
