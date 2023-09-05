package com.juaracoding.TA.utils;

import com.juaracoding.TA.configuration.OtherConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelWriter {

    private XSSFWorkbook workbook ;
    private XSSFRow row;
    private XSSFSheet spreadsheet ;
    private Map<Integer, String[]> mapData  ;
    private String[] exceptionString = new String[2];
    private String[] strDatas = null;
    private ServletOutputStream outputStream ;

    /*SELECT LIST FROM RESULT SQL STATEMENT SELECT FROM DATABASE*/
    public ExcelWriter(String[][] datas , String[] header, String sheetName,HttpServletResponse response )
    {
        workbook = new XSSFWorkbook();
        spreadsheet = workbook.createSheet(sheetName);
        mapData  = new TreeMap<Integer, String[]>();
        exceptionString[0] = "ExcelWriter";
        writeToExcel(datas, header,response);
    }

    public void writeToExcel(String[][] datas, String[] header, HttpServletResponse response )
    {
        mapData.clear();

        try {
            int k=1;/*initiate for rows data in excel*/

            mapData.put(1,header);

            for(int i=0;i<datas.length;i++)
            {
                k++;/*start from 2*/
                strDatas = new String[header.length];
                for(int j=0;j<header.length;j++)
                {
                    strDatas[j] = datas[i][j];
                }
                mapData.put(k,strDatas);
            }

            Set<Integer> keyid = mapData.keySet();
            int rowid = 0;
            for (int key : keyid) {

                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = mapData.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String)obj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            exceptionString[1] = "writeToExcel(List<HashMap<String,Object>> datas, String pathDestination, String[] header )  -- EXCEPTION LINE  98";
            LoggingFile.exceptionStringz(exceptionString, e, OtherConfig.getFlagLogging());
        }
        finally {
            try {
                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                exceptionString[1] = "writeToExcel(List<HashMap<String,Object>> datas, String pathDestination, String[] header ) -- EXCEPTION LINE  110";
                LoggingFile.exceptionStringz(exceptionString, e, OtherConfig.getFlagLogging());
            }
        }
    }
}