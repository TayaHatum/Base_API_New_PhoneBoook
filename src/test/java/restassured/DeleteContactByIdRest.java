package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import dto.ContactRequestDto;
import dto.ResponseDeleteByIdDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class DeleteContactByIdRest {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";

    String  id;
    @BeforeMethod
    public void init(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";

        ContactDto contactDto= ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@gmail.com")
                .phone("4445556667")
                .address("Tel Aviv")
                .description("friend").build();

        String message  =given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message");
        System.out.println(message);
        String  [] all =  message.split(": ");
        id = all[1];
    }

    @Test
    public void deleteContactByIdSuccess(){

        ContactRequestDto deleteResponseDto = given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(ContactRequestDto.class);
        Assert.assertEquals(deleteResponseDto.getMessage(),"Contact was deleted!");
    }


    @Test
    public void deleteContactByIdSuccess2(){

        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was deleted!"));

    }
}
