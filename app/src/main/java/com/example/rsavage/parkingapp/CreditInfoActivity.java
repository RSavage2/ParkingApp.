package com.example.rsavage.parkingapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreditInfoActivity extends AppCompatActivity {

    private EditText et_cardNum, et_month, et_year, et_cvv, et_zip, et_country;
    private String cardNum,country,month, year, zip, cvv ;

    private Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_info);

        et_cardNum =  (EditText) findViewById(R.id.creditcard);
        et_month = (EditText) findViewById(R.id.month);
        et_year = (EditText) findViewById(R.id.year);
        et_cvv = (EditText) findViewById(R.id.cvv);
        et_zip = (EditText) findViewById(R.id.zip);
        et_country = (EditText) findViewById(R.id.country);

        pay = (Button) findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                makePayment();
            }
        });
    }

    public void makePayment(){
        initialize();
        if(!validate()){
            Toast.makeText(this, "Payment has failed", Toast.LENGTH_SHORT).show();
        }
        else{
            onPaymentSuccess();
        }

    }

    public void onPaymentSuccess(){
        //TODO what will go after the valid input

    }

    public boolean validate(){
        boolean valid = true;

        if(cardNum.isEmpty()||cardNum.length() < 16){
            et_cardNum.setError("Please enter valid card number");
            valid = false;
        }
        if(year.isEmpty()||year.length() < 2){
            et_year.setError("Please enter valid year");
            valid = false;
        }
        if(cvv.isEmpty()||cvv.length() < 3){
            et_cvv.setError("Please enter valid cvv");
            valid = false;
        }
        if(month.isEmpty()||month.length() < 2){
            et_month.setError("Please enter valid month");
            valid = false;
        }
        if(zip.isEmpty()||zip.length() < 5){
            et_zip.setError("Please enter valid zip code");
            valid = false;
        }
        if(country.isEmpty()||country.length() < 3){
            et_country.setError("Please enter country");
            valid = false;
        }

        return valid;
    }

    public void initialize(){
        cardNum  = et_cardNum.getText().toString().trim();
        country = et_country.getText().toString().trim();
        month = et_month.getText().toString().trim();
        year = et_year.getText().toString().trim();
        zip = et_zip.getText().toString().trim();
        cvv = et_cvv.getText().toString().trim();
    }

}
