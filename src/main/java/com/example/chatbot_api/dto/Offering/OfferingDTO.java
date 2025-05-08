package com.example.chatbot_api.dto.Offering;

import java.util.List;


public record OfferingDTO (String name, String price, String totalSold, String description, String publicId, List<OfferingImageDTO> images ){}
