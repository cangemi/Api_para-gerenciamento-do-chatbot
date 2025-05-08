package com.example.chatbot_api.repositories.Offering;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.chatbot_api.domain.Offering.OfferingImage;

public interface OfferingImageRepository extends JpaRepository<OfferingImage,Long> {

    Optional<OfferingImage> findOfferingImageByPublicId(String publicId);


    Optional <List<OfferingImage>> findAllByOffering_PublicId(String offeringId);
}
