package com.example.chatbot_api.service.Offering;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.chatbot_api.client.Bot.ExternalBotClient;
import com.example.chatbot_api.domain.Bot.Bot;
import com.example.chatbot_api.domain.Offering.Offering;
import com.example.chatbot_api.domain.Organization.Organization;
import com.example.chatbot_api.dto.Offering.OfferingDTO;
import com.example.chatbot_api.repositories.Bot.BotRepository;
import com.example.chatbot_api.repositories.Offering.OfferingRepository;
import com.example.chatbot_api.repositories.Organization.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final OrganizationRepository organizationRepository;
    private final ExternalBotClient externalBotClient;
    private final BotRepository botRepository;

    public List<Offering> getAllOfferings(String oraganizationId){
        return offeringRepository.findAllByOrganization_PublicId(oraganizationId)
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));
    }

    public List<Offering> createOffering(String oraganizationId, OfferingDTO offeringDTO){

        Organization organization = organizationRepository.findOrganizationByPublicId(oraganizationId)
            .orElseThrow(() -> new RuntimeException("Nenhuma organização encontrada"));

        Offering offering = new Offering();
        offering.setName(offeringDTO.name());
        offering.setPrice(offeringDTO.price());
        offering.setDescription(offeringDTO.description());
        offering.setOrganization(organization);

        offeringRepository.save(offering);

        externalBotClient.updateRag(organization.getPublicId());

        return offeringRepository.findAllByOrganization_PublicId(oraganizationId)
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));
    }

    public List<Offering> updateOffering(String publicId, OfferingDTO offeringDTO){

        Offering offering = offeringRepository.findOfferingByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));

        offering.setName(offeringDTO.name());
        offering.setPrice(offeringDTO.price());
        offering.setDescription(offeringDTO.description());

        offeringRepository.save(offering);

        return offeringRepository.findAllByOrganization_PublicId(offering.getOrganization().getPublicId())
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));
    }
    
    public List<Offering> deleteOffering(String publicId){
        Offering offering = offeringRepository.findOfferingByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));

        String oraganizationId = offering.getOrganization().getPublicId();

        offeringRepository.delete(offering);

        externalBotClient.updateRag(oraganizationId);

        return offeringRepository.findAllByOrganization_PublicId(oraganizationId)
            .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado"));
    }

}
