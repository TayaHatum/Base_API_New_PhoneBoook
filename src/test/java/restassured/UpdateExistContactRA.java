package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class UpdateExistContactRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";


    ContactDto contact= ContactDto.builder()
            .name("Dolly")
            .lastName("Dow")
            .email("dolly@mail.com")
            .phone("77766684321")
            .address("Haifa")
            .description("friend").build();
    String id ;

    @BeforeMethod
    public void init(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
        String message  =given()
                .header("Authorization",token)
                .body(contact)
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
    public void updateExistContactSuccess(){
        String name= contact.getName();
        contact.setId(id);
        contact.setName("wwwww");

        given()
                .body(contact)
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"))
                ;


    }
}
