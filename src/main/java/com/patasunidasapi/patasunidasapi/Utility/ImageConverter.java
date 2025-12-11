package com.patasunidasapi.patasunidasapi.Utility;

import com.patasunidasapi.patasunidasapi.model.ImageStore;
import com.patasunidasapi.patasunidasapi.repository.ImageStoreRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Component
public class ImageConverter {

    private final ImageStoreRepository imageStoreRepository;

    public ImageConverter(ImageStoreRepository imageStoreRepository) {
        this.imageStoreRepository = imageStoreRepository;
    }

    public String decodeB64(String photoBase64) throws IOException {
        if (photoBase64.contains(",")) {
            photoBase64 = photoBase64.split(",")[1];
        }

        String uuid = UUID.randomUUID().toString();
        
        ImageStore image = new ImageStore(uuid, photoBase64);
        imageStoreRepository.save(image);
        
        return uuid; 
    }

    public byte[] getImageBytes(String fileName) throws IOException {
        
        ImageStore image = imageStoreRepository.findById(fileName)
                .orElseThrow(() -> new IOException("Imagem não encontrada no banco: " + fileName));
        
        try {
            return Base64.getDecoder().decode(image.getBase64Data());
        } catch (IllegalArgumentException e) {
            throw new IOException("Erro ao decodificar Base64 do banco", e);
        }
    }
}