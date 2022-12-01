package com.technical.philliptrade.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technical.philliptrade.R;
import com.technical.philliptrade.databinding.ItemListStockBinding;
import com.technical.philliptrade.view_model.MainViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListStockAdapter extends RecyclerView.Adapter<ListStockAdapter.ViewHolder> {

    private ArrayList< String > listTrade = new ArrayList<>();
    private MainViewModel mainViewModel;
    private final ListStockCallback listStockCallback;

    public ListStockAdapter(@NonNull MainViewModel mainViewModel, @NonNull ListStockCallback listStockCallback) {
        this.mainViewModel = mainViewModel;
        this.listStockCallback = listStockCallback;
    }

    public void setData(ArrayList< String > items) {
        listTrade.clear();
        listTrade.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListStockBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_stock, parent, false);
        return new ListStockAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setStockName(listTrade.get(position));
        holder.binding.imgCross.setVisibility(View.GONE);
        holder.binding.consData.setOnClickListener(view -> {
            listStockCallback.onClickStock(listTrade.get(position), holder.binding.consData);
        });
    }

    @Override
    public int getItemCount() {
        return listTrade.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListStockBinding binding;

        public ViewHolder(ItemListStockBinding itemListStockBinding) {
            super(itemListStockBinding.getRoot());
            this.binding = itemListStockBinding;
        }

        public void bind(Object obj) {
            binding.executePendingBindings();
        }
    }

    public interface ListStockCallback{
        void onClickStock (String data, View view);
    }
}
