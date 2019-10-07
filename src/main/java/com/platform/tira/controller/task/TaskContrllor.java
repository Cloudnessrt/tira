package com.platform.tira.controller.task;

import com.platform.tira.entity.temp.DailyTask;
import com.platform.tira.service.temp.IDailyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping ( "/task" )
public class TaskContrllor {

    @Autowired
    private IDailyTaskService iDailyTaskService;

    //添加一个任务
    @RequestMapping(value = "/add")
    public void addStudent() throws ParseException {

        List<DailyTask> dailyTaskList=new ArrayList<>();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateformat.parse("2019-10-07");
        DailyTask search=new DailyTask();
        search.setCreatedate(date);
        iDailyTaskService.DailyTaskStatistic(search);

    }

    //添加一个任务
    @RequestMapping(value = "/update")
    public void updateStudent() {

    }
}
