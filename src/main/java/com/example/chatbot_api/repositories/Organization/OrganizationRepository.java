package com.example.chatbot_api.repositories.Organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatbot_api.domain.Organization.Organization;


public interface OrganizationRepository extends JpaRepository<Organization, Long>{

    List<Organization> findByUserId(String userId);

    Optional<Organization> findOrganizationByPublicId(String publicId);

}
