package com.example.chatbot_api.controllers.Offering;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.chatbot_api.dto.Offering.OfferingDTO;
import com.example.chatbot_api.dto.Offering.OfferingImageDTO;
import com.example.chatbot_api.service.Offering.OfferingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/offering")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService offeringService;

@GetMapping
public List<OfferingDTO> getAllOfferings(@RequestParam String organizationId) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

    return offeringService.getAllOfferings(organizationId)
        .stream()
        .map(offering -> {
            List<OfferingImageDTO> imageDTOs = offering.getImages()
                .stream()
                .map(image -> new OfferingImageDTO(
                    image.getPublicId(),
                    baseUrl + image.getImageUrl()
                ))
                .toList();

            return new OfferingDTO(
                offering.getName(),
                offering.getPrice(),
                offering.getTotalSold(),
                offering.getDescription(),
                offering.getPublicId(),
                imageDTOs
            );
        })
        .toList();
}

    @PostMapping
    public List<OfferingDTO> createOffering(@RequestParam String organizationId, @RequestBody OfferingDTO offeringDTO, HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return offeringService.createOffering(organizationId,offeringDTO)
        .stream()
        .map(offering -> {
            List<OfferingImageDTO> imageDTOs = offering.getImages()
                .stream()
                .map(image -> new OfferingImageDTO(
                    image.getPublicId(),
                    baseUrl + image.getImageUrl()
                ))
                .toList();

            return new OfferingDTO(
                offering.getName(),
                offering.getPrice(),
                offering.getTotalSold(),
                offering.getDescription(),
                offering.getPublicId(),
                imageDTOs
            );
        })
        .toList();
    }

    @PutMapping
    public List<OfferingDTO> updateOffering(@RequestParam String publicId, @RequestBody OfferingDTO offeringDTO, HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return offeringService.updateOffering(publicId, offeringDTO)
        .stream()
        .map(offering -> {
            List<OfferingImageDTO> imageDTOs = offering.getImages()
                .stream()
                .map(image -> new OfferingImageDTO(
                    image.getPublicId(),
                    baseUrl + image.getImageUrl() // aqui concatena com /images/xyz.jpg
                ))
                .toList();

            return new OfferingDTO(
                offering.getName(),
                offering.getPrice(),
                offering.getTotalSold(),
                offering.getDescription(),
                offering.getPublicId(),
                imageDTOs
            );
        })
        .toList();
    }

    @DeleteMapping("/{publicId}")
    List<OfferingDTO>deleteOffering(@PathVariable  String publicId, HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return offeringService.deleteOffering(publicId)
        .stream()
        .map(offering -> {
            List<OfferingImageDTO> imageDTOs = offering.getImages()
                .stream()
                .map(image -> new OfferingImageDTO(
                    image.getPublicId(),
                    baseUrl + image.getImageUrl()
                ))
                .toList();

            return new OfferingDTO(
                offering.getName(),
                offering.getPrice(),
                offering.getTotalSold(),
                offering.getDescription(),
                offering.getPublicId(),
                imageDTOs
            );
        })
        .toList();
    }
}
