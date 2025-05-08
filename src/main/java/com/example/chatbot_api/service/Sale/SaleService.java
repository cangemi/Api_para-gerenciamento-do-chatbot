package com.example.chatbot_api.service.Sale;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chatbot_api.domain.Bot.Bot;
import com.example.chatbot_api.domain.Offering.Offering;
import com.example.chatbot_api.domain.Sale.Sale;
import com.example.chatbot_api.dto.Sale.SaleDTO;
import com.example.chatbot_api.repositories.Bot.BotRepository;
import com.example.chatbot_api.repositories.Offering.OfferingRepository;
import com.example.chatbot_api.repositories.Organization.OrganizationRepository;
import com.example.chatbot_api.repositories.Sale.SaleRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final OrganizationRepository organizationRepository;
    private final BotRepository botRepository;
    private final OfferingRepository offeringRepository;

    public List<Sale> getAllSales(String botId) {

        return saleRepository.findAllByBot_PublicId(botId)
                .orElseThrow(() -> new RuntimeException("Organization não encontrado"));
    }

    // 2. Criar uma venda
    public List<Sale> createSale(String botId, SaleDTO saleDTO) {

        Bot bot = botRepository.findBotByPublicId(botId)
                .orElseThrow(() -> new RuntimeException("Bot não encontrado"));

        Offering offering = offeringRepository.findByName(saleDTO.offeringName())
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        Sale sale = new Sale();
        sale.setQuantidade(saleDTO.quantidade());
        sale.setPrice(saleDTO.price());
        sale.setNomeCliente(saleDTO.nomeCliente());
        sale.setEndereco(saleDTO.endereco());
        sale.setBot(bot);
        sale.setOffering(offering);

        saleRepository.save(sale);

        return saleRepository.findAllByBot_PublicId(botId)
            .orElseThrow(() -> new RuntimeException("Bot não encontrado"));
    }

    // 3. Atualizar uma venda
    public List<Sale> updateSale(String saleId, SaleDTO saleDTO) {


        Sale sale = saleRepository.findByPublicId(saleId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));


        sale.setQuantidade(saleDTO.quantidade());
        sale.setPrice(saleDTO.price());
        sale.setNomeCliente(saleDTO.nomeCliente());
        sale.setEndereco(saleDTO.endereco());
        sale.setOffering(offeringRepository.findByName(saleDTO.offeringName())
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada")));

        saleRepository.save(sale);

        return saleRepository.findAllByBot_PublicId(sale.getBot().getPublicId())
            .orElseThrow(() -> new RuntimeException("Bot não encontrado"));
    }

    // 4. Remover uma venda
    public List<Sale> deleteSale(String saleId) {
        Sale sale = saleRepository.findByPublicId(saleId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        String botId = sale.getBot().getPublicId();

        saleRepository.delete(sale);

        return saleRepository.findAllByBot_PublicId(botId)
            .orElseThrow(() -> new RuntimeException("Bot não encontrado"));
    }
}
