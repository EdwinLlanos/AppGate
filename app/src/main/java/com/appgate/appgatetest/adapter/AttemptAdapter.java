package com.appgate.appgatetest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.databinding.LayoutAttemptItemBinding;
import com.appgate.authentication.domain.model.AttemptModel;
import java.util.List;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.AttemptHolder> {
    private final List<AttemptModel> mData;

    public AttemptAdapter(List<AttemptModel> data) {
        mData = data;
    }

    @NonNull
    @Override
    public AttemptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutAttemptItemBinding itemBinding = LayoutAttemptItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AttemptHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(AttemptHolder holder, int position) {
        AttemptModel item = mData.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class AttemptHolder extends RecyclerView.ViewHolder {
        private static final int NUMBER_ZERO = 0;
        private final LayoutAttemptItemBinding itemBinding;

        public AttemptHolder(LayoutAttemptItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(AttemptModel item) {
            View view = itemBinding.getRoot();
            itemBinding.timeZoneTxv.setText(item.getTimeZone());
            itemBinding.currentLocalTimeTxv.setText(item.getCurrentLocalTime());
            itemBinding.statusTxv.setText(view.getContext().getString(getStatus(item.getStatus())));
            itemBinding.clContainer.setBackgroundColor(ContextCompat.getColor(view.getContext(), getBackgroundColor(item.getStatus())));
        }

        private int getBackgroundColor(int status) {
            int background = R.color.blue;
            if (status == NUMBER_ZERO) {
                background = R.color.black;
            }
            return background;
        }

        private int getStatus(int status) {
            int statusTuResource = R.string.message_status_success;
            if (status == NUMBER_ZERO) {
                statusTuResource = R.string.message_status_failure;
            }
            return statusTuResource;
        }
    }
}
