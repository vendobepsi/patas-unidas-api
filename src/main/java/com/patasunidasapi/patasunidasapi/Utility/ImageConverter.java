package com.patasunidasapi.patasunidasapi.Utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class ImageConverter {
    
    //decode b64 to image, save and return path
    public static String decodeB64 (String photo) {
        UUID uuid = UUID.randomUUID();
        String uuidS = uuid.toString();
        byte[] decodedImg = Base64.getDecoder().decode(photo.getBytes(StandardCharsets.UTF_8));

        Path imagepath = Paths.get("./resources/imageDir", uuidS.concat(".jpg"));
        try{
            Files.write(imagepath, decodedImg);
        }catch(IOException e){
            
        }



        return uuidS;
    }

    public static String encodeB64(String path) {
        try{
            byte[] image = Files.readAllBytes(Paths.get("./resources/imageDir", path));
            return Base64.getEncoder().encodeToString(image);

        }catch(IOException e){
            
        }
        return path;
    }
}
