package com.cjhercen.springboot.app.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


public class InformesUtils {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public InformesUtils() {
		
	}
	 
	/*
	 * Método para crear informe PDF pasándole los fichajes del empleado
	 * De momento es el único método de prueba utilizando todos los datos en total
	 */
    public void crearInformePDF( HttpServletResponse response, HashMap<String,Object> parametros) throws JRException, SQLException {

        try {
            InputStream jrxmlInput = this.getClass().getResourceAsStream("/static/informes/informeFichajes.jrxml");
            JasperDesign design = JRXmlLoader.load(jrxmlInput);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            logger.info("Report compiled");

            // It is possible to generate Jasper reports from a JSON source.
            Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost/db_jobtime?serverTimezone=Europe/Madrid&useSSL=false&allowPublicKeyRetrieval=true","root", "j0bT1me*");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            logger.info("JasperPrint" + jasperPrint);

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
            pdfExporter.exportReport();

            response.setContentType("application/pdf");
            response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
            response.addHeader("Content-Disposition", "attachment; filename=jasper.pdf;");
            
            OutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(pdfReportStream.toByteArray());
            responseOutputStream.close();
            pdfReportStream.close();
            logger.info("Completed Successfully: ");
        } catch (Exception e) {
            logger.info("Error: ", e);
        }
   
    }
	
	
}
