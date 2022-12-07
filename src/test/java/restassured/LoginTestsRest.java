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

public class LoginTestsRest {

    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void loginSuccess(){


        AuthResponseDto responseDto = given()
                .body(AuthRequestDto.builder()
                        .username("noa@gmail.com")
                        .password("Nnoa12345$").build())
                .contentType("application/json")
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }
    @Test
    public void loginWrongEmail(){
        ErrorDto errorDto =given()
                .body(AuthRequestDto.builder().
                        username("noagmail.com")
                        .password("Nnoa12345$").build())
                .contentType("application/json")
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");


    }
    @Test
    public void loginWrongEmailFormat(){

        given()
                .body(AuthRequestDto.builder().username("noagmail.com").password("Nnoa12345$").build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
                .assertThat().body("path",containsString("/v1/user/login/usernamepassword"));

    }

    @Test
    public void loginWrongPasswordFormat(){

        given()
                .body(AuthRequestDto.builder().username("noagmail.com").password("Nnoa12345$").build())
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
                .assertThat().body("path",containsString("/v1/user/login/usernamepassword"));

    }
}
