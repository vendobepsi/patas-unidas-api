package com.patasunidasapi.patasunidasapi.Utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ImageConverter {
    
private final String UPLOAD_DIR = "./src/main/resources/static/uploads";

    //decode b64 to image, save and return path
    public String decodeB64 (String photoBase64) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (photoBase64.contains(",")) {
            photoBase64 = photoBase64.split(",")[1];
        }

        String uuid = UUID.randomUUID().toString();
        String filename = uuid + ".jpg";

        try {
            byte[] decodedImg = Base64.getDecoder().decode(photoBase64);
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, decodedImg);
        } catch(IllegalArgumentException e) {
            throw new IOException("String Base64 Inválida", e);
        }
        
        return uuid;

    }

    public byte[] getImageBytes(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName + ".jpg");
        
        if (!Files.exists(filePath)) {
             throw new IOException("Imagem não encontrada: " + fileName);
        }
        
        return Files.readAllBytes(filePath);
    }
}
