package com.technical.philliptrade.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technical.philliptrade.R;
import com.technical.philliptrade.databinding.ItemRunningStockMarketBinding;
import com.technical.philliptrade.model.ModelTraderMarket;
import com.technical.philliptrade.view_model.MainViewModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList< ModelTraderMarket > listTrade = new ArrayList<>();
    private MainViewModel mainViewModel;

    public MainAdapter(@NonNull MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    public void setData(ArrayList< ModelTraderMarket > items) {
        listTrade.clear();
        listTrade.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRunningStockMarketBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_running_stock_market, parent, false);
        return new MainAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelTraderMarket modelTraderMarket = listTrade.get(position);
        holder.binding.setModelTrade(modelTraderMarket);

        //set time
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date newDate = format.parse(modelTraderMarket.getTime());
            format = new SimpleDateFormat("HH:mm:ss");
            String time = format.format(newDate);
            holder.binding.setTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set ACT
        if (modelTraderMarket.getAct().equals("SD")) holder.binding.tvAct.setTextColor(Color.RED);
        else holder.binding.tvAct.setTextColor(Color.GREEN);

        //set CHG
        if (Double.parseDouble(modelTraderMarket.getChg()) > 0.0) {
            holder.binding.tvChg.setTextColor(Color.GREEN);
            holder.binding.tvPrice.setTextColor(Color.GREEN);
            holder.binding.tvStockName.setTextColor(Color.GREEN);
            holder.binding.setChg("+"+new DecimalFormat("##.##").format(Double.parseDouble(modelTraderMarket.getChg())));
        }
        else if (Double.parseDouble(modelTraderMarket.getChg()) == 0.0){
            holder.binding.tvChg.setTextColor(Color.YELLOW);
            holder.binding.tvPrice.setTextColor(Color.YELLOW);
            holder.binding.tvStockName.setTextColor(Color.YELLOW);
            holder.binding.setChg(new DecimalFormat("##.##").format(Double.parseDouble(modelTraderMarket.getChg())));
        }else {
            holder.binding.tvChg.setTextColor(Color.RED);
            holder.binding.tvPrice.setTextColor(Color.RED);
            holder.binding.tvStockName.setTextColor(Color.RED);
            holder.binding.setChg(new DecimalFormat("##.##").format(Double.parseDouble(modelTraderMarket.getChg())));
        }

        holder.bind(modelTraderMarket);
    }

    @Override
    public int getItemCount() {
        return listTrade.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRunningStockMarketBinding binding;

        public ViewHolder(ItemRunningStockMarketBinding itemRunningStockMarketBinding) {
            super(itemRunningStockMarketBinding.getRoot());
            this.binding = itemRunningStockMarketBinding;
        }

        public void bind(Object obj) {
            binding.executePendingBindings();
        }
    }
}
