package com.sunmi.printerhelper.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sunmi.printerhelper.R;

public class Fuel_Prices extends AppCompatActivity {


    ImageView backBtn;
    Button savePricesBtn;
    public static SharedPreferences pref_diesel, pref_91, pref_98, pref_taxPer;
    EditText dieselPrice, price_91, price_98, taxPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_prices);

        savePricesBtn = findViewById(R.id.savePrice_Btn);
        backBtn = findViewById(R.id.back_btn);
        dieselPrice = findViewById(R.id.diesel_price);
        price_91 = findViewById(R.id.price_91);
        price_98 = findViewById(R.id.price_98);
        taxPercentage = findViewById(R.id.tax_percentage);

        pref_diesel = getApplicationContext().getSharedPreferences("prefDiesel", MODE_PRIVATE);
        pref_91 = getApplicationContext().getSharedPreferences("pref91", MODE_PRIVATE);
        pref_98 = getApplicationContext().getSharedPreferences("pref98", MODE_PRIVATE);
        pref_taxPer = getApplicationContext().getSharedPreferences("prefTax", MODE_PRIVATE);

        GoBackToCalculatorActivity();
        SaveBtnSettings();
        ShowFuelPrices();



    }

    public void GoBackToCalculatorActivity(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void SaveBtnSettings() {

        savePricesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stored_DieselPrice = dieselPrice.getText().toString().trim();
                String stored_91Price = price_91.getText().toString().trim();
                String stored_98Price = price_98.getText().toString().trim();
                String stored_taxPer = taxPercentage.getText().toString().trim();
                pref_diesel.edit().putString("diesel", stored_DieselPrice).apply();
                pref_91.edit().putString("91", stored_91Price).apply();
                pref_98.edit().putString("98", stored_98Price).apply();
                pref_taxPer.edit().putString("tax", stored_taxPer).apply();

                Toast.makeText(getApplicationContext(), "Saved Data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void ShowFuelPrices() {
        Fuel_Prices.pref_diesel = getApplicationContext().getSharedPreferences("prefDiesel", MODE_PRIVATE);
        String priceOfDiesel = Fuel_Prices.pref_diesel.getString("diesel", "");
        dieselPrice.setText(priceOfDiesel);

        Fuel_Prices.pref_91 = getApplicationContext().getSharedPreferences("pref91", MODE_PRIVATE);
        String priceOf91 = Fuel_Prices.pref_91.getString("91", "");
        price_91.setText(priceOf91);

        Fuel_Prices.pref_98 = getApplicationContext().getSharedPreferences("pref98", MODE_PRIVATE);
        String priceOf98 = Fuel_Prices.pref_98.getString("98", "");
        price_98.setText(priceOf98);

        Fuel_Prices.pref_taxPer = getApplicationContext().getSharedPreferences("prefTax", MODE_PRIVATE);
        String taxPercentageString = Fuel_Prices.pref_taxPer.getString("tax", "");
        taxPercentage.setText(taxPercentageString);
    }

}






















