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

public class ListStockAddAdapter extends RecyclerView.Adapter<ListStockAddAdapter.ViewHolder>{

    private ArrayList< String > listTrade = new ArrayList<>();
    private MainViewModel mainViewModel;

    public ListStockAddAdapter(@NonNull MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
//        this.listStockCallback = listStockCallback;
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
        return new ListStockAddAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setStockName(listTrade.get(position));
        holder.binding.imgCross.setOnClickListener(view -> {
            if (mainViewModel.stockListAdd.contains(listTrade.get(position))){
                mainViewModel.stockListAdd.remove(listTrade.get(position));
                mainViewModel.listAddStockMarket.postValue(mainViewModel.stockListAdd);
            }
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
}
