package com.jabibim.admin.func;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

public class GoogleCalendarServiceFactory {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "JABIBIM Calendar App";

    public static Calendar createCalendarService(String accessToken) {
        try {
            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    (req) -> req.getHeaders().setAuthorization("Bearer " + accessToken)
            ).setApplicationName(APPLICATION_NAME).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JABIBIM Calendar 서비스를 생성하는데 실패했습니다.", e);
        }
    }
}
