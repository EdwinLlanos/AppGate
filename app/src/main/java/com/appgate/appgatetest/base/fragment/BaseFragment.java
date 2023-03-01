package com.appgate.appgatetest.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.appgate.appgatetest.MainActivity;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment implements DefaultLifecycleObserver {

    private static final int NUMBER_ZERO = 0;
    ProgressBar progressBar;
    View rootLayout;

    public abstract void initUI();

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setRootLayout(View rootLayout) {
        this.rootLayout = rootLayout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            getActivity().getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        if (getActivity() != null) {
            getActivity().getLifecycle().removeObserver(this);
        }
        initUI();
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getContext();
    }

    public void showProgressBar(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showMessage(String message) {
        if (!message.isEmpty()) {
            showSnackbar(message);
        }
    }

    public void showMessage(Integer message) {
        if (message != NUMBER_ZERO) {
            showSnackbar(getResources().getString(message));
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
