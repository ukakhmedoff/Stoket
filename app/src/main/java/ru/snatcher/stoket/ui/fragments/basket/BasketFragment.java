package ru.snatcher.stoket.ui.fragments.basket;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.databinding.FragmentBasketBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.activity.main.MainActivity;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.ui.common.recycler.ProductsListAdapter;
import ru.snatcher.stoket.util.AutoClearedValue;

public class BasketFragment extends LifecycleFragment implements Injectable {
    final DataBindingComponent bindingComponent = new FragmentDataBindingComponent(this);

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    private BasketViewModel viewModel;

    private AutoClearedValue<ProductsListAdapter> productsAdapter;
    private AutoClearedValue<FragmentBasketBinding> binding;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        final FragmentBasketBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_basket, container, false, bindingComponent);
        this.binding = new AutoClearedValue<>(this, binding);

        return binding.getRoot();
    }

    @Override
    public final void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders
                .of(this, viewModelProviderFactory)
                .get(BasketViewModel.class);

        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.navigation_bar_item_shopping_basket);
        }
        initUi();
    }

    private void initUi() {
        final ProductsListAdapter adapter = new ProductsListAdapter(bindingComponent,
                navigationController::navigateToProduct);
        final RecyclerView recycler = binding.get().basketProducts.recycler;
        recycler.setAdapter(adapter);
        productsAdapter = new AutoClearedValue<>(this, adapter);
        initProducts();
    }

    private void initProducts() {
        viewModel.getProducts().observe(this, observer -> {
            if (observer != null) {
                productsAdapter.get().replace(observer.data);
                binding.get().executePendingBindings();
            }
        });
    }
}