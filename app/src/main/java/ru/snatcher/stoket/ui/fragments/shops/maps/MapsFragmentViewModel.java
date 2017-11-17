package ru.snatcher.stoket.ui.fragments.shops.maps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ShopsRepository;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Shop;

public class MapsFragmentViewModel extends ViewModel {

    private final ShopsRepository repository;

    @Inject
    MapsFragmentViewModel(ShopsRepository repository) {
        this.repository = repository;
    }

    final LiveData<Resource<Shop>> getShop(long id) {
        return repository.load(id);
    }

    public final LiveData<Resource<List<Shop>>> getShops() {
        return repository.loadAll(false);
    }
}
