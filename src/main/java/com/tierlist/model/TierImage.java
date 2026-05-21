package com.tierlist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tier_images")
public class TierImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tier_slot", nullable = false)
    private String tierSlot; // e.g. "god", "smash", "__pool__"

    @Column(name = "image_data", columnDefinition = "TEXT", nullable = false)
    private String imageData; // base64 data URL

    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    @Column(name = "sort_order")
    private int sortOrder = 0;

    public TierImage() {}

    public TierImage(String tierSlot, String imageData) {
        this.tierSlot = tierSlot;
        this.imageData = imageData;
    }

    public Long getId() { return id; }
    public String getTierSlot() { return tierSlot; }
    public void setTierSlot(String tierSlot) { this.tierSlot = tierSlot; }
    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
