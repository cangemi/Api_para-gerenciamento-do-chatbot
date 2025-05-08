package com.example.chatbot_api.repositories.Offering;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatbot_api.domain.Offering.Offering;


public interface OfferingRepository extends JpaRepository<Offering, Long> {
    
    Optional<Offering> findOfferingByPublicId(String publicId);

    Optional <List<Offering>> findAllByOrganization_PublicId(String oraganizationId);

    Optional<Offering> findByName(String name);

}
