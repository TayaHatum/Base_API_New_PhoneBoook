package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDto;
import dto.ErrorDto;
import dto.GetAllContactsDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class GetAllContactsRest {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibm9hQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjMwNTc5LCJpYXQiOjE2NjkwMzA1Nzl9.tDqkeMr0Z5KeanvTCxIB2FCAXpc0rO-aZ0FcvLo9pzY";

    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }


    @Test
    public void getAllContactsSuccess() {
        GetAllContactsDto all = given()
                .header("Authorization", token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDto.class);
        List<ContactDto> contacts = all.getContacts();
        for (ContactDto contactDto : contacts) {
            System.out.println(contactDto.toString());
            System.out.println("*******");
        }
    }
        @Test
        public void getAllContactsWrongToken(){
            ErrorDto errorDto =given()
                    .header("Authorization","ttt")
                    .when()
                    .get("contacts")
                    .then()
                    .extract()
                    .response()
                    .as(ErrorDto.class);


            Assert.assertTrue(errorDto.getMessage().toString().contains("JWT strings must contain"));
            Assert.assertEquals(errorDto.getStatus(),401);



        }
    @Test
    public void getAllContactsWrongToken2(){
        given()
                .header("Authorization","ttt")
                .when()
                .get("contacts")
                .then()
                        .assertThat().statusCode(401)
                        .assertThat().body("message",containsString("JWT strings must contain"));

    }
}
