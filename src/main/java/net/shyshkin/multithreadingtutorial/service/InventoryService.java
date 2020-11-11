package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.Inventory;
import net.shyshkin.multithreadingtutorial.domain.ProductOption;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;

import java.util.concurrent.CompletableFuture;

public class InventoryService {
    public Inventory addInventory(ProductOption productOption) {
        CommonUtil.delay(500);
        return Inventory.builder()
                .count(2).build();

    }

    public CompletableFuture<Inventory> addInventory_CF(ProductOption productOption) {

        return CompletableFuture.supplyAsync(() -> {
            CommonUtil.delay(500);
            return Inventory.builder()
                    .count(2).build();
        });

    }
}
