package ru.snatcher.stoket.ui.common.recycler;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class DataBoundListAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    @Nullable
    private List<T> items = new ArrayList<>();

    private int dataVersion = 0;
    private int startVersion = 0;

    @Override
    public final DataBoundViewHolder<V> onCreateViewHolder(final ViewGroup parent,
                                                           final int viewType) {
        final V binding = createBinding(parent);
        return new DataBoundViewHolder<>(binding);
    }

    protected abstract V createBinding(final ViewGroup parent);

    @Override
    public final void onBindViewHolder(final DataBoundViewHolder<V> holder, final int position) {
        //noinspection ConstantConditions
        bind(holder.binding, items.get(position));
        holder.binding.executePendingBindings();
    }

    public final void add(List<T> add) {
        if (startVersion == dataVersion && checkSize(add) && checkItems(items)) {
            dataVersion++;
            int itemsSize = items.size();
            items.addAll(add);
            notifyItemInserted(itemsSize);
        } else if (startVersion != dataVersion && checkItems(add)) {
            startVersion = dataVersion;
        }
    }

    public void replace(List<T> data) {
        items = data;
        startVersion = dataVersion;
        notifyDataSetChanged();
    }

    private boolean checkSize(List<T> add) {
        return checkItems(items) && checkItems(add) && items.size() > add.size();
    }

    private boolean checkItems(List<T> list) {
        return list != null && !list.isEmpty();
    }

    protected abstract void bind(V binding, T item);

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}