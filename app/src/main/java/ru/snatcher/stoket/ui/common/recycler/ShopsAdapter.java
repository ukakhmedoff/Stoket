package ru.snatcher.stoket.ui.common.recycler;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Shop;
import ru.snatcher.stoket.databinding.ShopItemBinding;

public class ShopsAdapter extends DataBoundListAdapter<Shop, ShopItemBinding> {

    final ShopClickCallback callback;
    private final android.databinding.DataBindingComponent component;

    public ShopsAdapter(final DataBindingComponent component, final ShopClickCallback callback) {
        this.component = component;
        this.callback = callback;
    }

    @Override
    protected final ShopItemBinding createBinding(final ViewGroup parent) {
        final ShopItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shop_item,
                        parent, false, component);

        binding.getRoot().setOnClickListener(v -> {
            Shop shop = binding.getShop();
            if (shop != null && callback != null) {
                callback.onClick(shop);
            }
        });
        return binding;
    }

    @Override
    protected final void bind(final ShopItemBinding binding, final Shop item) {
        binding.setShop(item);
    }

    public interface ShopClickCallback {
        void onClick(final Shop shop);
    }
}