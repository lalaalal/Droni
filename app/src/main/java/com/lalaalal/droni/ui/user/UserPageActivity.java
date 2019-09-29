package com.lalaalal.droni.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lalaalal.droni.*;
import com.lalaalal.droni.client.DroniClient;
import com.lalaalal.droni.client.DroniRequest;
import com.lalaalal.droni.client.DroniResponse;

public class UserPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        TextView userIdTextView = findViewById(R.id.user_id_tv);

        Intent intentFromQr = getIntent();
        String id = intentFromQr.getStringExtra("ID");
        try {
            if (id == null) {
                String userId = getUserId();
                userIdTextView.setText("내 계정");
                initUserDataLayout(userId);
            } else {
                userIdTextView.setText(id);
                initUserDataLayout(id);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Button qrScanButton = findViewById(R.id.scan_qr_btn);
        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPageActivity.this, QRScanActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setQrImage(String id) {
        ImageView qrImageButton = findViewById(R.id.user_qr_btn);

        try{
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImageButton.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserId() throws LoginException {
        SharedPreferencesHandler loginSessionHandler = SharedPreferencesHandler.getSharedPreferences(this);

        return loginSessionHandler.getSessionId();
    }

    private void initUserDataLayout(String id) throws DroniException {
        setQrImage(id);
        UserData userData = getUserData(id);
        TextView userDataTextView = findViewById(R.id.user_data_tv);
        userDataTextView.setText(userData.toString());
    }

    private UserData getUserData(String id) throws DroniException {
        try {
            DroniRequest request = new DroniRequest("TEXT", "USER_DATA", id);
            DroniClient droniClient = new DroniClient(request);

            Thread thread = new Thread(droniClient);
            thread.start();
            thread.join();

            DroniResponse response = droniClient.getResponse();
            return new UserData(response.stringData);
        } catch (InterruptedException e) {
            throw new DroniException(DroniException.NO_RESPONSE);
        }
    }
}
