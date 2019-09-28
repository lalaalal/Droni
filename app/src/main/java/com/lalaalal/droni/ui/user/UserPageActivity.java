package com.lalaalal.droni.ui.user;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lalaalal.droni.LoginException;
import com.lalaalal.droni.LoginSessionHandler;
import com.lalaalal.droni.R;
import com.lalaalal.droni.client.DroniClient;
import com.lalaalal.droni.client.DroniRequest;
import com.lalaalal.droni.client.DroniResponse;

import java.util.ArrayList;

public class UserPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        setQrImage();
        initUserDataLayout();
    }

    private void setQrImage() {
        ImageView qrImageButton = findViewById(R.id.user_qr_btn);

        try{
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(getUserId(), BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImageButton.setImageBitmap(bitmap);
        } catch (LoginException e) {
            // Can't happen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserId() throws LoginException {
        LoginSessionHandler loginSessionHandler = LoginSessionHandler.getLoginSession(this);

        return loginSessionHandler.getSessionId();
    }

    private void initUserDataLayout() {
        try {
            UserData userData = getUserData();

            TextView userDataTextView = findViewById(R.id.user_data_tv);
            userDataTextView.setText(userData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UserData getUserData() throws Exception {
        DroniRequest request = new DroniRequest("TEXT", "USER_DATA", getUserId());
        DroniClient droniClient = new DroniClient(request);

        Thread thread = new Thread(droniClient);
        thread.start();
        thread.join();

        DroniResponse response = droniClient.getResponse();
        return new UserData(response.stringData);
    }
}
