package com.example.chatbot_api.controllers.Sale;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatbot_api.dto.Sale.SaleDTO;
import com.example.chatbot_api.service.Sale.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    public List<SaleDTO> getAllSales(@RequestParam String botId) {
        return saleService.getAllSales(botId)
            .stream()
            .map(sale -> new SaleDTO(
                sale.getPublicId(),
                sale.getQuantidade(),
                sale.getPrice(),
                sale.getNomeCliente(),
                sale.getEndereco(),
                sale.getOffering().getName(),
                sale.getBot().getName()
            ))
            .toList();
    }

    @PostMapping
    public List<SaleDTO> createSale(@RequestParam String botId, @RequestBody SaleDTO saleDTO) {
        
        return saleService.createSale(botId, saleDTO)
            .stream()
            .map(sale -> new SaleDTO(
                sale.getPublicId(),
                sale.getQuantidade(),
                sale.getPrice(),
                sale.getNomeCliente(),
                sale.getEndereco(),
                sale.getOffering().getName(),
                sale.getBot().getName()
            ))
            .toList();
    }

    @PutMapping
    public List<SaleDTO> updateSale(@RequestParam String saleId, @RequestBody SaleDTO saleDTO) {

        return saleService.updateSale(saleId, saleDTO)
            .stream()
            .map(sale -> new SaleDTO(
                sale.getPublicId(),
                sale.getQuantidade(),
                sale.getPrice(),
                sale.getNomeCliente(),
                sale.getEndereco(),
                sale.getOffering().getName(),
                sale.getBot().getName()
            ))
            .toList();
    }

    @DeleteMapping
    public List<SaleDTO> deleteSale(@RequestParam String saleId) {
    
        return saleService.deleteSale(saleId)
            .stream()
            .map(sale -> new SaleDTO(
                sale.getPublicId(),
                sale.getQuantidade(),
                sale.getPrice(),
                sale.getNomeCliente(),
                sale.getEndereco(),
                sale.getOffering().getName(),
                sale.getBot().getName()
            ))
            .toList();
    }
}