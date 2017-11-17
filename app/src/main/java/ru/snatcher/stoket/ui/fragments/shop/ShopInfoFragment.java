package ru.snatcher.stoket.ui.fragments.shop;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Status;
import ru.snatcher.stoket.databinding.FragmentShopInfoBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.activity.main.MainActivity;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.ui.common.recycler.ShopListAdapter;
import ru.snatcher.stoket.util.AutoClearedValue;

public class ShopInfoFragment extends LifecycleFragment implements Injectable {
    private static final String SHOP_ID_KEY = "shop_id";
    final DataBindingComponent component = new FragmentDataBindingComponent(this);
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    @Inject
    NavigationController controller;
    @Inject
    ViewModelProvider.Factory factory;
    AutoClearedValue<ShopListAdapter> adapter;
    Menu menu;
    AutoClearedValue<FragmentShopInfoBinding> binding;
    private ShopInfoFragmentViewModel viewModel;

    public static ShopInfoFragment create(long shopId) {
        final Bundle args = new Bundle();
        args.putLong(SHOP_ID_KEY, shopId);
        final ShopInfoFragment fragment = new ShopInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final FragmentShopInfoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_shop_info, container, false, component);
        this.binding = new AutoClearedValue<>(this, binding);

        return binding.getRoot();
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        viewModel = ViewModelProviders.of(this, factory).get(ShopInfoFragmentViewModel.class);
        showUi();
        binding.get().setRetryCallback(this::showUi);
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_shop_info_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_call_to_shop) {
            clickCall();
            return true;
        } else if (id == R.id.action_show_on_map) {
            controller.navigateToMaps(binding.get().getShop());
            return true;
        } else if (id == R.id.action_chat_to_shop) {
            clickChat();
            return true;
        } /*else if (id == R.id.action_favourite) {
            addShotToFavourite(item);
            return true;
        }*/
        return false;
    }

/*
    private void addShotToFavourite(MenuItem item) {
        Shop shop = binding.get().getShop();
        shop.favourite = !shop.favourite;
        viewModel.saveShop(shop);

        if (shop.favourite) {
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }
*/

    private void clickCall() {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + binding.get().getShop().phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickChat() {
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=" +
                        binding.get().getShop().phone.replace("+", "")));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    final void showUi() {
        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.navigation_bar_item_shop);
        }
        viewModel.getShop(getArguments().getLong(SHOP_ID_KEY)).observe(this, observer -> {
            if (observer != null) {
                binding.get().setResource(observer);
                binding.get().setShop(observer.data);
                if (observer.data != null) {
                    setHasOptionsMenu(true);
                    initProductList();
                    if (actionBar != null) {
                        actionBar.setTitle(observer.data.name);
                    }/*
                    if (observer.data.favourite) {
                        menu.findItem(R.id.action_favourite).setIcon(R.drawable.ic_star_black_24dp);
                    }*/
                }
            }
        });
    }

    final void paginationList(long position) {
        LiveData<Resource<List<Product>>> productsData = loadProducts(15, position);

        if (productsData.hasObservers()) {
            productsData.removeObservers(this);
        }

        productsData.observe(this, observer -> {
            if (observer != null && observer.status == Status.SUCCESS) {
                adapter.get().add(observer.data);
                binding.get().executePendingBindings();
            }
        });
    }

    final void updateProducts() {
        LiveData<Resource<List<Product>>> productsData = loadProducts(20, 0);
        if (productsData.hasObservers()) {
            productsData.removeObservers(this);
        }

        productsData.observe(this, observer -> {
            if (observer != null) {
                binding.get().setResource(observer);
                adapter.get().replace(observer.data);
                binding.get().executePendingBindings();
            }
        });
    }

    final void initProductList() {
        final ShopListAdapter adapter =
                new ShopListAdapter(component, product -> controller.navigateToProduct(product));

        final RecyclerView recycler = binding.get().recyclerShopsList.recycler;
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recycler.setAdapter(adapter);
        this.adapter = new AutoClearedValue<>(this, adapter);

        updateProducts();
        addScrollListener(recycler);
    }

    final LiveData<Resource<List<Product>>> loadProducts(int limit, long offset) {
        return viewModel.loadProducts(binding.get().getShop(), limit, offset);
    }

    private void addScrollListener(final RecyclerView recycler) {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                final long lastPosition =
                        ((GridLayoutManager) recycler.getLayoutManager()).findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    paginationList(lastPosition + 1);
                }
            }
        });
    }
}