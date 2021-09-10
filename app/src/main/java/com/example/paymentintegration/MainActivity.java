package com.example.paymentintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    EditText name, upiId, amount, note;
    TextView msg;
    Button pay;
    public static String payername, UpiID, sendamount, msgnote, status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = findViewById(R.id.name);
        upiId = findViewById(R.id.Upiid);
        amount = findViewById(R.id.amount);
        note = findViewById(R.id.note);
        msg = findViewById(R.id.status);
        pay = findViewById(R.id.Pay);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payername = name.getText().toString();
                UpiID = upiId.getText().toString();
                sendamount = amount.getText().toString();
                msgnote = msg.getText().toString();

                if (!payername.equals("") && !UpiID.equals("") && !msgnote.equals("") && !sendamount.equals("")) {

                    uri = getUpiPaymentUri(payername, UpiID, msgnote, sendamount);
                    payWithGpay(GPAY_PACKAGE_NAME);
                }

            }
        });


    }


    private static Uri getUpiPaymentUri(String name, String upiId, String note, String amount) {

        return new Uri.Builder().scheme("upi").authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

    }

    private void payWithGpay(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(packageName);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (data != null) {
            status = data.getStringExtra("status");

            if (RESULT_OK == requestCode && status.equals("success")) {

                Toast.makeText(MainActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "GPAY not Installed", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}




