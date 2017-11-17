package ru.snatcher.stoket.ui.common;

import javax.inject.Inject;

import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Shop;
import ru.snatcher.stoket.util.Constants;
import ru.terrakok.cicerone.Router;

public class NavigationController {

    private final Router router;

    public String brand;

    @Inject
    public NavigationController(Router router) {
        this.router = router;
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public final void navigateToProduct(Product product) {
        router.navigateTo(Constants.FRAGMENT_PRODUCT_ID, product.id);
    }

    public final void navigateToShopInfo(long id) {
        router.navigateTo(Constants.FRAGMENT_SHOP_ID, id);
    }

    public final void navigateToMaps(Shop shop) {
        router.navigateTo(Constants.FRAGMENT_MAPS_ID, shop);
    }
}