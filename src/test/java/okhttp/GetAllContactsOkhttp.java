package okhttp;

import com.google.gson.Gson;

import dto.ContactDto;
import dto.ErrorDto;
import dto.GetAllContactsDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttp {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";


    @Test
    public void getAllContactsSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(),200);

        GetAllContactsDto contactsDto = gson.fromJson(response.body().string(),GetAllContactsDto.class);

        List<ContactDto> list = contactsDto.getContacts();
        for(ContactDto contact:list){
            System.out.println(contact.getEmail());
            System.out.println(contact.getId());
            System.out.println("********");
        }


    }
    @Test
    public void getAllContactsUnauthorized() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization","fdfyfrhgiu")
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(),401);

        ErrorDto error  = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(error.getMessage());
        Assert.assertTrue(error.getMessage().toString().contains( "JWT strings must contain"));
        Assert.assertEquals(response.code(), 401);
        Assert.assertFalse(response.isSuccessful());
    }
}
