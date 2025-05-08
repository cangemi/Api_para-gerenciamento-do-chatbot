package com.example.chatbot_api.controllers.Offering;

import com.example.chatbot_api.dto.Offering.OfferingImageDTO;
import com.example.chatbot_api.service.Offering.OfferingImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offering-image")
public class OfferingImageController {

    private final OfferingImageService offeringImageService;

    // GET: Lista todas as imagens de uma oferta
    @GetMapping
    public ResponseEntity<List<OfferingImageDTO>> getAll(
            @RequestParam String offeringId,
            HttpServletRequest request) {

        String baseUrl = getBaseUrl(request);

        List<OfferingImageDTO> images = offeringImageService.getAllOfferingsImages(offeringId)
            .stream()
            .map(image -> new OfferingImageDTO(
                image.getPublicId(),
                baseUrl + image.getImageUrl()
            ))
            .toList();

        return ResponseEntity.ok(images);
    }

    // POST: Cadastra uma nova imagem para uma oferta
    @PostMapping
    public ResponseEntity<List<OfferingImageDTO>> uploadImage(
            @RequestParam String offeringId,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {

        String baseUrl = getBaseUrl(request);

        List<OfferingImageDTO> images = offeringImageService.createOfferingImage(offeringId, image)
            .stream()
            .map(img -> new OfferingImageDTO(
                img.getPublicId(),
                baseUrl + img.getImageUrl()
            ))
            .toList();

        return ResponseEntity.ok(images);
    }

    // PUT: Atualiza a imagem existente mantendo o mesmo nome de arquivo
    @PutMapping
    public ResponseEntity<List<OfferingImageDTO>> updateImage(
            @RequestParam String offeringImageId,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {

        String baseUrl = getBaseUrl(request);

        List<OfferingImageDTO> images = offeringImageService.updateOfferingImage(offeringImageId, image)
            .stream()
            .map(img -> new OfferingImageDTO(
                img.getPublicId(),
                baseUrl + img.getImageUrl()
            ))
            .toList();

        return ResponseEntity.ok(images);
    }

    // DELETE: Remove uma imagem e o arquivo físico
    @DeleteMapping
    public ResponseEntity<List<OfferingImageDTO>> deleteImage(
            @RequestParam String offeringImageId,
            HttpServletRequest request) {

        String baseUrl = getBaseUrl(request);

        List<OfferingImageDTO> images = offeringImageService.deleteOfferingImage(offeringImageId)
            .stream()
            .map(img -> new OfferingImageDTO(
                img.getPublicId(),
                baseUrl + img.getImageUrl()
            ))
            .toList();

        return ResponseEntity.ok(images);
    }

    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
