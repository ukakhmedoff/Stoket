package ru.snatcher.stoket.ui.fragments.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ProductsRepository;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;

public class ProductInfoFragmentViewModel extends ViewModel {

    final ProductsRepository repository;

    @Inject
    ProductInfoFragmentViewModel(ProductsRepository repository) {
        this.repository = repository;
    }

    final LiveData<Resource<Product>> getProduct(long id) {
        return repository.getProduct(id);
    }

    final void saveProduct(Product product) {
        repository.saveProduct(product);
    }
}