package ru.snatcher.stoket.ui.common.recycler;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.databinding.ProductMainListItemBinding;

public class ProductsListAdapter extends DataBoundListAdapter<Product, ProductMainListItemBinding> {

    final ProductClickCallback callback;
    private final DataBindingComponent component;

    public ProductsListAdapter(final DataBindingComponent component, final ProductClickCallback callback) {
        this.component = component;
        this.callback = callback;
    }

    @Override
    protected final ProductMainListItemBinding createBinding(final ViewGroup parent) {
        final ProductMainListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.product_main_list_item, parent,
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
    protected final void bind(final ProductMainListItemBinding binding, final Product item) {
        binding.setProduct(item);
    }

    public interface ProductClickCallback {
        void onClick(Product product);
    }
}