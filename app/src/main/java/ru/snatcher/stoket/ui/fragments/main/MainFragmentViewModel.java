package ru.snatcher.stoket.ui.fragments.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ProductsRepository;
import ru.snatcher.stoket.data.repositories.UserRepository;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;

public class MainFragmentViewModel extends ViewModel {

    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;

    @Inject
    MainFragmentViewModel(ProductsRepository productsRepository, UserRepository userRepository) {
        this.productsRepository = productsRepository;
        this.userRepository = userRepository;
    }

    final LiveData<Resource<List<Product>>> getProducts(long limit, long offset,
                                                        String brand, String type, String priceMin,
                                                        String priceMax, int gender,
                                                        boolean isPagination) {

        return productsRepository.loadProducts(limit, offset, brand, type, priceMin, priceMax,
                false, !isPagination, gender);
    }

    final LiveData<Resource<List<String>>> loadBrands() {
        return productsRepository.getBrands();
    }

    final LiveData<Resource<List<String>>> loadTypes() {
        return productsRepository.getTypes();
    }

    final int getGender() {
        return userRepository.getGender();
    }
}