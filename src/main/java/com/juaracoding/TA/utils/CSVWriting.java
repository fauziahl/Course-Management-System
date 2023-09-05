package com.juaracoding.TA.utils;


import com.juaracoding.TA.configuration.OtherConfig;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;



public class CSVWriting {

    private FileWriter fW;
    private String[] exceptionString = new String[2];
    private CSVPrinter csvPrinter ;

    private String[] strArr;

    private ServletOutputStream outputStream ;

    public CSVWriting(String[][] datas, String[] header, HttpServletResponse response)
    {
        exceptionString[0] = "CSVWriting";
        writeToCsv(datas, header,response);
    }

    /*THE DATA SET FROM SQL STATEMENT DATABASE */
    public void writeToCsv(String[][] datas,String[] header,HttpServletResponse response)
    {
        try
        {
            csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT);
            csvPrinter.printRecord(header);

            for(int i=0;i<datas.length;i++)
            {
                strArr = new String[header.length];

                for(int j=0;j<header.length;j++)
                {
                    strArr[j] = datas[i][j];
                }
                csvPrinter.printRecord(strArr);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            exceptionString[1] = "writeToCsv(List<HashMap<String,Object>> datas, String pathDestination,String[] header,char separator)  -- EXCEPTION LINE 73";
            LoggingFile.exceptionStringz(exceptionString, e, OtherConfig.getFlagLogging());
        }finally {

            try {
                outputStream.close();
                fW.flush();
                csvPrinter.flush();
                csvPrinter.close();
                fW.close();
            }catch (Exception e) {
                e.printStackTrace();
                exceptionString[1] = "writeToCsv(List<HashMap<String,Object>> datas, String pathDestination,String[] header,char separator)  -- EXCEPTION LINE 84";
                LoggingFile.exceptionStringz(exceptionString, e, OtherConfig.getFlagLogging());
            }
        }
    }

}