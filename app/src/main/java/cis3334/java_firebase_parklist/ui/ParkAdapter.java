package cis3334.java_firebase_parklist.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import cis3334.java_firebase_parklist.R; // Ensure this is correctly imported
import cis3334.java_firebase_parklist.data.model.Park;

public class ParkAdapter extends ListAdapter<Park, ParkAdapter.ParkViewHolder> {

    private OnParkClickListener listener;

    public interface OnParkClickListener {
        void onParkClick(Park park);
    }

    public ParkAdapter(OnParkClickListener listener) {
        super(PARK_DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Park> PARK_DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Park>() {
        @Override
        public boolean areItemsTheSame(@NonNull Park oldItem, @NonNull Park newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Park oldItem, @NonNull Park newItem) {
            // For simplicity, we can just compare names and addresses.
            // For a more robust comparison, compare all fields.
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getAddress().equals(newItem.getAddress());
        }
    };

    @NonNull
    @Override
    public ParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_park, parent, false);
        return new ParkViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkViewHolder holder, int position) {
        Park currentPark = getItem(position);
        holder.bind(currentPark);
    }

    static class ParkViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewParkName;
        private TextView textViewParkAddress;
        private Park currentPark;

        public ParkViewHolder(@NonNull View itemView, OnParkClickListener listener) {
            super(itemView);
            textViewParkName = itemView.findViewById(R.id.textViewParkName);
            textViewParkAddress = itemView.findViewById(R.id.textViewParkAddress);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentPark != null) {
                    listener.onParkClick(currentPark);
                }
            });
        }

        public void bind(Park park) {
            currentPark = park;
            textViewParkName.setText(park.getName());
            textViewParkAddress.setText(park.getAddress());
        }
    }
}
