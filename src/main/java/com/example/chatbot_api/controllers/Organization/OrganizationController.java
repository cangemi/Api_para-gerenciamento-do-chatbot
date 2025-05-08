package com.example.chatbot_api.controllers.Organization;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatbot_api.dto.Organization.OrganizationDTO;
import com.example.chatbot_api.service.Organization.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<OrganizationDTO> getAllOrganizations(){
        return organizationService.getAllOrganizations()
        .stream()
        .map(organization-> new OrganizationDTO(
            organization.getName(),
            organization.getDescription(),
            organization.getPublicId()
        )).toList();
    }

    @PostMapping
    public List<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        return organizationService.createOrganization(organizationDTO).stream()
        .map(organization-> new OrganizationDTO(
            organization.getName(),
            organization.getDescription(),
            organization.getPublicId()
        )).toList();
    }

    @PutMapping
    public List<OrganizationDTO> updateOrganization(@RequestBody String publicId, @RequestBody OrganizationDTO organizationDTO){
        return organizationService.updateOrganization(publicId, organizationDTO).stream()
        .map(organization-> new OrganizationDTO(
            organization.getName(),
            organization.getDescription(),
            organization.getPublicId()
        )).toList();
    }

    @DeleteMapping
    List<OrganizationDTO>deleteOffering(@RequestParam String publicId){
        return organizationService.deleteOrganizations(publicId)
        .stream()
        .map(organization-> new OrganizationDTO(
            organization.getName(),
            organization.getDescription(),
            organization.getPublicId()
        )).toList();
    }

}
