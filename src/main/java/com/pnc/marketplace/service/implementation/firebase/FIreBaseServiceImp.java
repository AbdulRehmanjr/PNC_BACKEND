package com.pnc.marketplace.service.implementation.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.stereotype.Service;


import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.firebase.cloud.StorageClient;
import com.pnc.marketplace.service.firebase.FireBaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FIreBaseServiceImp implements FireBaseService {

    /**
     * The function saves a file to a cloud storage bucket and returns the media
     * link of the saved
     * file.
     * 
     * @param fileName   The name of the file to be saved.
     * @param fileStream The fileStream parameter is an InputStream object that
     *                   represents the content
     *                   of the file that needs to be saved.
     * @param fileType   The `fileType` parameter represents the type of the file
     *                   being saved. It could
     *                   be a file extension like "txt", "pdf", "jpg", etc.
     * @return The method is returning the media link of the saved file.
     */
    @Override
    public String saveFile(String fileName, InputStream fileStream, String fileType) {

        Bucket bucket = StorageClient.getInstance().bucket();
        Acl acl = Acl.of(User.ofAllUsers(), Role.READER);
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName)
                .setContentType(fileType)
                .setAcl(Arrays.asList(acl))
                .build();
        Storage storage = bucket.getStorage();
        try {
            Blob blob = storage.createFrom(blobInfo, fileStream);
            return blob.getMediaLink();
        } catch (IOException e) {   
            log.error("Error {}",e.getMessage());
            return null;
        }
    }

}
