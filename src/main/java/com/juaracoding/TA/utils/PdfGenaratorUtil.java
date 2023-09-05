package com.juaracoding.TA.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Component
public class PdfGenaratorUtil {

    @Autowired
    TemplateEngine templateEngine;

    private StringBuilder sBuild = new StringBuilder();
    public File createPdf(String templateName, Map<String, Object> map,String fileName) throws Exception {

        Context ctx = new Context();
        Iterator itMap = map.entrySet().iterator();
        File outputFile = null;
        while (itMap.hasNext()) {
            Map.Entry pair = (Map.Entry) itMap.next();
            ctx.setVariable(pair.getKey().toString(), pair.getValue());
        }

        String processedHtml = templateEngine.process(templateName, ctx);
        FileOutputStream os = null;

        try {
            outputFile = new File(fileName);
//                    append(fileName).append(".pdf").toString());
            os = new FileOutputStream(outputFile);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, false);

            renderer.finishPDF();

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
        return outputFile;
    }
}