package com.jyw.laguagetutor;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class BookedDecorator implements DayViewDecorator {

    HashSet<String> dateSet;

    public BookedDecorator(HashSet<String> dateSet) {
        this.dateSet = dateSet;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        Date from = day.getDate();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        String to = transFormat.format(from);

        return dateSet.contains(to);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5F, Color.parseColor("#1D872A")));
    }
}
