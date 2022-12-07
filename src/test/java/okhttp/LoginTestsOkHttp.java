package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Provider;
import java.io.IOException;
//https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor

public class LoginTestsOkHttp {
    Gson gson = Provider.getInstance().getGson();
    public static final MediaType JSON = Provider.getInstance().getJSON();
    OkHttpClient client = Provider.getInstance().getClient();


    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345$").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDto responseDto =
                gson.fromJson(response.body().string(), AuthResponseDto.class);
        String token = responseDto.getToken();
        System.out.println(token);
    }

    @Test
    public void loginWrongEmail() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noagmail.com").password("Nnoa12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message, "Login or Password incorrect");
        Assert.assertEquals(response.code(), 401);
        Assert.assertFalse(response.isSuccessful());
    }

    @Test
    public void loginWrongPasswordLength() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message, "Login or Password incorrect");


        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

    }

    @Test
    public void loginWrongPasswordWithoutSpecialSymbol() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message.toString(), "Login or Password incorrect");


        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

    }

    @Test
    public void loginUnregisteredAuth() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("kfc@gmail.com").password("Kfc54321$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message.toString(), "Login or Password incorrect");

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

    }
}
