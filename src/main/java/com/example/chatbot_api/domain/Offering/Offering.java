package com.example.chatbot_api.domain.Offering;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.chatbot_api.domain.Organization.Organization;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "offerings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Offering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    private String name;

    private String price;

    private String totalSold = "0";

    @OneToMany(mappedBy = "offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfferingImage> images = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean onUpdate = false;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @PrePersist
    public void generatePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    public void markAsUpdated() {
        onUpdate = true;
    }
}
