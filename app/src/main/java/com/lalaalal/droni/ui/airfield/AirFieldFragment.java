package com.lalaalal.droni.ui.airfield;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lalaalal.droni.DroniException;
import com.lalaalal.droni.R;
import com.lalaalal.droni.SharedPreferencesHandler;
import com.lalaalal.droni.client.DroniClient;
import com.lalaalal.droni.client.DroniRequest;
import com.lalaalal.droni.client.DroniResponse;

public class AirFieldFragment extends Fragment {
    private static final String COMMAND_AIRFIELD = "AIRFIELD";
    private static final String SUB_COMMAND_GET = "GET";
    private static final String SUB_COMMAND_USE = "SET_USE";
    private static final String SUB_COMMAND_NOT_USE = "SET_NOT_USE";

    private TableLayout fpvTableLayout;
    private RadioGroup airfieldRadioGroup;
    private RadioButton gwangNaruRadioButton;
    private RadioButton sinJeongRadioButton;
    private EditText setFpvBandEditText;
    private EditText setFpvChannelEditText;
    private Button setFpvButton;
    private Button cancelFpvButton;

    private AirFieldData fieldData;

    private SharedPreferencesHandler sharedPreferencesHandler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_airfield, container, false);

        sharedPreferencesHandler = SharedPreferencesHandler.getSharedPreferences(getContext());
        if (!sharedPreferencesHandler.isLoggedIn()) {
            Toast.makeText(getContext(), "먼저 로그인을 해 주세요", Toast.LENGTH_SHORT).show();
        }

        fpvTableLayout = root.findViewById(R.id.fpv_table);
        airfieldRadioGroup = root.findViewById(R.id.airfield_rg);
        gwangNaruRadioButton = root.findViewById(R.id.gwangnaru_rb);
        sinJeongRadioButton = root.findViewById(R.id.sinjeong_rb);

        gwangNaruRadioButton.setChecked(true);

        setFpvBandEditText = root.findViewById(R.id.set_fpv_band_et);
        setFpvChannelEditText = root.findViewById(R.id.set_fpv_channel_et);
        setFpvButton = root.findViewById(R.id.set_fpv_btn);
        cancelFpvButton = root.findViewById(R.id.cancel_fpv_btn);

        initFpvTableLayout();
        setRadioButtonsOnClickListener();
        setFpvButtonOnClickListener();


        String fieldName = getSelectedAirFieldName(root);
        loadFpvTable(fieldName);

        return root;
    }

    private void setRadioButtonsOnClickListener() {
        gwangNaruRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFpvTable(gwangNaruRadioButton.getText().toString());
            }
        });
        sinJeongRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFpvTable(sinJeongRadioButton.getText().toString());
            }
        });
    }

    private void setFpvButtonOnClickListener() {

        setFpvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (setFpvBandEditText.getText().length() == 0 || setFpvChannelEditText.getText().length() == 0)
                        throw new DroniException("사용할 대역폭을 입력해 주세요");
                    if (!sharedPreferencesHandler.isLoggedIn())
                        throw new DroniException("로그인을 해 주세요");
                    int band = Integer.parseInt(setFpvBandEditText.getText().toString()) - 1;
                    int channel = Integer.parseInt(setFpvChannelEditText.getText().toString()) - 1;
                    if (!AirFieldData.inRange(band, channel))
                        throw new DroniException("범위를 확인해 주세요");
                    setFpvStatusAt(band , channel, SUB_COMMAND_USE);
                    sharedPreferencesHandler.useFpv(band, channel);
                    loadFpvTable(fieldData.getFieldName());
                    setFpvChannelEditText.setEnabled(false);
                    setFpvBandEditText.setEnabled(false);
                } catch (DroniException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelFpvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!sharedPreferencesHandler.isFpvUsing())
                        throw new DroniException("사용 중인 대역폭이 없습니다");
                    if (!sharedPreferencesHandler.isLoggedIn())
                        throw new DroniException("로그인을 해 주세요");
                    int band = sharedPreferencesHandler.getFpvBand();
                    int channel = sharedPreferencesHandler.getFpvChannel();
                    setFpvStatusAt(band , channel, SUB_COMMAND_NOT_USE);
                    sharedPreferencesHandler.cancelFpv();
                    loadFpvTable(fieldData.getFieldName());
                    setFpvChannelEditText.setEnabled(true);
                    setFpvBandEditText.setEnabled(true);
                } catch (DroniException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initFpvTableLayout() {
        for (int channel = 0; channel < AirFieldData.CHANNEL_NUM; channel++) {
            TableRow row = new TableRow(getContext());
             for (int band = 0; band < AirFieldData.BAND_NUM; band++) {
                TextView cell = new TextView(getContext());
                String s = "b" + (band + 1) + ":c" + (channel + 1);
                cell.setText(s);
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(cell);
            }
            fpvTableLayout.addView(row);
        }
    }

    private AirFieldData getAirFieldData(String fieldName) throws DroniException {
        try {
            DroniRequest request = new DroniRequest(DroniRequest.TYPE_FILE, COMMAND_AIRFIELD, SUB_COMMAND_GET + ":" + fieldName);
            DroniClient droniClient = new DroniClient(request);
            Thread thread = new Thread(droniClient);
            thread.start();
            thread.join();

            DroniResponse response = droniClient.getResponse();
            if (response == null)
                throw new DroniException(DroniException.NO_RESPONSE);
            return new AirFieldData(fieldName, response.stringData);
        } catch (InterruptedException e) {
            throw new DroniException(DroniException.CONNECTION_FAILED);
        }
    }

    private void setFpvStatusAt(int band, int channel, String setCommand) throws DroniException {
        if (setCommand.equals(SUB_COMMAND_USE) && fieldData.getStatusAt(band, channel))
            throw new DroniException("이미 사용중 입니다");

        try {
            String stringData = setCommand + ":" + fieldData.getFieldName() + ":" + band + ":" + channel;
            DroniRequest request = new DroniRequest(DroniRequest.TYPE_FILE, COMMAND_AIRFIELD, stringData);
            DroniClient droniClient = new DroniClient(request);
            Thread thread = new Thread(droniClient);
            thread.start();
            thread.join();

            DroniResponse response = droniClient.getResponse();
            if (response == null)
                throw new DroniException(DroniException.NO_RESPONSE);
            Toast.makeText(getContext(), response.stringData.get(0), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            throw new DroniException(DroniException.CONNECTION_FAILED);
        }
    }

    private String getSelectedAirFieldName(View root) {
        int selectedRadioButtonId = airfieldRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = root.findViewById(selectedRadioButtonId);

        return radioButton.getText().toString();
    }

    private void loadFpvTable(String fieldName) {
        try {
            fieldData = getAirFieldData(fieldName);

            for (int channel = 0; channel < AirFieldData.CHANNEL_NUM; channel++) {
                TableRow row = (TableRow) fpvTableLayout.getChildAt(channel);
                for (int band = 0; band < AirFieldData.BAND_NUM; band++) {
                    TextView cell = (TextView) row.getChildAt(band);
                    if (fieldData.getStatusAt(band, channel)) {
                        cell.setBackgroundColor(Color.BLACK);
                        cell.setTextColor(Color.WHITE);
                    } else {
                        cell.setBackgroundColor(Color.WHITE);
                        cell.setTextColor(Color.BLACK);
                    }
                }
            }
        } catch (DroniException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
