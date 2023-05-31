package com.example.datingapp.common;

import android.content.Context;

import com.example.datingapp.R;

import java.util.Locale;

import javax.inject.Inject;

public class TextFormatter {

    private final Context context;

    @Inject
    public TextFormatter(Context context) {
        this.context = context;
    }

    public String formatDistanceUnits(Long distance) {
        StringBuilder formattedDistanceBuilder = new StringBuilder();
        if (distance >= 1000) {
            double distanceInKm = (double) distance / 1000;
            formattedDistanceBuilder
                    .append(String.format(Locale.US, "%.1f", distanceInKm))
                    .append(" ")
                    .append(context.getString(R.string.kilometers_unit));
        } else {
            formattedDistanceBuilder
                    .append(distance)
                    .append(" ")
                    .append(context.getString(R.string.meters_unit));
        }
        return formattedDistanceBuilder.toString();
    }
}
