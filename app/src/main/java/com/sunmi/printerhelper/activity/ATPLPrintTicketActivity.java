package com.sunmi.printerhelper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.printerhelper.BaseApp;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;


import java.io.IOException;

/**
 * Created by Ishant on 2019/8/13.
 */

public class ATPLPrintTicketActivity extends AppCompatActivity {

    TextView gasType_textView, LitresAmount_textView, priceOfOneLiter_textView, totalLitresPrice_textView, taxPrice_textView;
    TextView taxPerValue_textView, totalBillValue_textView;
    TextView companyAddress, companyTaxNo, companyUnifiedNo;
    RadioGroup radioGroup_gasType;
    EditText litresAmount_editText;
    Button calculateBtn, printBtn;
    ImageView settingsBtn, qrCodeIV;
    float totalPriceOfLitres, taxValue, totalBillCost;
    // Text Settings
    byte resource = 0x00;
    String charset = "utf-8";
    float fontSize = 24;

    // QR Code Settings
    int printSize = 6;
    int errorLevel = 3;

    BaseApp baseApp;

    // Ticket Data
    String ticketHeader = "فاتورة ضريبية مبسطة";
    String ticketHashCodeForQRCode = "www.finlo.in/q=415321";
    String url = "www.finlo.in";
    String ticketReceiptNumber = "B1987472913";
    String ticketSeparator = "--------------------------------";
    String ticketDetails = "Vehicle# PB10AL2937\nIn Time:17:00\nOut Time: 19:00";
    String ticketTerms="Terms\nFine of Rs.100 If Ticket Lost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);

        radioGroup_gasType = findViewById(R.id.radio_group_gasType);
        gasType_textView = findViewById(R.id.gas_type);
        litresAmount_editText = findViewById(R.id.litres_amount_editText);
        calculateBtn = findViewById(R.id.calculate_btn);
        LitresAmount_textView = findViewById(R.id.litres_amount_textView);
        settingsBtn = findViewById(R.id.settings_btn);
        priceOfOneLiter_textView = findViewById(R.id.priceOfOneLiter);
        totalLitresPrice_textView = findViewById(R.id.liters_price);
        taxPrice_textView = findViewById(R.id.tax_price_textView);
        taxPerValue_textView = findViewById(R.id.taxPerValue_textView);
        totalBillValue_textView = findViewById(R.id.total_price);
        printBtn = findViewById(R.id.printBtn);

        GoToFuelPricesActivity();
        RadioButtonSettings();
        CalculateBtnSettings();

        AidlUtil.getInstance().initPrinter();

