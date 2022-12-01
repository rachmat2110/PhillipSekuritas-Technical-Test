package com.technical.philliptrade.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.technical.philliptrade.R;
import com.technical.philliptrade.adapter.ListStockAdapter;
import com.technical.philliptrade.adapter.ListStockAddAdapter;
import com.technical.philliptrade.adapter.MainAdapter;
import com.technical.philliptrade.da_sqlite.DA_M_trade_market;
import com.technical.philliptrade.databinding.ActivityMainBinding;
import com.technical.philliptrade.model.ModelTraderMarket;
import com.technical.philliptrade.view_model.MainViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ListStockAdapter.ListStockCallback{

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private MainAdapter adapter;
    private ListStockAdapter listStockAdapter;
    private ListStockAddAdapter listStockAddAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setViewModelTrace(mainViewModel);
        initView();
        iniObserver();
    }

    private void initView(){
        if (adapter == null){
            adapter = new MainAdapter(mainViewModel);
            binding.rvData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rvData.setHasFixedSize(true);
            binding.rvData.setAdapter(adapter);
        }

        binding.consStockFilter.setOnClickListener(view -> {
            showBottonFilter(view);
        });

        binding.switchFilter.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                if (mainViewModel.stockListAdd.size() > 0){
                    mainViewModel.getListDataWithFilter();
                    mainViewModel.isFilter.setValue(true);
                }
            } else {
                mainViewModel.isFilter.setValue(false);
            }
        });
    }

    private void iniObserver(){

        mainViewModel.listAddStockMarket.observe(this, list ->{
            if (list.size() > 0) binding.tvStockFilter.setText("Stock Filter ("+mainViewModel.stockListAdd.size()+")");
            else binding.tvStockFilter.setText("Stock Filter");
        });

        mainViewModel.isFilter.observe(this, isTrue ->{
            binding.switchFilter.setChecked(isTrue);
            if (isTrue){
                mainViewModel.stopTimer();
                if (mainViewModel.stockListAdd.size() == 0) binding.switchFilter.setChecked(false);
                mainViewModel.listStockTradeWithFilter.observe(this, listData ->{
                    if (listData.size() > 0)  adapter.setData(listData);
                });
            }else {
                mainViewModel.startTrack();

                mainViewModel.listStockTrade.observe(this, listData ->{
                    adapter.setData(listData);
                });
            }
        });
    }

    private void showBottonFilter(View view){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                view.getContext(), R.style.BottomSheetDialogTheme
        );
        final View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.bottom_sheet_filter, (ConstraintLayout)view.findViewById(R.id.consBottomSheet));
        MaterialButton btnAddStock = view1.findViewById(R.id.btnAddStock);
        MaterialButton btnApplyStock = view1.findViewById(R.id.btnApplyStock);
        RecyclerView rvData = view1.findViewById(R.id.rvData);

        btnAddStock.setOnClickListener(view2 -> {
            bottomSheetDialog.cancel();
            showListNameOfStock(view2);
        });

        btnApplyStock.setOnClickListener(view3 -> {
            mainViewModel.isFilter.setValue(true);
            bottomSheetDialog.cancel();
        });

        mainViewModel.listAddStockMarket.observe(this, list ->{
            listStockAddAdapter = new ListStockAddAdapter(mainViewModel);
            rvData.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            rvData.setHasFixedSize(true);
            rvData.setAdapter(listStockAddAdapter);
            listStockAddAdapter.setData(list);
        });

        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
    }

    private void showListNameOfStock(View view){
        mainViewModel.getListNameOfStock();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                view.getContext(), R.style.BottomSheetDialogTheme
        );

        final View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.bottom_sheet_list_stocks, (ConstraintLayout)view.findViewById(R.id.consBottomSheet));
        RecyclerView rvListStock = view1.findViewById(R.id.rvListStock);
        ImageView imgCross = view1.findViewById(R.id.imgCross);

        imgCross.setOnClickListener(view2 -> {
            bottomSheetDialog.cancel();
        });

        mainViewModel.listNameOfStock.observe(this, list ->{
            listStockAdapter = new ListStockAdapter(mainViewModel, this);
            rvListStock.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvListStock.setHasFixedSize(true);
            rvListStock.setAdapter(listStockAdapter);
            listStockAdapter.setData(list);
        });

        mainViewModel.isFilter.observe(this, isTrue ->{
            if (isTrue && bottomSheetDialog!=null){
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
    }

    @Override
    public void onClickStock(String data, View view) {
        if (!mainViewModel.stockListAdd.contains(data)){
            mainViewModel.stockListAdd.add(data);
            mainViewModel.listAddStockMarket.postValue(mainViewModel.stockListAdd);
        }
        Log.d("TAG","data = "+mainViewModel.stockListAdd);
        showBottonFilter(view);
    }
}