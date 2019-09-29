package com.lalaalal.droni;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lalaalal.droni.client.DroniClient;
import com.lalaalal.droni.client.DroniRequest;
import com.lalaalal.droni.client.DroniResponse;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SignUpActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private EditText nameEditText;
    private EditText careerEditText;
    private EditText phoneEditText;
    private EditText droneEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idEditText = findViewById(R.id.sign_up_id_et);
        pwEditText = findViewById(R.id.sign_up_pw_et);
        nameEditText = findViewById(R.id.sign_up_name_et);
        careerEditText = findViewById(R.id.sign_up_career_et);
        phoneEditText = findViewById(R.id.sign_up_phone_et);
        droneEditText = findViewById(R.id.sign_up_drone_et);

        Button signUpButton = findViewById(R.id.sign_up_btn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signUp();
                } catch (DroniException e) {
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    finish();
                }
            }
        });
    }

    private boolean isEditTextsEmpty() {
        return idEditText.getText().length() == 0 ||
                pwEditText.getText().length() == 0 ||
                nameEditText.getText().length() == 0 ||
                careerEditText.getText().length() == 0 ||
                phoneEditText.getText().length() == 0 ||
                droneEditText.getText().length() == 0;
    }

    private String getUserData() {
        return idEditText.getText().toString() + ":"
                + MD5Hash(pwEditText.getText().toString()) + ":"
                + nameEditText.getText().toString() + ":"
                + careerEditText.getText().toString() + ":"
                + phoneEditText.getText().toString() + ":"
                + droneEditText.getText().toString() + ":";
    }

    private String MD5Hash(String s) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());

            StringBuilder result = new StringBuilder(new BigInteger(1, m.digest()).toString(16));
            while (result.length() < 32) {
                result.insert(0, "0");
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void signUp() throws DroniException {
        try {
            if (isEditTextsEmpty())
                throw new DroniException("모두 채워주세요");

            DroniRequest request = new DroniRequest("TEXT", "SIGN_UP", getUserData());
            DroniClient client = new DroniClient(request);
            Thread thread = new Thread(client);
            thread.start();
            thread.join();

            DroniResponse response = client.getResponse();
            if (response == null)
                throw new DroniException(DroniException.NO_RESPONSE);

            Toast.makeText(this, response.stringData.get(0), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            throw new DroniException(DroniException.CONNECTION_FAILED);
        }
    }
}
