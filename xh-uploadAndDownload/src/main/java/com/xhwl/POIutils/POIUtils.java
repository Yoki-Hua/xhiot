package com.xhwl.POIutils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class POIUtils {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String DATE_FORMAT = "yyyy/MM/dd";
    /**
     * 读入excel文件，解析后返回,返回值List<String[]>其实是将每一个单元格封装在String[]中，而每一个String[]又做为每一列封装了List中。
     * @param file 将上传后的文件传入到方法中
     * @throws IOException
     */
    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件是否是一个excel文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if(workbook != null){
            //遍历所有的工作表
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;//如果第一个工作表是空的，就跳出，继续循环下一个工作表。
                }
                //获得当前sheet中有数据的第一行（防止可能前面几行没有数据）
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet中有数据的最后一行的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    //获取第一行（标题行）的长度
                    Row firstRow = sheet.getRow(firstRowNum);
                    if(row == null){
                        continue;//如果当前行是空，跳出，继续遍历下一个
                    }
                    //获得当前行中有数据的第一个单元格
                    int firstCellNum = row.getFirstCellNum();
                    //获得第一行（标题行）中不为空的单元格的个数
                    int lastCellNum = firstRow.getPhysicalNumberOfCells();
                    //创建一个固定大小的数组
                    String[] cells = new String[lastCellNum];
                    //循环当前行中的单元格
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        //获取有数据的单元格
                        Cell cell = row.getCell(cellNum);
                        //将单元格中的数据都转换成字符串并存入数组中，每一个数组就相当于行中的所有数据
                        cells[cellNum] = getCellValue(cell);
                    }
                    //将每一行的数据存入集合中
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }

    //校验文件是否合法
    public static void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            throw new IOException(fileName + "不是excel文件");
        }
    }
    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }



    /**
     * 读取excel模板，并复制到新文件中供写入和下载
     * @return
     */
    public static File createNewFile(File file){
        String path = file.getAbsolutePath();
        //读取模板，并赋值到新文件
        //新的文件名
        String newFileName = "人员信息"+System.currentTimeMillis() + ".xlsx";

        //写入到新的excel
        File newFile = new File(path + newFileName);
        try {
            //复制模板到新文件
            fileChannelCopy(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }
    /**
     * 复制到的新文件
     * @param modelFile
     * @param newFile
     */
    public static void fileChannelCopy(File modelFile, File newFile) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(modelFile),1024);
                out = new BufferedOutputStream(new FileOutputStream(newFile),1024);
                byte[] buffer = new byte[1024];
                int len;
                while ((len=in.read(buffer))!=-1) {
                    out.write(buffer,0,len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 下载成功后删除
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
