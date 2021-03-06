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
import com.lalaalal.droni.ui.LoginDialog;

public class AirFieldFragment extends Fragment {
    private static final String COMMAND_AIRFIELD = "AIRFIELD";
    private static final String SUB_COMMAND_GET = "GET";
    private static final String SUB_COMMAND_USE = "SET_USE";
    private static final String SUB_COMMAND_NOT_USE = "SET_NOT_USE";
    private static final String SUB_COMMAND_DJI_USE = "SET_DJI_USE";
    private static final String SUB_COMMAND_DJI_NOT_USE = "SET_DJI_NOT_USE";

    private View root;
    private View gwangNaruData;
    private View sinJeonData;

    private TableLayout fpvTableLayout;
    private RadioGroup airfieldRadioGroup;
    private RadioButton gwangNaruRadioButton;
    private RadioButton sinJeongRadioButton;
    private EditText setFpvBandEditText;
    private EditText setFpvChannelEditText;
    private Button setFpvButton;
    private Button cancelFpvButton;
    private Button useDjiDronButton;
    private TextView djiDroneUsingTextView;

    private LinearLayout airFieldData;

    private AirFieldData fieldData;

    private SharedPreferencesHandler sharedPreferencesHandler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_airfield, container, false);
        gwangNaruData = inflater.inflate(R.layout.gwangnaru, container, false);
        sinJeonData = inflater.inflate(R.layout.sinjeong, container, false);

        sharedPreferencesHandler = SharedPreferencesHandler.getSharedPreferences(getContext());
        if (!sharedPreferencesHandler.isLoggedIn()) {
            LoginDialog loginDialog = new LoginDialog();
            loginDialog.show(getActivity().getSupportFragmentManager(), "Login Dialog");
            Toast.makeText(getContext(), "먼저 로그인을 해 주세요", Toast.LENGTH_SHORT).show();
        }

        airFieldData = root.findViewById(R.id.airfield_info);

        fpvTableLayout = root.findViewById(R.id.fpv_table);
        airfieldRadioGroup = root.findViewById(R.id.airfield_rg);
        gwangNaruRadioButton = root.findViewById(R.id.gwangnaru_rb);
        sinJeongRadioButton = root.findViewById(R.id.sinjeong_rb);

        gwangNaruRadioButton.setChecked(true);

        djiDroneUsingTextView = root.findViewById(R.id.dji_use);

        setFpvBandEditText = root.findViewById(R.id.set_fpv_band_et);
        setFpvChannelEditText = root.findViewById(R.id.set_fpv_channel_et);
        setFpvButton = root.findViewById(R.id.set_fpv_btn);
        cancelFpvButton = root.findViewById(R.id.cancel_fpv_btn);
        useDjiDronButton = root.findViewById(R.id.use_dji_btn);

        initFpvTableLayout();
        setRadioButtonsOnClickListener();
        setFpvButtonOnClickListener();

        if (sharedPreferencesHandler.isFpvUsing()) {
            String band = Integer.toString(sharedPreferencesHandler.getFpvBand() + 1);
            String channel = Integer.toString(sharedPreferencesHandler.getFpvChannel() + 1);

            setFpvBandEditText.setText(band);
            setFpvBandEditText.setEnabled(false);
            setFpvChannelEditText.setText(channel);
            setFpvChannelEditText.setEnabled(false);
            setFpvButton.setEnabled(false);
        }
        if (sharedPreferencesHandler.isDjiUsing()) {
            useDjiDronButton.setEnabled(false);
        }

        String fieldName = getSelectedAirFieldName();
        loadFpvTable(fieldName);
        airFieldData.addView(gwangNaruData);

        return root;
    }

    private void setRadioButtonsOnClickListener() {
        gwangNaruRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFpvTable(gwangNaruRadioButton.getText().toString());
                airFieldData.removeAllViews();
                airFieldData.addView(gwangNaruData);
            }
        });
        sinJeongRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFpvTable(sinJeongRadioButton.getText().toString());
                airFieldData.removeAllViews();
                airFieldData.addView(sinJeonData);
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
                    sharedPreferencesHandler.setUsingFieldName(fieldData.getFieldName());
                    loadFpvTable(fieldData.getFieldName());
                    setFpvChannelEditText.setEnabled(false);
                    setFpvBandEditText.setEnabled(false);
                    setFpvButton.setEnabled(false);
                } catch (DroniException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        useDjiDronButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setDjiDrone(SUB_COMMAND_DJI_USE);
                    sharedPreferencesHandler.setUsingFieldName(fieldData.getFieldName());
                    sharedPreferencesHandler.useDji();
                    useDjiDronButton.setEnabled(false);
                    loadFpvTable(getSelectedAirFieldName());
                } catch (DroniException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelFpvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String s = sharedPreferencesHandler.getUsingFieldName();
                    if (!sharedPreferencesHandler.isFpvUsing() && !sharedPreferencesHandler.isDjiUsing())
                        throw new DroniException("사용 중인 대역폭이 없습니다");
                    if (!sharedPreferencesHandler.getUsingFieldName().equals(fieldData.getFieldName()))
                        throw new DroniException("다른 비행장 입니다");
                    if (!sharedPreferencesHandler.isLoggedIn())
                        throw new DroniException("로그인을 해 주세요");
                    if (sharedPreferencesHandler.isDjiUsing()) {
                        setDjiDrone(SUB_COMMAND_DJI_NOT_USE);
                        sharedPreferencesHandler.cancelDji();
                        useDjiDronButton.setEnabled(true);
                    } else {
                        int band = sharedPreferencesHandler.getFpvBand();
                        int channel = sharedPreferencesHandler.getFpvChannel();
                        setFpvStatusAt(band , channel, SUB_COMMAND_NOT_USE);
                        sharedPreferencesHandler.cancelFpv();
                        setFpvChannelEditText.setEnabled(true);
                        setFpvBandEditText.setEnabled(true);
                        setFpvButton.setEnabled(true);
                    }
                    loadFpvTable(fieldData.getFieldName());
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

    private void setDjiDrone(String setCommand) throws DroniException {
        try {
            String stringData = setCommand + ":" + fieldData.getFieldName();
            DroniRequest request = new DroniRequest(DroniRequest.TYPE_FILE, COMMAND_AIRFIELD, stringData);
            DroniClient droniClient = new DroniClient(request);
            Thread thread = new Thread(droniClient);
            thread.start();
            thread.join();

            DroniResponse response = droniClient.getResponse();
            if (response == null)
                throw new DroniException(DroniException.NO_RESPONSE);
            if (response.stringData.get(0).equals("false"))
                throw new DroniException("실패..");
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

    private String getSelectedAirFieldName() {
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
            String s = "DJI : " + fieldData.getDjiDrone();
            if (fieldData.getDjiDrone() > 0) {
                djiDroneUsingTextView.setTextColor(Color.RED);
            } else {
                djiDroneUsingTextView.setTextColor(Color.GREEN);
            }

            djiDroneUsingTextView.setText(s);
        } catch (DroniException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
