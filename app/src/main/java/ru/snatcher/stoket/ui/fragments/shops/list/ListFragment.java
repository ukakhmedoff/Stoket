package ru.snatcher.stoket.ui.fragments.shops.list;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.databinding.FragmentShopListBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.ui.common.recycler.ShopsAdapter;
import ru.snatcher.stoket.util.AutoClearedValue;

public class ListFragment extends LifecycleFragment implements Injectable {
    final DataBindingComponent component = new FragmentDataBindingComponent(this);
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    NavigationController controller;
    ListFragmentViewModel viewModel;
    AutoClearedValue<ShopsAdapter> adapter;
    private AutoClearedValue<FragmentShopListBinding> binding;

    @Override
    public final LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final FragmentShopListBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_shop_list, container, false, component);
        this.binding = new AutoClearedValue<>(this, binding);
        return binding.getRoot();
    }

    @Override
    public final void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory)
                .get(ListFragmentViewModel.class);
        binding.get().setRetryCallback(this::getShops);
        initRecyclerView();
    }

    private void initRecyclerView() {
        final ShopsAdapter mainListAdapter = new ShopsAdapter(component,
                shop -> controller.navigateToShopInfo(shop.id));
        final RecyclerView recyclerView = binding.get().recyclerShopsList.recycler;
        recyclerView.setAdapter(mainListAdapter);
        adapter = new AutoClearedValue<>(this, mainListAdapter);

        getShops();
    }

    final void getShops() {
        viewModel.getShops().observe(this, resource -> {
            if (resource != null) {
                adapter.get().replace(resource.data);
                binding.get().setResource(resource);
            }
        });
    }
}
