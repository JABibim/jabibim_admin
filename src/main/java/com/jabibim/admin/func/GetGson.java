package com.jabibim.admin.func;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GetGson {
    public static Gson getGson() {
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();


        class LocalDateSerializer implements JsonSerializer<LocalDate> {
            private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(formatter.format(localDate));
            }
        }

        class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDate.parse(json.getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));
            }
        }
        class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
            private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(formatter.format(localDateTime));
            }
        }

        class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.KOREA));
            }
        }

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gson = gsonBuilder.setPrettyPrinting().create();

        /*
        LocalDateTime의 경우 Field명에 JsonFormat을 정의해줘야 gson에서도 알아먹는다.
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private final LocalDateTime localDateTime;
        위의 코드로 Test Code 작성 중 현재 시간을 받아올 때 second 단위 아래로 인해서 오류가 발생하는데 아래의 코드로 second 단위 아래를 제거할 수 있다.
        final LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
         */
        return gson;
    }
}
