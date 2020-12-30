
package com.rest.api_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/upload")
public class MyResource {
	
	String basePath = System.getProperty("user.home") + "/Downloads/";
    
    @GET 
    @Produces("text/plain")
    public String getIt() {
        return "API Works!";
    }
    
    @POST 
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadImage(
    		@FormDataParam("image") InputStream imageStream,
    		@FormDataParam("image") FormDataContentDisposition imageMetaData
    		) throws Exception {
    	// exec start time
    	long startTime = System.nanoTime();
    	
    	String scannedText = "";
    	// File Path to save
    	String UPLOAD_PATH = basePath;
    	
    	// File path
    	File filePath = new File(UPLOAD_PATH + imageMetaData.getFileName());
    	try {
    		// File size allocation
    		int read = 0;
    		byte[] bytes = new byte[1024];
    		
    		// File output stream
    		OutputStream out = new FileOutputStream(filePath);
    		
    		// Writing into file
    		while ((read = imageStream.read(bytes)) != -1) 
            {
                out.write(bytes, 0, read);
            }
    		
    		// Flush and close
    		out.flush();
    	    out.close();
    	} catch(IOException e)
    	{
    		 throw new WebApplicationException();
    	}
    	
    	// Reading text from image
    	scannedText = readTextFromImage(filePath);
    	
    	// Execution end time
    	long endTime = System.nanoTime();
    	
    	// Write Performance log
    	writeOCRPerformance(startTime, endTime, scannedText, imageMetaData.getFileName());
    	// Success response
    	return Response.ok(scannedText).build();
    	
    }
    
    private String readTextFromImage(File imageFile) throws TesseractException
    {
    	Tesseract tesseract = new Tesseract();
    	tesseract.setDatapath(System.getProperty("user.home") + "/tessdata/");
    	String detectedText = tesseract.doOCR(imageFile);
    	return detectedText;
    }
    
    private void writeOCRPerformance(long startTime, long endTime, String readedText, String fileName) throws IOException
    {
    	long calcutatedInterval = endTime - startTime;
    	
    	String logText = "[" + fileName + "]\r\n" + readedText + (double)calcutatedInterval/1000000000 + "s\r\n";
    	
    	File logFile = new File(basePath + "OCR Log");
    	if (!logFile.exists())
    	{
    		logFile.createNewFile();
    	}
    	
    	FileWriter logFW = new FileWriter(logFile, true);
    	BufferedWriter logBuffer = new BufferedWriter(logFW);
    	PrintWriter pw = new PrintWriter(logBuffer);
    	pw.println(logText);
    	
    	pw.close();
    	logBuffer.close();
    	logFW.close();
    }
    
    
}
