package utils;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
//https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor

public class Provider {
    private static final Provider instance = new Provider();

    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    private Gson gson = new Gson();
    private HttpLoggingInterceptor logging;
    private OkHttpClient client;

    private Provider() {
        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public static Provider getInstance() {
        return instance;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Gson getGson() {
        return gson;
    }

    public MediaType getJSON(){
        return JSON;
    }
}
