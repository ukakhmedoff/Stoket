package ru.snatcher.stoket.ui.common.recycler;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.databinding.ProductImageShopBinding;

public class ShopListAdapter extends DataBoundListAdapter<Product, ProductImageShopBinding> {

    final ProductClickCallback callback;
    private final DataBindingComponent component;

    public ShopListAdapter(final DataBindingComponent component, final ProductClickCallback callback) {
        this.component = component;
        this.callback = callback;
    }

    @Override
    protected final ProductImageShopBinding createBinding(final ViewGroup parent) {
        final ProductImageShopBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.product_image_shop, parent,
                        false, component);

        binding.getRoot().setOnClickListener(v -> {
            Product product = binding.getProduct();
            if (product != null && callback != null) {
                callback.onClick(product);
            }
        });

        return binding;
    }

    @Override
    protected final void bind(final ProductImageShopBinding binding, final Product item) {
        binding.setProduct(item);
    }

    public interface ProductClickCallback {
        void onClick(Product pProduct);
    }
}