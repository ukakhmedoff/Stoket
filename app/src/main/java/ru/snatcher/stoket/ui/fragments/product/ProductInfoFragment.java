package ru.snatcher.stoket.ui.fragments.product;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.databinding.FragmentProductInfoBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.activity.main.MainActivity;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.util.AutoClearedValue;

public class ProductInfoFragment extends LifecycleFragment implements Injectable {

    private static final String PRODUCT_ID_KEY = "product_id";

    final DataBindingComponent component = new FragmentDataBindingComponent(this);

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    NavigationController controller;

    AutoClearedValue<FragmentProductInfoBinding> binding;

    private ProductInfoFragmentViewModel viewModel;

    public static ProductInfoFragment create(long id) {
        Bundle args = new Bundle();
        args.putLong(PRODUCT_ID_KEY, id);

        ProductInfoFragment fragment = new ProductInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final FragmentProductInfoBinding inflate = DataBindingUtil.inflate(inflater,
                R.layout.fragment_product_info, container, false, component);

        this.binding = new AutoClearedValue<>(this, inflate);
        return inflate.getRoot();
    }

    @Override
    public final void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(ProductInfoFragmentViewModel.class);
        initUi();

        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.product);
        }
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_product_info_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_chat_to_shop) {
            clickChat();
            return true;
        } else if (id == R.id.action_shop_info) {
            controller.navigateToShopInfo(binding.get().getProduct().shop.id);
            return true;
        } else if (id == R.id.action_call_to_shop) {
            clickCall();
            return true;
        }
        return false;
    }


    private void clickCall() {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + binding.get().getProduct().shop.phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickChat() {
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=" +
                        binding.get().getProduct().shop.phone.replace("+", "")));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initUi() {
        viewModel.getProduct(getArguments().getLong(PRODUCT_ID_KEY)).observe(this,
                resource -> {
                    binding.get().setResource(resource);

                    if (resource != null && resource.data != null) {
                        setHasOptionsMenu(true);
                        binding.get().setProduct(resource.data);

                        binding.get().addToFavorite.setOnClickListener(v -> {
                            resource.data.isBasket = !resource.data.isBasket;
                            viewModel.saveProduct(resource.data);
                        });
                    }
                });
    }

    @Override
    public final LifecycleRegistry getLifecycle() {
        return registry;
    }
}