        baseApp = (BaseApp)getApplication();


        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTicket();
            }
        });
    }

    // Implement printByAidl and printByBluetooth as per ticket

    public void printTicket(){
        // Check if Connected to AIDL Service and print directly
        if (baseApp.isAidl()) {
            printByAidl();
            Log.i("PrintTicketActivity", ">> Printed with AIDL");
        } else { // Else print from BT. In case BT is also non functional -> Restart the App
            printByBluetooth();
            Log.i("PrintTicketActivity", ">> Printed with BlueTooth");
        }
    }


    void printByAidl(){

        AidlUtil.getInstance().sendRawData(ESCUtil.alignRight());


        AidlUtil.getInstance().printText(ticketHeader, fontSize, true, false);
        AidlUtil.getInstance().lineWrap(2);

        AidlUtil.getInstance().printQr(ticketHashCodeForQRCode, printSize, errorLevel);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().printText(url, fontSize, true, false);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().printText(ticketReceiptNumber, fontSize, false, false);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().printText(ticketSeparator, fontSize, false, false);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft());
        AidlUtil.getInstance().printText(ticketDetails, fontSize, false, false);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().sendRawData(ESCUtil.alignCenter());
        AidlUtil.getInstance().printText(ticketSeparator, fontSize, false, false);
        AidlUtil.getInstance().lineWrap(1);

        AidlUtil.getInstance().sendRawData(ESCUtil.alignCenter());
        AidlUtil.getInstance().printText(ticketTerms, fontSize, false, false);
        AidlUtil.getInstance().lineWrap(3);
    }

    private void printByBluetooth() {

        try {

            BluetoothUtil.sendData(ESCUtil.boldOn());
            BluetoothUtil.sendData(ESCUtil.alignCenter());

            BluetoothUtil.sendData(ESCUtil.singleByteOff());
            BluetoothUtil.sendData(ESCUtil.setCodeSystem(resource));

            BluetoothUtil.sendData(ticketHeader.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(2));

            BluetoothUtil.sendData(ESCUtil.getPrintQRCode(ticketHashCodeForQRCode, printSize, errorLevel));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ESCUtil.boldOff());

            BluetoothUtil.sendData(url.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ticketReceiptNumber.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ticketSeparator.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ESCUtil.alignLeft());

            BluetoothUtil.sendData(ticketDetails.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ticketSeparator.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(1));

            BluetoothUtil.sendData(ticketTerms.getBytes(charset));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));

        } catch (IOException e) {
            Toast.makeText(this, ">> Re-Start the App !! Some Printing Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("PrintTicketActivity", ">> Exception while Printing: "+e.getMessage());
            e.printStackTrace();
        }
    }



    /////////////////////////////////////////////////////////////////////////////
    ////////////                    CALCULATOR FUNCTIONS        /////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void CalculateBtnSettings() {
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(litresAmount_editText.getText().toString().equals("") || gasType_textView.getText().toString().equals("-")){
                    Toast.makeText(getApplicationContext(), "Please enter amount of litres & gas type", Toast.LENGTH_SHORT).show();
                } else {
                    LitresAmount_textView.setText(litresAmount_editText.getText());
                    LitresCalculations();
                    TaxesCalculations();
                    TotalBillCalculations();




                }

            }
        });
    }

    public void GoToFuelPricesActivity() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Fuel_Prices.class);
                startActivity(i);
            }
        });
    }

    public void RadioButtonSettings() {
        radioGroup_gasType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.gas_diesel:
                        gasType_textView.setText("ديزل");
                        Fuel_Prices.pref_diesel = getApplicationContext().getSharedPreferences("prefDiesel", MODE_PRIVATE);
                        String priceOfDiesel = Fuel_Prices.pref_diesel.getString("diesel", "No data found!");
                        priceOfOneLiter_textView.setText(priceOfDiesel);
                        UpdateTaxPercentage();
                        break;
                    case R.id.gas_98:
                        gasType_textView.setText("98");
                        Fuel_Prices.pref_98 = getApplicationContext().getSharedPreferences("pref98", MODE_PRIVATE);
                        String priceOf98 = Fuel_Prices.pref_98.getString("98", "No data found!");
                        priceOfOneLiter_textView.setText(priceOf98);
                        UpdateTaxPercentage();
                        break;
                    case R.id.gas_91:
                        gasType_textView.setText("91");
                        Fuel_Prices.pref_91 = getApplicationContext().getSharedPreferences("pref91", MODE_PRIVATE);
                        String priceOf91 = Fuel_Prices.pref_91.getString("91", "No data found!");
                        priceOfOneLiter_textView.setText(priceOf91);
                        UpdateTaxPercentage();
                        break;
                }

            }
        });
    }

    public void UpdateTaxPercentage() {
        Fuel_Prices.pref_taxPer = getApplicationContext().getSharedPreferences("prefTax", MODE_PRIVATE);
        String taxPer = Fuel_Prices.pref_taxPer.getString("tax", "No data");
        taxPerValue_textView.setText(taxPer);
    }



    public void LitresCalculations() {
        //liters calculations
        String litAmount = LitresAmount_textView.getText().toString();
        float litAmountFloat = Float.parseFloat(litAmount);

        String priceLiter = priceOfOneLiter_textView.getText().toString();
        float priceLiterFloat = Float.parseFloat(priceLiter);

        totalPriceOfLitres = litAmountFloat * priceLiterFloat;

        totalLitresPrice_textView.setText(String.valueOf(totalPriceOfLitres));
    }

    public void TaxesCalculations() {
        //taxes calculation
        String taxPerString = taxPerValue_textView.getText().toString();
        float taxPerFloat = Float.parseFloat(taxPerString);

        taxValue = (taxPerFloat/100) * totalPriceOfLitres;

        taxPrice_textView.setText(String.valueOf(taxValue));
    }

    public void TotalBillCalculations() {
        //total bill price calculation
        totalBillCost = taxValue + totalPriceOfLitres;
        totalBillValue_textView.setText(String.valueOf(totalBillCost));
    }



}
