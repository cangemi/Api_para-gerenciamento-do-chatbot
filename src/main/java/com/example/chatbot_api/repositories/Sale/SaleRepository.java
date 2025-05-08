package com.example.chatbot_api.repositories.Sale;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.chatbot_api.domain.Sale.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByPublicId(String publicId);

    Optional <List<Sale>> findAllByBot_PublicId(String botId);

}
