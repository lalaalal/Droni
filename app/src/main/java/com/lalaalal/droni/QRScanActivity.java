package com.lalaalal.droni;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lalaalal.droni.ui.user.UserPageActivity;

public class QRScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "취소됨", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "ID : " + result.getContents(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                intent.putExtra("ID", result.getContents());
                finish();
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
