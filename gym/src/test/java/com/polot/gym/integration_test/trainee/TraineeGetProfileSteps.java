package com.polot.gym.integration_test.trainee;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.response.TraineeProfileResponse;
import com.polot.gym.service.impl.JwtService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TraineeGetProfileSteps {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtService jwtService;
    private String jwtToken;
    private ResponseEntity<TraineeProfileResponse> responseGetProfile;
    private String username = "john123.doe";
    private String firstName = "john123";
    private String lastName = "doe";
    private LocalDate dob = LocalDate.of(2023, 11, 22);
    private String address = "address";

    @Given("an authenticated trainee")
    public void givenUserIsAuthenticatedAsTrainee() {
        jwtToken = jwtService.generateToken(User.builder().username(username).role(Role.TRAINEE).build());
    }

    @When("the client sends a GET request to {string}")
    public void whenClientSendsGetProfileRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        responseGetProfile = restTemplate.exchange(url, HttpMethod.GET, httpEntity, TraineeProfileResponse.class);
        assertThat(responseGetProfile.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("the response body should contain the trainee's profile details")
    public void thenProfileDetailsShouldNotNull() {
        assertThat(responseGetProfile.getBody().getFirstName()).isEqualTo(firstName);
        assertThat(responseGetProfile.getBody().getLastName()).isEqualTo(lastName);
        assertThat(responseGetProfile.getBody().getDob()).isEqualTo(dob);
    }

    @When("the client sends a GET request to {string} without token")
    public void theClientSendsAGETRequestToWithoutToken(String url) {
        responseGetProfile = restTemplate.getForEntity(url, TraineeProfileResponse.class);
        System.out.println(responseGetProfile.getBody());
    }

    @Then("get profile response status code should be {int}")
    public void thenResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertThat(responseGetProfile.getStatusCode().value()).isEqualTo(expectedStatusCode);
    }
}
