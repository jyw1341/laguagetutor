package com.jyw.laguagetutor;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class MinMaxDecorator implements DayViewDecorator {

    CalendarDay min,max;

    public MinMaxDecorator(CalendarDay min, CalendarDay max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return (day.getMonth()==max.getMonth()&&day.getDay()>max.getDay())||day.getMonth()==min.getMonth()&&day.getDay()<min.getDay();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#d2d2d2")));

    }
}
