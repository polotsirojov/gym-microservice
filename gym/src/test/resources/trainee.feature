Feature: Trainee API
  Scenario: Register a trainee
    Given a trainee registration request with the following details:
      | firstName | lastName | dob  | address |
      | john123  | doe | 2023-11-22 | address |
    When the client sends a POST request to "/api/v1/trainee"
    Then the response status code should be 200
    And the response body should contain the trainee's username and password

  Scenario: Get trainee profile
    Given an authenticated trainee
    When the client sends a GET request to "/api/v1/trainee"
    Then the response body should contain the trainee's profile details

  Scenario: Trainee updates their profile successfully
    Given the user is authenticated as a trainee
    And the trainee profile update request with the following details:
      | username    | firstName  | lastName | address  | isActive |
      | john123.doe | John       | Doe      | New York | true     |
    When the client sends a PUT request to "/api/v1/trainee"
    Then the response body should contain the updated trainee profile details:
      | firstName | lastName    | address  |
      | John      | Doe         | New York |

  Scenario: Trainee updates their profile unsuccessfully
    Given the user is authenticated as a trainee
    And the trainee profile update request with the following details without isActive:
      | username    | firstName  | lastName | address  |
      | john123.doe | John       | Doe      | New York |
    When the client sends a PUT request to "/api/v1/trainee"
    Then response status code should be 400

  Scenario: Retrieve trainings with filters
    Given the user is authenticated as a trainee
    And a user with username "john123.doe"
    And the client sends a GET request to "/api/v1/trainee/trainings" with the following filters:
      | username    |
      | john123.doe |
    Then response status code should be 200

  Scenario: Retrieve trainings without filters
    Given the user is authenticated as a trainee
    And a user with username "john123.doe"
    When the client sends a GET request to "/api/v1/trainee/trainings" without any filters
    Then response status code should be 400

  Scenario: Trainee activate
    Given the user is authenticated as a trainee
    And a user with username "john123.doe"
    When the client sends a PATCH request to activate-deactivate "/api/v1/trainee/activate/1"
      | username    |
      | john123.doe |
    Then response status code should be 200

  Scenario: Trainee deactivate
    Given the user is authenticated as a trainee
    And a user with username "john123.doe"
    When the client sends a PATCH request to activate-deactivate "/api/v1/trainee/deactivate/1"
      | username    |
      | john123.doe |
    Then response status code should be 200

  Scenario: Trainee delete profile successfully
    Given the user is authenticated as a trainee
    And a user with username "john123.doe"
    When the client sends a DELETE request to "/api/v1/trainee"
    Then response status code should be 200
