package com.zetcode.service;

import com.zetcode.form.FileUploadForm;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Path("upload")
public class FileUploadService {

    Logger log = Logger.getLogger(FileUploadService.class);

    @POST
    @Consumes("multipart/form-data")
    public Response uploadFile(@MultipartForm FileUploadForm form) {

        var data = form.getData();
        var fileName = form.getFileName();

        if (data.length == 0 || fileName.isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("form not filled correctly").build();
        }

        var isWritten = writeFile(form.getData(), fileName);

        if (isWritten) {

            var msg = String.format("File %s uploaded", fileName);
            return Response.status(200).entity(msg).build();
        } else {

            var msg = String.format("Failed to upload %s file", fileName);
            return Response.status(500).entity(msg).build();
        }
    }

    private boolean writeFile(byte[] content, String fileName) {

        var dirName = "C:/Users/Jano/Documents/tmp";
        var filePath = String.format("%s/%s", dirName, fileName);

        OpenOption[] options = {CREATE, WRITE};

        try {
            Files.write(Paths.get(filePath), content, options);
        } catch (IOException e) {
            log.error("failed to save file", e);

            return false;
        }

        return true;
    }
}
