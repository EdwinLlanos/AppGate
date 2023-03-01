package com.appgate.appgatetest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appgate.appgatetest.adapter.AttemptAdapter;
import com.appgate.appgatetest.base.fragment.BaseFragment;
import com.appgate.appgatetest.databinding.FragmentAttemptsBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.AttemptsViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import java.util.List;

public class AttemptFragment extends BaseFragment {

    private FragmentAttemptsBinding binding;
    private AttemptsViewModel viewModel;

    public static AttemptFragment newInstance() {
        return new AttemptFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAttemptsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void initUI() {
        initViewModel();
        initObservers();
        viewModel.getAttempts();
    }

    private void initObservers() {
        viewModel.getAttemptList().observe(this, this::showData);
    }

    private void initViewModel() {
        GetAttemptsUseCase getAttemptsUseCase = new Injector().createGetAttemptsUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(getAttemptsUseCase);
        viewModel = new ViewModelProvider(this, factory).get(AttemptsViewModel.class);
    }

    private void showData(List<AttemptModel> attemptModels) {
        if (!attemptModels.isEmpty()) {
            RecyclerView recyclerView = binding.recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            AttemptAdapter adapter = new AttemptAdapter(attemptModels);
            recyclerView.setAdapter(adapter);
        }
    }
}
