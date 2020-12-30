
package com.rest.api_server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/upload")
public class MyResource {
    
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
    	String UPLOAD_PATH = System.getProperty("user.home") + "/";
    	try {
    		int read = 0;
    		byte[] bytes = new byte[1024];
    		
    		OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + imageMetaData.getFileName()));
    		while ((read = imageStream.read(bytes)) != -1) 
            {
                out.write(bytes, 0, read);
            }
    		out.flush();
    	    out.close();
    	} catch(IOException e)
    	{
    		 throw new WebApplicationException();
    	}
    	return Response.ok("Data uploaded successfully !!").build();
    }
    
    
}
