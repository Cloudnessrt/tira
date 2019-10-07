package com.platform.tira.vo;

import com.platform.tira.entity.temp.DailyTask;

public class DailyTaskVo extends DailyTask {

    private double todayprogress;

    public double getTodayprogress() {
        return todayprogress;
    }

    public void setTodayprogress(double todayprogress) {
        this.todayprogress = todayprogress;
    }
}
