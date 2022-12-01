package com.technical.philliptrade.view_model;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.technical.philliptrade.da_sqlite.DA_M_trade_market;
import com.technical.philliptrade.model.ModelTraderMarket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private DA_M_trade_market da_m_trade_market;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    public Application application;
    public ArrayList<String> stockListAdd = new ArrayList<>();
    public ArrayList<ArrayList<ModelTraderMarket>> stockListAddWithFilter = new ArrayList<>();
    public ArrayList<ModelTraderMarket> stockListAddWithFilterWithSortir = new ArrayList<>();
    public ArrayList<ModelTraderMarket> getAllData = new ArrayList<>();

    //live data
    public MutableLiveData<ArrayList<ModelTraderMarket>> listStockTrade = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelTraderMarket>> listStockTradeWithFilter = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> listNameOfStock = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> listAddStockMarket = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFilter = new MutableLiveData<>(false);

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void startTrack(){
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerDisplay(), 0, 2500);
        }
    }

    public void stopTimer(){
        if (listAddStockMarket.getValue() != null && listAddStockMarket.getValue().size() > 0 && mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
    }

    class TimerDisplay extends TimerTask {

        @Override
        public void run() {

            da_m_trade_market = new DA_M_trade_market(application);

            mHandler.post(() -> {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                ModelTraderMarket modelTraderMarket = new ModelTraderMarket();
                ArrayList<ModelTraderMarket> arrayList = new ArrayList<>();
                try {

                    //generate PRICE random
                    int min = 50;
                    int max = 10000;
                    int randomPrice = (int) Math.floor(Math.random()*(max - min+1)+min);

                    //generate CHG percentage
                    double minChg = -3;
                    double maxChg = 3;
                    Random random = new Random();
                    double randomChg = minChg + (maxChg - minChg) * random.nextDouble();

                    //generate VOL random
                    int minVol = 1;
                    int maxVol = 1000;
                    int randomVol = (int) Math.floor(Math.random()*(maxVol - minVol+1)+minVol);

                    //generate ACT random
                    String[] valuesAct = new String[]{"BU","SD"};
                    int minRangeAct = 0;
                    int maxRangAct = 1;
                    int randomRangeAct = (int) Math.floor(Math.random()*(maxRangAct - minRangeAct+1)+minRangeAct);
                    String valuesActStore = valuesAct[randomRangeAct];

                    //generate Name Stock value
                    String[] valuesNameStock = new String[]{"AALI","ABBA","BAPA","BBCA","COCO","DWGL","DIGI","WEGE","KRAS","SICO","BBNI","FREN"};
                    int minRangeStockName = 0;
                    int maxRangeStockName = valuesNameStock.length - 1;
                    int randomRangeStockName = (int) Math.floor(Math.random()*(maxRangeStockName - minRangeStockName+1)+minRangeStockName);
                    String valuesActStockName = valuesNameStock[randomRangeStockName];

                    //generate Date
                    Date mDate = dateFormat.parse(dateFormat.format(cal.getTime()));
                    long time = mDate.getTime() - 120000;

                    modelTraderMarket.setStock_name(valuesActStockName);
                    modelTraderMarket.setTime(dateFormat.format(cal.getTime()));
                    modelTraderMarket.setTimestamp(mDate.getTime());
                    modelTraderMarket.setPrice(String.valueOf(randomPrice));
                    modelTraderMarket.setChg(String.valueOf(randomChg));
                    modelTraderMarket.setVol(String.valueOf(randomVol));
                    modelTraderMarket.setAct(valuesActStore);
                    arrayList.add(modelTraderMarket);

                    da_m_trade_market.insert_or_replace(arrayList);
                    da_m_trade_market.deleteRowbove50(time);
                    if (da_m_trade_market.getAllTrade().size() > 0){
                        listStockTrade.setValue(da_m_trade_market.getAllTrade());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void getListNameOfStock(){
        if (da_m_trade_market.getListNameOfStock().size() > 0){
            listNameOfStock.setValue(da_m_trade_market.getListNameOfStock());
        }
    }

    public void getListDataWithFilter(){
        if (listAddStockMarket.getValue() != null && listAddStockMarket.getValue().size() > 0){
            stockListAddWithFilter.clear();
            for (int i=0; i<=listAddStockMarket.getValue().size() - 1; i++){
                stockListAddWithFilter.add(da_m_trade_market.getAllTradeWithFilter(listAddStockMarket.getValue().get(i)));
                if (stockListAddWithFilter.contains(da_m_trade_market.getAllTradeWithFilter(listAddStockMarket.getValue().get(i)))) {
                    stockListAddWithFilter.remove(da_m_trade_market.getAllTradeWithFilter(listAddStockMarket.getValue().get(i)));
                }
            }
            Log.d("TAG","data = "+stockListAddWithFilter);
            stockListAddWithFilterWithSortir.clear();
            for (int j=0; j<=stockListAddWithFilter.size() - 1; j++){
                for (int k = 0; k<=stockListAddWithFilter.get(j).size() - 1; k++){
                    stockListAddWithFilterWithSortir.add(stockListAddWithFilter.get(j).get(k));
                }
            }
            Log.d("TAG","data = "+stockListAddWithFilterWithSortir);
            if (stockListAddWithFilterWithSortir.size() == 0) Toast.makeText(application, "tidak ada data pada filter tersebut", Toast.LENGTH_SHORT).show();
            else listStockTradeWithFilter.setValue(stockListAddWithFilterWithSortir);
        }
    }
}
