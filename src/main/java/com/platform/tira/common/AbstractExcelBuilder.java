package com.platform.tira.common;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

public abstract class AbstractExcelBuilder<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExcelBuilder.class);

    public void createExcel(final OrderType type, List<T> datas, HttpServletResponse response) {
        if (0 == datas.size()) {
            return;
        }
        XSSFWorkbook workBook = new XSSFWorkbook();

        OutputStream os;
        try {
            Sheet sheet = createSheet(workBook);
            if (sheet == null){
                return;
            }

            // 写表头
            writeHead(type, sheet, datas);

            // 写内容
            XSSFCellStyle cellStyle = workBook.createCellStyle();
            XSSFDataFormat format = workBook.createDataFormat();
            writeBody(type, cellStyle, format, sheet, datas);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=OrderDetail.xls");
            os = response.getOutputStream();
            workBook.write(response.getOutputStream());
            os.flush();
        } catch (Throwable t) {
            logger.error("导出 excel出错！", t);
        }
    }

    /**
     * 写表头
     */
    protected void writeHead(OrderType type, Sheet sheet, List<T> datas) {
        String[] head = getHead(type, datas.get(0));
        Row row = sheet.createRow(0);
        for (int i = 0; i < head.length; i++) {
            row.createCell(i).setCellValue(head[i]);
            sheet.setColumnWidth(i, head[i].getBytes().length * 256);
        }
    }

    /**
     * 写表体
     */
    protected abstract void writeBody(OrderType type, XSSFCellStyle cellStyle, XSSFDataFormat format, Sheet sheet, List<T> datas)
            throws Exception;

    /**
     * 表头内容
     */
    protected abstract String[] getHead(OrderType type, T datas);

    /**
     * Sheet页名称
     */
    protected String getSheetName() {
        return "xo";
    }

    /**
     * 来个Sheet
     */
    private XSSFSheet createSheet(XSSFWorkbook workBook) {
        if (StringUtils.isBlank(getSheetName())) {
            return workBook.createSheet();
        } else {
            return workBook.createSheet(getSheetName());
        }
    }

    /**
     * 设置下载文件中文件的名称
     */
    public static String encodeFilename(String filename) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa
         * Toolbar) 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717
         * Firefox/1.0.6
         */
        try {
            // 先检查IE浏览器的
            String newFileName = URLEncoder.encode(filename, "UTF-8");
            newFileName = StringUtils.replace(newFileName, "+", "%20");
            if (newFileName.length() > 150) {
                newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                newFileName = StringUtils.replace(newFileName, " ", "%20");
            }
            if (StringUtils.isBlank(newFileName)) {
                // 若不是IE，则检查Firxbox
                return MimeUtility.encodeText(filename, "UTF-8", "B");
            }
            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }
}