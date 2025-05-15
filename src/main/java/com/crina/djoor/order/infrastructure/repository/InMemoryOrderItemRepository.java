package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderItemRepository;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.order.domain.vo.CartItem;
import com.crina.djoor.order.domain.vo.TopSellingProduct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryOrderItemRepository implements OrderItemRepository {

    private final List<Order> allOrders = new ArrayList<>();

    public void addOrder(Order order) {
        this.allOrders.add(order);
    }

    @Override
    public void add(OrderItem orderItem) throws ErrorOnSaveOrderException {
        // Pas nécessaire ici car nous utilisons Order, pas OrderItem
    }

    @Override
    public List<TopSellingProduct> findSoldProductsBetween(Date from, Date to) {
        return allOrders.stream()
                .filter(order -> {
                    // Vérifie que la commande est dans la période demandée et qu'elle est payée
                    Date createdAt = order.snapshot().createdAt();
                    return !createdAt.before(from) && !createdAt.after(to) && order.snapshot().state() == OrderState.PAID;
                })
                .flatMap(order -> order.items().stream()) // Parcours les items de la commande
                .collect(Collectors.groupingBy(
                        item -> item.product().id(), // Regroupe par ID du produit
                        Collectors.summingInt(CartItem::quantity) // Calcule la quantité totale vendue pour chaque produit
                ))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue()) // Trie les produits par quantité vendue
                .map(e -> new TopSellingProduct(e.getKey(), e.getValue())) // Crée des objets TopSellingProduct
                .toList(); // Retourne la liste des produits les plus vendus
    }
}
