package com.zetcode.form;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

public class FileUploadForm {

    private byte[] data;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    @FormParam("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {

        return data;
    }

    @FormParam("selectedFile")
    @PartType("application/octet-stream")
    public void setData(byte[] data) {

        this.data = data;
    }
}
