package ru.snatcher.stoket.ui.fragments.shops.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ShopsRepository;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Shop;

public class ListFragmentViewModel extends ViewModel {

    private final ShopsRepository repository;

    @Inject
    ListFragmentViewModel(ShopsRepository repository) {
        this.repository = repository;
    }

    final LiveData<Resource<List<Shop>>> getShops() {
        return repository.loadAll(false);
    }
}
