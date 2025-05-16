package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.snapshot.SaleCampaignSnapshot;
import com.crina.djoor.shared.vo.Id;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SaleCampaign {
    private Id id;
    private Date startDate;
    private Date endDate;
    private String code;
    private int percentage;
    private int maxProducts;
    private Set<String> discountedProductIds = new HashSet<>();

    public SaleCampaign(Date startDate, Date endDate, int percentage, int maxProducts) {
        if (percentage < 1 || percentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 1 et 100");
        }
        if (maxProducts < 1) {
            throw new IllegalArgumentException("Le nombre maximum de produits doit être supérieur à 0");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maxProducts = maxProducts;
    }


    public boolean canAddProduct() {
        return discountedProductIds.size() < maxProducts;
    }

    public void addProduct(String productId) {
        if (!canAddProduct()) {
            throw new IllegalStateException("Limite de produits soldés atteinte");
        }
        discountedProductIds.add(productId);
    }


    public int percentage() {
        return percentage;
    }

    public int remainingSlots() {
        return maxProducts - discountedProductIds.size();
    }

    public Set<String> discountedProductIds() {
        return Set.copyOf(discountedProductIds);
    }

    public SaleCampaignSnapshot snapshot() {
        return new SaleCampaignSnapshot(
                this.startDate,
                this.endDate,
                this.code,
                this.percentage,
                this.maxProducts,
                this.discountedProductIds
        );
    }

    public boolean isActive(Date currentDate) {
        return !currentDate.before(startDate) && !currentDate.after(endDate);
    }

    public boolean isProductInSale(String productId) {
        return discountedProductIds.contains(productId);
    }

    public double applyDiscount(double originalPrice) {
        return originalPrice * (1 - percentage / 100.0);
    }
}
