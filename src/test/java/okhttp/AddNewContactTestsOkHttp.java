package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactRequestDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;
import utils.Provider;

import java.io.IOException;

public class AddNewContactTestsOkHttp  implements Constants {
    public static final MediaType JSON = Provider.getInstance().getJSON();
    Gson gson = Provider.getInstance().getGson();
    OkHttpClient client = Provider.getInstance().getClient();
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";

    @Test
    public void  addNewContactSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis()/1000);
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya"+i+"@mail.com")
                .address("Haifa")
                .phone("8888"+i)
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        ContactRequestDto contact = gson.fromJson(response.body().string(), ContactRequestDto.class);
        System.out.println(contact.getMessage());

       Assert.assertTrue(contact.getMessage().contains("Contact was added!"));
       Assert.assertTrue(contact.getMessage().contains("ID"));
// Contact was added! ID: 49a7d3f0-a9c2-4297-bc88-fc8ea626a6d5
    }
    @Test
    public void  addNewContactWrongName() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .lastName("Wolf")
                .email("jenny@mail.com")
                .address("TV")
                .phone("9876543210")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));

    }
    @Test
    public void  addNewContactWrongLastName() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Jenny")
                .email("jenny@mail.com")
                .address("TV")
                .phone("9876543210")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("lastName=must not be blank"));

    }
    @Test
    public void  addNewContactWrongPhone() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .address("TV")
                .phone("987")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("Phone number must contain only digits! And length min 10, max 15!"));

    }
    @Test
    public void  addNewContactWrongEmail() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jennymail.com")
                .address("TV")
                .phone("9870070077")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("email=must be a well-formed email address"));

    }

    @Test
    public void  addNewContactWrongAddress() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .phone("9870070077")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("address=must not be blank"));

    }
    @Test
    public void  addNewContactUnauthorized() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .phone("9870070077")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization","hhyyyyt")
                .build();

        Response response = client.newCall(request).execute();

        ErrorDto error  = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(error.getMessage());
        Assert.assertTrue(error.getMessage().toString().contains( "JWT strings must contain"));
        Assert.assertEquals(response.code(), 401);
        Assert.assertFalse(response.isSuccessful());

    }
}
