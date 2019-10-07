package com.platform.tira.service.temp.impl;

import com.platform.tira.common.AbstractExcelBuilder;
import com.platform.tira.entity.temp.DailyTask;
import com.platform.tira.mapper.temp.IDailyTaskMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DailyTaskExcelBuilder extends AbstractExcelBuilder<DailyTask> {

    private static DailyTaskExcelBuilder builder;
    @Autowired
    private IDailyTaskMapper iDailyTaskMapper;

    private DailyTaskExcelBuilder() {
    }

    /**
     * 单例
     */
    public synchronized static DailyTaskExcelBuilder getInstance() {
        if (null == builder) {
            builder = new DailyTaskExcelBuilder();
            return builder;
        }
        return builder;
    }

    /**
     * 写Excel内容
     */
    @Override
    protected void writeBody(XSSFCellStyle cellStyle, XSSFDataFormat format, Sheet sheet,List <DailyTask> datas) throws Exception {
        int columnIndex = 0;
        int rowIndex = 1;
        boolean flag=true;
        create(datas);
        DataSort(datas);
        int reginStartRow=1;
        double allworkload=0;
        double unworkload=0;
        for (DailyTask dailyTask : datas) {
            columnIndex = 0;
            double thisUnworkload =dailyTask.getWorkload()*(100-dailyTask.getProgress())/100;
            Row row = sheet.createRow(rowIndex);
            row.createCell(columnIndex++).setCellValue(dailyTask.getProjectName());
            row.createCell(columnIndex++).setCellValue(dailyTask.getCode());
            row.createCell(columnIndex++).setCellValue(dailyTask.getTitle());
            row.createCell(columnIndex++).setCellValue(dailyTask.getOwner());
            row.createCell(columnIndex++).setCellValue("");
            row.createCell(columnIndex++).setCellValue(dailyTask.getWorkload());
            row.createCell(columnIndex++).setCellValue(dailyTask.getProgress());
            row.createCell(columnIndex++).setCellValue("");
            row.createCell(columnIndex++).setCellValue(thisUnworkload);
            allworkload+=dailyTask.getWorkload();
            unworkload+=thisUnworkload;
            if( rowIndex==datas.size() || !datas.get(rowIndex).getOwner().equals(dailyTask.getOwner())) {
                sheet.getRow(reginStartRow).createCell(columnIndex++).setCellValue(allworkload);
                addMerged(sheet,reginStartRow,rowIndex,columnIndex,columnIndex);
                sheet.getRow(reginStartRow).createCell(columnIndex++).setCellValue(unworkload);
                addMerged(sheet,reginStartRow,rowIndex,columnIndex,columnIndex);
                reginStartRow = rowIndex + 1;
                allworkload = 0;
                unworkload = 0;
            }
            rowIndex++;
        }
    }

    private void addMerged(Sheet sheet,int reginStartRow,int rowEndRow,int columnStart,int columnEnd){
        if(reginStartRow!=rowEndRow){
            CellRangeAddress merged = new CellRangeAddress(reginStartRow, rowEndRow, columnStart-1, columnEnd-1); // 起始行, 终止行, 起始列, 终止列
            sheet.addMergedRegion(merged);
        }

    }

    /**
     * 写Excel表头
     */
    @Override
    protected String[] getHead( ) {
        return new String[] { "看板", "卡片ID","标题","指派人","提测日期","工作量","完成百分比","今日进展","剩余工作量","总工作量","总剩余工作量"};
    }

    private List <DailyTask>  create(List <DailyTask> dailyTaskList) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateformat.parse("2019-10-08");
        DailyTask search=new DailyTask();
        search.setCreatedate(date);
        dailyTaskList= iDailyTaskMapper.getDailyTaskList(search);
        for (int i=1;i<100;i++){
            DailyTask dailyTask= new DailyTask();
            dailyTask.setProjectName("ProjectName");
            dailyTask.setCode(i+"code");
            dailyTask.setId((long)i);
            dailyTask.setOwner(i+"Owne");
            dailyTask.setProgress(i);
            dailyTask.setTitle(i+"title");
            dailyTask.setWorkload(i);
            if(i==3 || i==8){
                dailyTaskList.add(dailyTask);
                DailyTask dailyTask2= new DailyTask();
                dailyTask2.setProjectName("ProjectName");
                dailyTask2.setCode(i+10+"coder");
                dailyTask2.setId((long)i+10);
                dailyTask2.setOwner(i+10+"Owne");
                dailyTask2.setProgress(i);
                dailyTask2.setTitle(i+10+"title");
                dailyTask2.setWorkload(i+10);
                dailyTaskList.add(dailyTask2);
            }
            dailyTaskList.add(dailyTask);
        }

        return dailyTaskList;
    }

    private void DataSort(List <DailyTask> dailyTasks){

         dailyTasks.sort((l,r)->{
            if(l.getProjectName().equals(r.getProjectName())){
                return l.getOwner().hashCode()- r.getOwner().hashCode();
            }else{
                return l.getProjectName().compareTo(r.getProjectName());
            }
        });

    }
}