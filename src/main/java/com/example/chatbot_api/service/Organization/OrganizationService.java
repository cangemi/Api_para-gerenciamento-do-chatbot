package com.example.chatbot_api.service.Organization;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chatbot_api.Utils.AuthUtils.AuthUtils;
import com.example.chatbot_api.domain.Organization.Organization;
import com.example.chatbot_api.domain.User.User;
import com.example.chatbot_api.dto.Organization.OrganizationDTO;
import com.example.chatbot_api.repositories.Organization.OrganizationRepository;
import com.example.chatbot_api.repositories.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {


    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public List<Organization> getAllOrganizations(){

        String id = AuthUtils.getCurrentUserId();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return organizationRepository.findByUserId(user.getId());
    }

    public List<Organization> createOrganization(OrganizationDTO organizationDTO){
        String id = AuthUtils.getCurrentUserId();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Organization organization = new Organization();
        organization.setName(organizationDTO.name());
        organization.setDescription(organizationDTO.description());
        organization.setCreationDate(LocalDateTime.now());
        organization.setUser(user);
        
        organizationRepository.save(organization);

        return organizationRepository.findByUserId(user.getId());
    }

    public List<Organization> updateOrganization( String organizationId ,OrganizationDTO organizationDTO){
        String id = AuthUtils.getCurrentUserId();

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Organization organization = organizationRepository.findOrganizationByPublicId(organizationId)
            .orElseThrow(() -> new RuntimeException("Organização não encontrada"));
        
        if (!organization.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Você não tem permissão para atualizar esta organização");
            }
        
        organization.setName(organizationDTO.name());
        organization.setDescription(organizationDTO.description());

        organizationRepository.save(organization);
        
        return organizationRepository.findByUserId(user.getId());
    }

    public List<Organization> deleteOrganizations(String publicId){
        Organization organization = organizationRepository.findOrganizationByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Organização não encontrada com publicId: " + publicId));

        String userId = organization.getUser().getId();
        
        organizationRepository.delete(organization);

        return organizationRepository.findByUserId(userId);
    }
}
