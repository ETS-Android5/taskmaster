package com.akkanben.taskmaster.utility;

import android.util.Log;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

import java.util.Date;

public class AnalyticsUtility {
    public static void analyticsLogTime(String eventName) {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name(eventName)
                .addProperty("name", eventName)
                .addProperty("time", Long.toString(new Date().getTime()))
                .build();
        Amplify.Analytics.recordEvent(event);
    }
}
