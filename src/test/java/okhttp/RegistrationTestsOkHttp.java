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
import java.util.Map;

public class RegistrationTestsOkHttp {

    Gson gson = Provider.getInstance().getGson();
    public static final MediaType JSON = Provider.getInstance().getJSON();
    OkHttpClient client = Provider.getInstance().getClient();

    @Test
    public void registrationSuccess() throws IOException {
        int i = (int)(System.currentTimeMillis()/1000)%3600;
        AuthRequestDto auth = AuthRequestDto.builder().username("noa"+i+"@gmail.com").password("Nnoa12345$").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        AuthResponseDto responseDto=
                gson.fromJson(response.body().string(),AuthResponseDto.class);
        String  token = responseDto.getToken();
        System.out.println(token);
    }

    @Test
    public void registrationDuplicateUserNegative() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345$").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);

        ErrorDto responseDto=gson.fromJson(response.body().string(),ErrorDto.class);
        Object  message = responseDto.getMessage();
        int status = responseDto.getStatus();
        Assert.assertEquals(message,"User already exists");
        Assert.assertEquals(status,409);

    }
    @Test
    public void registrationWrongEmail() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noagmail.com").password("Nnoa12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        System.out.println(message);
        Assert.assertTrue(message.toString().contains("must be a well-formed email address"));

        Assert.assertEquals(response.code(),400);
        Assert.assertFalse(response.isSuccessful());
    }

    @Test
    public void registrationWrongEmailAdv() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noagmail.com").password("Nnoa12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        System.out.println(message);
        if(!(message instanceof String)){
            Map<String,String> map = (Map<String, String>) message;
            map.forEach((k,v) -> System.out.println(k + " ----> " + v));
           System.out.println(map.get("username"));
            System.out.println(map.containsKey("username"));

        }

        Assert.assertEquals(response.code(),400);
        Assert.assertFalse(response.isSuccessful());
    }

    @Test
    public void registrationWrongPasswordLength() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertTrue(message.toString().contains("At least 8 characters"));

//At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

    }
    @Test
    public void registrationWrongPasswordWithoutSpecialSymbol() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto errorDto = gson.fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertTrue(message.toString().contains("Can contain special characters [@$#^&*!]"));
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

    }

}
