package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class RegistrationTestsRest {

    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void registrationSuccess(){
        int i = (int)(System.currentTimeMillis()/1000)%3600;

        AuthResponseDto responseDto = given()
                .body(AuthRequestDto.builder()
                        .username("neta"+i+"@gmail.com")
                        .password("Nn12345$").build())
                .contentType("application/json")
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }

    @Test
    public void registrationDuplicateUserNegative(){


        ErrorDto responseDto = given()
                .body(AuthRequestDto.builder()
                        .username("noa@gmail.com")
                        .password("Nnoa12345$").build())
                .contentType("application/json")
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .extract()
                .response()
                .as(ErrorDto.class);
        System.out.println(responseDto.getMessage());
        Assert.assertEquals(responseDto.getMessage(),"User already exists");

    }
    @Test
    public void registrationWrongEmail(){
        given()
                .body(AuthRequestDto.builder().
                        username("noagmail.com")
                        .password("Nnoa12345$").build())
                .contentType("application/json")
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username",containsString("must be a well-formed email address"));

    }


    @Test
    public void registrationWrongPasswordFormatWithoutSpecialSymbol(){

        given()
                .body(AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa12345").build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password",containsString("Can contain special characters [@$#^&*!]"));


    }
    @Test
    public void registrationWrongPasswordFormatLength(){

        given()
                .body(AuthRequestDto.builder().username("noa@gmail.com").password("Nnoa1").build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password",containsString("At least 8 characters"));


    }
}
