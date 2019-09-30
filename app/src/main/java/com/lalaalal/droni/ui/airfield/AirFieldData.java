package com.lalaalal.droni.ui.airfield;

import java.util.ArrayList;

public class AirFieldData {
    static final int CHANNEL_NUM = 8;
    static final int BAND_NUM = 5;
    static final int DJI_DRONE_NUM_INDEX = 40;

    private String fieldName;
    private boolean[][] fpvTable;
    private int djiDrone;

    AirFieldData(String fieldName, ArrayList<String> fpvStringData) {
        this.fieldName = fieldName;
        fpvTable = new boolean[BAND_NUM][CHANNEL_NUM];

        for (int band = 0; band < BAND_NUM; band++) {
            for (int channel = 0; channel < CHANNEL_NUM; channel++) {
                boolean fpv = Boolean.parseBoolean(fpvStringData.get(CHANNEL_NUM * band + channel));
                fpvTable[band][channel] = fpv;
            }
        }

        djiDrone = Integer.parseInt(fpvStringData.get(DJI_DRONE_NUM_INDEX));
    }

    String getFieldName() {
        return fieldName;
    }

    void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    boolean getStatusAt(int band, int channel) throws IndexOutOfBoundsException {
        if (!inRange(band, channel))
            throw new IndexOutOfBoundsException();

        return fpvTable[band][channel];
    }

    int getDjiDrone() {
        return djiDrone;
    }

    static boolean inRange(int band, int channel) {
        return (band >= 0 && band < BAND_NUM) && (channel >= 0 && channel < CHANNEL_NUM);
    }
}
