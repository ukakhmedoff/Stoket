package ru.snatcher.stoket.ui.fragments.main;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Status;
import ru.snatcher.stoket.databinding.FragmentMainBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.activity.main.MainActivity;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.ui.common.recycler.ProductsListAdapter;
import ru.snatcher.stoket.util.AutoClearedValue;

public class MainFragment extends LifecycleFragment implements Injectable {

    final DataBindingComponent bindingComponent = new FragmentDataBindingComponent(this);

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    AutoClearedValue<ProductsListAdapter> productsAdapter;

    AutoClearedValue<FragmentMainBinding> binding;

    int positionIndex;

    MainFragmentViewModel viewModel;

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        setHasOptionsMenu(true);
        final FragmentMainBinding mainBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_main, container, false, bindingComponent);
        this.binding = new AutoClearedValue<>(this, mainBinding);

        return mainBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory)
                .get(MainFragmentViewModel.class);

        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.navigation_bar_item_home);
        }

        initGenders();
        setCallbacks();
        initProductsRecycler();
    }

    private void initGenders() {
        Spinner genders = binding.get().genders;

        genders.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.genders, android.R.layout.simple_dropdown_item_1line));
        genders.setSelection(viewModel.getGender());
    }

    private void initProductsRecycler() {
        final ProductsListAdapter listAdapter = new ProductsListAdapter(bindingComponent,
                navigationController::navigateToProduct);
        final RecyclerView recycler = binding.get().products.recycler;
        recycler.setAdapter(listAdapter);

        productsAdapter = new AutoClearedValue<>(this, listAdapter);

        updateProducts();
        addScrollListener(recycler);
        initBrands();
        initTypes();
    }

    private void setCallbacks() {
        binding.get().filterApplyButton.setOnClickListener(v -> updateProducts());
        binding.get().filterCancelButton.setOnClickListener(v -> clearFilters());
        binding.get().setRetryCallback(this::updateProducts);
    }

    private void initBrands() {
        AutoCompleteTextView brands = binding.get().filterBrands;

        brands.setOnFocusChangeListener((view, b) ->
                binding.get().filterBrands.showDropDown());

        viewModel.loadBrands().observe(this, resource -> {
            if (resource != null && resource.data != null) {
                brands.setAdapter(new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, resource.data));
            }
        });
    }

    private void initTypes() {
        AutoCompleteTextView types = binding.get().filterTypes;

        types.setOnFocusChangeListener((view, b) ->
                types.showDropDown());

        viewModel.loadTypes().observe(this, resource -> {
            if (resource != null && resource.data != null) {
                types.setAdapter(new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, resource.data));
            }
        });
    }

    private void clearFilters() {
        binding.get().filterTypes.setText("");
        binding.get().filterPriceMaxEditText.setText("");
        binding.get().filterBrands.setText("");
        binding.get().filterPriceMinEditText.setText("");
        binding.get().genders.setSelection(viewModel.getGender());
        updateProducts();
    }

    private void addScrollListener(RecyclerView recycler) {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recycler.getLayoutManager();
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int lastPosition = layoutManager.findLastVisibleItemPosition();
                if (productsAdapter.get() != null && lastPosition == productsAdapter.get().getItemCount() - 2) {
                    paginationList(lastPosition + 2);
                }
            }
        });
    }

    void updateProducts() {
        final LiveData<Resource<List<Product>>> products = loadProducts();

        products.observe(this, observer -> {
            if (observer != null) {
                binding.get().setResource(observer);
                productsAdapter.get().replace(observer.data);

                if (positionIndex > 0 && observer.status == Status.SUCCESS) {
                    binding.get().products.recycler.getLayoutManager().scrollToPosition(positionIndex + 1);
                    positionIndex = 0;
                }
                binding.get().executePendingBindings();
            }
        });

    }

    void paginationList(int position) {
        final LiveData<Resource<List<Product>>> products = loadProductsPagination(position);

        products.observe(this, observer -> {
            if (observer != null && observer.status == Status.SUCCESS) {
                productsAdapter.get().add(observer.data);
                binding.get().executePendingBindings();
            }
        });
    }

    LiveData<Resource<List<Product>>> loadProductsPagination(long offset) {
        final EditText priceMin = binding.get().filterPriceMinEditText;
        final EditText priceMax = binding.get().filterPriceMaxEditText;
        final AutoCompleteTextView brand = binding.get().filterBrands;
        final AutoCompleteTextView type = binding.get().filterTypes;
        final Spinner gender = binding.get().genders;

        binding.get().drawerLayout.closeDrawer(GravityCompat.END);

        return viewModel.getProducts(9,
                offset,
                brand.getText().toString().trim(),
                type.getText().toString().trim(),
                priceMin.getText().toString().trim(),
                priceMax.getText().toString().trim(),
                gender.getSelectedItemPosition(),
                true);
    }

    LiveData<Resource<List<Product>>> loadProducts() {
        final EditText priceMin = binding.get().filterPriceMinEditText;
        final EditText priceMax = binding.get().filterPriceMaxEditText;
        final AutoCompleteTextView brand = binding.get().filterBrands;
        final AutoCompleteTextView type = binding.get().filterTypes;
        final Spinner gender = binding.get().genders;

        binding.get().drawerLayout.closeDrawer(GravityCompat.END);

        return viewModel.getProducts(10 + positionIndex,
                0L,
                brand.getText().toString().trim(),
                type.getText().toString().trim(),
                priceMin.getText().toString().trim(),
                priceMax.getText().toString().trim(),
                gender.getSelectedItemPosition(),
                false);
    }

    @Override
    public void onStop() {
        final RecyclerView recycler = binding.get().products.recycler;

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recycler.getLayoutManager();
        positionIndex = layoutManager.findFirstCompletelyVisibleItemPosition();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_filters_open) {
            final DrawerLayout drawerLayout = binding.get().drawerLayout;
            if (drawerLayout.isDrawerOpen(Gravity.END)) {
                drawerLayout.closeDrawer(Gravity.END);
            } else {
                drawerLayout.openDrawer(Gravity.END);
            }
        }
        return true;
    }
}