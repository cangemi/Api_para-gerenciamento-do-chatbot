package com.example.chatbot_api.service.Offering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.chatbot_api.domain.Offering.Offering;
import com.example.chatbot_api.domain.Offering.OfferingImage;
import com.example.chatbot_api.repositories.Offering.OfferingImageRepository;
import com.example.chatbot_api.repositories.Offering.OfferingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferingImageService {


    private final OfferingImageRepository offeringImageRepository;
    private final OfferingRepository offeringRepository;

    public List<OfferingImage> getAllOfferingsImages (String offeringId){
        return offeringImageRepository.findAllByOffering_PublicId(offeringId)
            .orElseThrow(() -> new RuntimeException("Nenhuma imagem encontrada"));
    }


    public List<OfferingImage> createOfferingImage(String offeringId, MultipartFile image){

        Offering offering = offeringRepository.findOfferingByPublicId(offeringId)
            .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        try {
            // Salvar imagem no disco
            String filename = UUID.randomUUID() + "_" + offering.getPublicId().replaceAll("[^a-zA-Z0-9_-]", "") +".jpeg";
            Path path = Paths.get("images/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());

            // Criar e salvar entidade da imagem
            OfferingImage offeringImage = new OfferingImage();
            offeringImage.setImageUrl("/images/" + filename);
            offeringImage.setOffering(offering);

            offeringImageRepository.save(offeringImage);

            return offeringImageRepository.findAllByOffering_PublicId(offeringId)
            .orElseThrow(() -> new RuntimeException("Nenhuma imagem encontrada"));

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem: " + e.getMessage());
        }
    }


    public List<OfferingImage> updateOfferingImage(String offeringImageId, MultipartFile image) {

        OfferingImage offeringImage = offeringImageRepository.findOfferingImageByPublicId(offeringImageId)
            .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));
    
        try {
            // Pega o caminho atual da imagem (ex: /uploads/abc.jpg)
            String imageUrl = offeringImage.getImageUrl();
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    
            Path path = Paths.get("images/" + filename);
            Files.createDirectories(path.getParent()); 
    
            // Substitui o conteúdo da imagem
            Files.write(path, image.getBytes());
    

            offeringImageRepository.save(offeringImage);
    
            return offeringImageRepository.findAllByOffering_PublicId(offeringImage.getOffering().getPublicId())
                .orElseThrow(() -> new RuntimeException("Nenhuma imagem encontrada"));
    
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem: " + e.getMessage());
        }
    }

    public List<OfferingImage> deleteOfferingImage(String offeringImageId){
        OfferingImage offeringImage = offeringImageRepository.findOfferingImageByPublicId(offeringImageId)
            .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));

        String imageUrl = offeringImage.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1); // extrai só o nome do arquivo
        Path path = Paths.get("images/" + filename);
    
        try {
            // Deleta o arquivo físico
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar o arquivo da imagem: " + e.getMessage());
        }
    
        // Deleta a entidade no banco
        offeringImageRepository.delete(offeringImage);


        return offeringImageRepository.findAllByOffering_PublicId(offeringImage.getOffering().getPublicId())
            .orElseThrow(() -> new RuntimeException("Nenhuma imagem encontrada"));
    }
}
