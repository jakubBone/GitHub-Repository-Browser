# 📖 GitHub Repository Browser

This project is a REST API application built with Java and the Spring Boot framework. 

The application fetches and displays a specified GitHub owner's public repositories, excluding forks. 
It communicates with the official GitHub API to retrieve the data, processes it, and exposes it through a custom endpoint.

## 🏛️ Design Philosophy

This project was built following the provided requirements. The following points clarify the key architectural and implementation decisions:

- **Focused Scope**: As requested, the project implements only the required functionality without adding any extra features

- **Simple Layered Architecture**: The application utilizes three-layer architecture (Controller -> Service -> API Client)

- **Integration Test**: Following the requirements the project includes a single, comprehensive integration test `RepositoryBrowserIntegrationTest` without mocking

- **Error Handling**: The implementation handles the error scenario for a non-existent owner by returning a 404 status with a JSON response body

## 🚀 Technologies & Tools

- **Java 21**: The core language
- **Spring Boot 3**: The main framework for building the application
- **Spring MVC**: Used for creating REST API endpoints
- **Spring RestClient**: HTTP client for communicating with the GitHub API
- **JUnit 5 & AssertJ**: For integration test
- **Log4j2**: For application event logging
- **Maven**: For dependency management and building the project

## 🛠️ Getting Started

To run the application locally, please follow the steps below:

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven installed and configured
- A GitHub API access token

### Configuration

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jakubBone/GitHub-Repository-Browser.git
   cd GitHub-Repository-Browser
   ```

2. **Configure Environment Variables:**
   
   The application requires a token to authorize requests to the GitHub API. Create a `.env` file in the project's root directory and add your token to it
   ```
   API_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```
   
   - Alternatively, you can set the environment variable directly in your operating system.
   - The application reads the token from the application properties.
   - The base API URL is also configured in `application.properties` and defaults to `https://api.github.com/`
   
   
   **Important**: The absence of a token will cause an `IllegalStateException` which is an intentional safeguard

### Running the Application

1. **Build and run the application using Maven:**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on the default port 8080.
   

2. **Run the tests:**
   ```bash
   mvn test
   ```

## 🌐 API Endpoint

The application exposes one main endpoint for retrieving data.

### Get User Repositories

Retrieves a list of a owner's repositories (that are not forks) along with their branches and the last commit SHA for each branch

- **URL**: `/api/v1/repositories/{owner}`
- **Method**: `GET`
- **Parameter**: `{owner}` - The GitHub owner login

### Example Usage (cURL)

```bash
curl -i http://localhost:8080/api/v1/repositories/octocat
```

### Sample Success Response (200 OK)

```json
[
    {
        "name": "Hello-World",
        "owner": {
            "login": "octocat"
        },
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0"
            }
        ]
    },
    {
        "name": "Spoon-Knife",
        "owner": {
            "login": "octocat"
        },
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "f1e2d3c4b5a6f7e8d9c0b1a2f3e4d5c6b7a8f9e0"
            },
            {
                "name": "test-branch",
                "lastCommitSha": "0a9b8c7d6f5e4d3c2b1a0f9e8d7c6b5a4f3e2d1c"
            }
        ]
    }
]
```

### Sample Error Response (404 Not Found)

Returned when the specified owner does not exist on GitHub

```json
{
    "statusCode": 404,
    "message": "Owner not found"
}
```

## 🧪 Testing

The project includes comprehensive integration tests that verify:

- Successful retrieval of non-fork repositories for existing owners
- Handling of owners with no repositories
- Correct filtering of forked repositories
- Appropriate 404 responses for non-existent owners
- Error handling for missing owner parameter

## 📋 Acceptance Criteria

The application fulfills the following acceptance criteria:

1. **As an API consumer**, I can list all GitHub repositories for a owner which are not forks
2. **Repository information** includes: repository name, owner login and for each branch: name and last commit SHA
3. **Error handling**: For non-existing GitHub owners, returns 404 response with proper JSON format containing status code and error message
