package ru.snatcher.stoket.ui.fragments.basket;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ProductsRepository;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;

public class BasketViewModel extends ViewModel {

    private final ProductsRepository repository;

    @Inject
    BasketViewModel(ProductsRepository repository) {
        this.repository = repository;
    }

    final LiveData<Resource<List<Product>>> getProducts() {
        return repository.loadProducts(0, 0, null, null, null,
                null, true, false, 0);
    }
}