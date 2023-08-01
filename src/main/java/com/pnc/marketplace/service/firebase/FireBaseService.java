package com.pnc.marketplace.service.firebase;

import java.io.InputStream;

public interface FireBaseService {


   /**
    * The function saves a file with a given name, input stream, and file type.
    * 
    * @param fileName A string representing the name of the file to be saved.
    * @param fileStream The fileStream parameter is an InputStream object that represents the input
    * stream of the file that needs to be saved. It is used to read the contents of the file.
    * @param fileType The fileType parameter is a string that represents the type or format of the file
    * being saved. It could be a file extension like "txt" for a text file, "jpg" for an image file,
    * "pdf" for a PDF file, etc.
    * @return The method is returning a String value.
    */
   String saveFile(String fileName, InputStream fileStream,String fileType);
}
