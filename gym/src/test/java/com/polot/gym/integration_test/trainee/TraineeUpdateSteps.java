package com.polot.gym.integration_test.trainee;

import com.polot.gym.client.report.ReportServiceClient;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.CreateTrainingRequest;
import com.polot.gym.payload.request.StatusRequest;
import com.polot.gym.payload.request.TraineeProfileUpdateRequest;
import com.polot.gym.payload.request.TrainerRegisterRequest;
import com.polot.gym.payload.response.TraineeProfileResponse;
import com.polot.gym.payload.response.TrainingReport;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.service.impl.JwtService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private List<TrainingResponse> trainingResponseList;
    @Autowired
    private ReportServiceClient reportServiceClient;
    private String trainerUsername;
    private String firstname;
    private String lastname;

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
        updateRequest.setDob(LocalDate.parse(profileData.get("dob")));
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
        ResponseEntity<List<TrainingResponse>> responseEntity = restTemplate.exchange(url + "?username=" + profileData.get("username"), HttpMethod.GET, request, new ParameterizedTypeReference<>() {
        });
        responseCode = responseEntity.getStatusCode().value();
        trainingResponseList = responseEntity.getBody();
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
        HttpEntity<StatusRequest> request = new HttpEntity<>(new StatusRequest(data.get("username")), headers);

        responseCode = restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class).getStatusCode().value();
    }

    @When("the client sends a PUT request to {string} without token")
    public void theClientSendsAPUTRequestToWithoutToken(String url) {
        response = restTemplate.exchange(url, HttpMethod.PUT, null, TraineeProfileResponse.class);
        responseCode = response.getStatusCode().value();
    }

    @And("the client sends a GET request to {string} with the following filters and without token:")
    public void theClientSendsAGETRequestToWithTheFollowingFiltersAndWithoutToken(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> profileData = rows.get(0);
        responseCode = restTemplate.exchange(url + "?username=" + profileData.get("username"), HttpMethod.GET, null, String.class).getStatusCode().value();
    }

    @Then("response should contain list of data")
    public void responseShouldContainListOfData() {
        TrainingResponse training = trainingResponseList.get(0);
        assertThat(training.getName()).isEqualTo("running");
        assertThat(training.getDate()).isEqualTo(LocalDate.of(2023, 12, 12));
        assertThat(training.getType().getName()).isEqualTo("type 1");
        assertThat(training.getTrainerName()).isEqualTo("test test");
    }

    @When("the client sends a PATCH request to activate-deactivate {string} without token")
    public void theClientSendsAPATCHRequestToActivateDeactivateWithoutToken(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        HttpEntity<StatusRequest> request = new HttpEntity<>(new StatusRequest(data.get("username")));

        responseCode = restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class).getStatusCode().value();
    }

    @When("the client sends a POST request to create training {string}")
    public void theClientSendsAPOSTRequestToCreateTraining(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<CreateTrainingRequest> request = new HttpEntity<>(new CreateTrainingRequest(data.get("traineeUsername"), data.get("trainerUsername"), data.get("trainingName"), LocalDate.parse(data.get("trainingDate")), data.get("trainingDuration") != null ? Integer.parseInt(data.get("trainingDuration")) : null), headers);

        responseCode = restTemplate.exchange(url, HttpMethod.POST, request, Void.class).getStatusCode().value();
    }


    @When("the client sends a POST request to create training {string} without token")
    public void theClientSendsAPOSTRequestToCreateTrainingWithoutToken(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CreateTrainingRequest> request = new HttpEntity<>(new CreateTrainingRequest(data.get("traineeUsername"), data.get("trainerUsername"), data.get("trainingName"), LocalDate.parse(data.get("trainingDate")), Integer.parseInt(data.get("trainingDuration"))), headers);

        responseCode = restTemplate.exchange(url, HttpMethod.POST, request, Void.class).getStatusCode().value();
    }

    @And("in report service this report should be added")
    public void inReportServiceThisReportShouldBeAdded() {
        List<TrainingReport> reportResponses = reportServiceClient.getAll("Bearer " + jwtToken);
        Optional<TrainingReport> report = reportResponses.stream().filter(trainingReport -> trainingReport.getTrainerUsername().equals("test.test")).findFirst();
        assertThat(report).isPresent();
        assertThat(report.get().getTrainerFirstname()).isEqualTo("test");
        assertThat(report.get().getTrainerLastname()).isEqualTo("test");
        assertThat(report.get().getYears()).containsKey(2023);
        assertThat(report.get().getYears().get(2023)).containsKey("DECEMBER");
    }

    @And("in report service this report should be added and trainingDuration must be {int}")
    public void inReportServiceThisReportShouldBeAddedAndTrainingDurationMustBe(int trainingDuration) {
        List<TrainingReport> reportResponses = reportServiceClient.getAll("Bearer " + jwtToken);
        Optional<TrainingReport> report = reportResponses.stream().filter(trainingReport -> trainingReport.getTrainerUsername().equals("test.test")).findFirst();
        assertThat(report).isPresent();
        assertThat(report.get().getTrainerFirstname()).isEqualTo("test");
        assertThat(report.get().getTrainerLastname()).isEqualTo("test");
        assertThat(report.get().getYears()).containsKey(2024);
        assertThat(report.get().getYears().get(2024)).containsKey("JANUARY");
        assertThat(report.get().getYears().get(2024).get("JANUARY")).isEqualTo(trainingDuration);
    }

    @And("in report service this report should be added for newtrainer")
    public void inReportServiceThisReportShouldBeAddedForNewtrainer() {
        List<TrainingReport> reportResponses = reportServiceClient.getAll("Bearer " + jwtToken);
        Optional<TrainingReport> report = reportResponses.stream().filter(trainingReport -> trainingReport.getTrainerUsername().equals("newtrainer.test")).findFirst();
        assertThat(report).isPresent();
        assertThat(report.get().getTrainerFirstname()).isEqualTo("newtrainer");
        assertThat(report.get().getTrainerLastname()).isEqualTo("test");
        assertThat(report.get().getYears()).containsKey(2024);
        assertThat(report.get().getYears().get(2024)).containsKey("FEBRUARY");
    }

    @And("in report service this report should be added for newtrainer and trainingDuration must be {int}")
    public void inReportServiceThisReportShouldBeAddedForNewtrainerAndTrainingDurationMustBe(int duration) {
        List<TrainingReport> reportResponses = reportServiceClient.getAll("Bearer " + jwtToken);
        Optional<TrainingReport> report = reportResponses.stream().filter(trainingReport -> trainingReport.getTrainerUsername().equals(trainerUsername)).findFirst();
        assertThat(report).isPresent();
        assertThat(report.get().getTrainerFirstname()).isEqualTo(firstname);
        assertThat(report.get().getTrainerLastname()).isEqualTo(lastname);
        assertThat(report.get().getYears()).containsKey(2024);
        assertThat(report.get().getYears().get(2024)).containsKey("FEBRUARY");
        assertThat(report.get().getYears().get(2024).get("FEBRUARY")).isEqualTo(duration);
    }

    @When("the client sends a POST request to create training {string} with new trainer")
    public void theClientSendsAPOSTRequestToCreateTrainingWithNewTrainer(String url, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        //creating new trainer
        firstname = UUID.randomUUID().toString();
        lastname = UUID.randomUUID().toString();
        HttpEntity<TrainerRegisterRequest> requestTrainer = new HttpEntity<>(new TrainerRegisterRequest(firstname, lastname, 1), headers);
        ResponseEntity<UsernamePasswordResponse> responseEntity = restTemplate.exchange("/api/v1/trainer", HttpMethod.POST, requestTrainer, UsernamePasswordResponse.class);
        trainerUsername = responseEntity.getBody().getUsername();
        //create training for new trainer
        HttpEntity<CreateTrainingRequest> request = new HttpEntity<>(new CreateTrainingRequest(data.get("traineeUsername"), trainerUsername, data.get("trainingName"), LocalDate.parse(data.get("trainingDate")), data.get("trainingDuration") != null ? Integer.parseInt(data.get("trainingDuration")) : null), headers);
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

        HttpEntity<CreateTrainingRequest> requestTraining = new HttpEntity<>(new CreateTrainingRequest(data.get("traineeUsername"), trainerUsername, data.get("trainingName"), LocalDate.parse(data.get("trainingDate")), data.get("trainingDuration") != null ? Integer.parseInt(data.get("trainingDuration")) : null), headers);
        responseCode = restTemplate.exchange(url, HttpMethod.POST, requestTraining, Void.class).getStatusCode().value();

    }
}
