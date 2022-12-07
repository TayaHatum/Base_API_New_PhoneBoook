package okhttp;

import com.google.gson.Gson;

import dto.ContactDto;
import dto.ContactRequestDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Provider;

import java.io.IOException;

public class DeleteContactByIdOkhttp {

    String id;
    public static final MediaType JSON = Provider.getInstance().getJSON();
    Gson gson = Provider.getInstance().getGson();
    OkHttpClient client = Provider.getInstance().getClient();
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";


    @BeforeMethod
    public void preCondition() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya@mail.com")
                .address("Haifa")
                .phone("8888000000")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactRequestDto contact = gson.fromJson(response.body().string(),ContactRequestDto.class);
        String message = contact.getMessage();//Contact was added! ID: 3ff457c8-f115-4c3f-848d-2d0e59adee65
        System.out.println(message);
        String  [] all =  message.split(": ");
        id = all[1];

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {

       Request request = new Request.Builder()
               .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
               .delete()
               .addHeader("Authorization",token)
               .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactRequestDto responseDeleteByIdDto = gson.fromJson(response.body().string(),ContactRequestDto.class);
        Assert.assertEquals(responseDeleteByIdDto.getMessage(),"Contact was deleted!");

    }

    @Test   /// bug!!!!!!!!!! return code 400  but have return 404
    public void deleteContactNotFound() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/11")
                .delete()
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        System.out.println(response.code());
        Assert.assertEquals(response.code(),404);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("not found in your contacts!"));

    }
}
