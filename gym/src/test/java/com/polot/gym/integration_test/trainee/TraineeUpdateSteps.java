package com.polot.gym.integration_test.trainee;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.StatusRequest;
import com.polot.gym.payload.request.TraineeProfileUpdateRequest;
import com.polot.gym.payload.response.TraineeProfileResponse;
import com.polot.gym.service.impl.JwtService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TraineeUpdateSteps {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtService jwtService;
    private String jwtToken;
    private ResponseEntity<TraineeProfileResponse> response;
    private int responseCode;
    private String username = "john123.doe";
    private TraineeProfileUpdateRequest updateRequest;

    @Given("the user is authenticated as a trainee")
    public void givenUserIsAuthenticatedAsTrainee() {
        jwtToken = jwtService.generateToken(User.builder().username(username).role(Role.TRAINEE).build());
    }

    @Given("the trainee profile update request with the following details:")
    public void givenTraineeProfileUpdateRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> profileData = rows.get(0);

        updateRequest = new TraineeProfileUpdateRequest();
        updateRequest.setUsername(profileData.get("username"));
        updateRequest.setFirstName(profileData.get("firstName"));
        updateRequest.setLastName(profileData.get("lastName"));
        updateRequest.setAddress(profileData.get("address"));
        updateRequest.setIsActive(Boolean.valueOf(profileData.get("isActive")));
    }

    @When("the client sends a PUT request to {string}")
    public void whenClientSendsPutRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<TraineeProfileUpdateRequest> request = new HttpEntity<>(updateRequest, headers);

        response = restTemplate.exchange(url, HttpMethod.PUT, request, TraineeProfileResponse.class);
        responseCode = response.getStatusCode().value();
    }

    @Then("the response body should contain the updated trainee profile details:")
    public void thenResponseBodyShouldContainUpdatedTraineeProfileDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedProfileData = rows.get(0);

        TraineeProfileResponse profileResponse = response.getBody();
        assertThat(profileResponse).isNotNull();
        assertThat(profileResponse.getFirstName()).isEqualTo(expectedProfileData.get("firstName"));
        assertThat(profileResponse.getLastName()).isEqualTo(expectedProfileData.get("lastName"));
        assertThat(profileResponse.getAddress()).isEqualTo(expectedProfileData.get("address"));
    }


    @And("the trainee profile update request with the following details without isActive:")
    public void theTraineeProfileUpdateRequestWithTheFollowingDetailsWithoutIsActive(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> profileData = rows.get(0);

        updateRequest = new TraineeProfileUpdateRequest();
        updateRequest.setUsername(profileData.get("username"));
        updateRequest.setFirstName(profileData.get("firstName"));
        updateRequest.setLastName(profileData.get("lastName"));
        updateRequest.setAddress(profileData.get("address"));
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int code) {
        assertThat(responseCode).isEqualTo(code);
    }

    @When("the client sends a DELETE request to {string}")
    public void theClientSendsADELETERequestTo(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        responseCode = restTemplate.exchange(url + "?username=" + username, HttpMethod.DELETE, request, Void.class).getStatusCode().value();
    }

    @And("a user with username {string}")
    public void aUserWithUsername(String username) {
        this.username = username;
    }

    @And("the client sends a GET request to {string} with the following filters:")
    public void theClientSendsAGETRequestToWithTheFollowingFilters(String url, DataTable dataTable) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> profileData = rows.get(0);
        responseCode = restTemplate.exchange(url + "?username=" + profileData.get("username"), HttpMethod.GET, request, String.class).getStatusCode().value();
    }

    @When("the client sends a GET request to {string} without any filters")
    public void theClientSendsAGETRequestToWithoutAnyFilters(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        responseCode = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getStatusCode().value();
    }

    @When("the client sends a PATCH request to activate-deactivate {string}")
    public void theClientSendsAGETRequestToActivate(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<StatusRequest> request = new HttpEntity<>(new StatusRequest(data.get("username")),headers);

        responseCode = restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class).getStatusCode().value();
    }

}
