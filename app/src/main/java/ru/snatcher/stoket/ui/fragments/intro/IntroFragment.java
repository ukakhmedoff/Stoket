package ru.snatcher.stoket.ui.fragments.intro;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.binding.FragmentDataBindingComponent;
import ru.snatcher.stoket.databinding.FragmentIntroBinding;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.common.NavigationController;
import ru.snatcher.stoket.util.AutoClearedValue;

public class IntroFragment extends LifecycleFragment implements Injectable {
    final android.databinding.DataBindingComponent bindingComponent = new FragmentDataBindingComponent(this);
    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;
    @Inject
    NavigationController navigationController;
    private IntroFragmentViewModel viewModel;
    private AutoClearedValue<FragmentIntroBinding> binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentIntroBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_intro, container, false, bindingComponent);
        this.binding = new AutoClearedValue<>(this, binding);
        getActivity().setTitle(R.string.navigation_bar_item_shopping_basket);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders
                .of(this, viewModelProviderFactory)
                .get(IntroFragmentViewModel.class);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.get().btnMenStore.setOnClickListener(setGender(0));
        binding.get().btnWomenStore.setOnClickListener(setGender(1));
    }

    public View.OnClickListener setGender(int gender) {
        return view -> {
            viewModel.setGender(gender);
            navigationController.onBackCommandClick();
        };
    }
}