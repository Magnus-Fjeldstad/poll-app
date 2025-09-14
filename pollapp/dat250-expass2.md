# dat250-expass2.md

## Summary

This project is a Spring Boot application that exposes a simple REST API. I decided to follow a documentation-first
approach by writing a Swagger (OpenAPI) contract before implementing the API. Using the OpenAPI
Generator (`openApiGenerate` task in Gradle), I generated the models and controller interfaces based on the
specification. This allowed me to focus on implementing the logic behind each endpoint directly from the generated
structure.

The development process went relatively smoothly since I spent around eight weeks over the summer working with similar
setups. During that time, I became comfortable using OpenAPI and Gradle in combination, which made the implementation
phase more efficient.

## Technical Problems Encountered

The most significant technical challenge was setting up CI/CD using GitHub Actions. This was my first time working with
continuous integration and deployment pipelines, so I had to familiarize myself with how GitHub workflows function. I
followed official documentation and various community examples to build a workflow that could automatically build and
test the application on each push. While it took some experimentation, I was eventually able to set up a working
pipeline for this project.

Another issue I encountered was with writing and running integration tests using REST Assured. While I was able to write
basic tests to validate some endpoints, I ran into problems getting them to execute correctly within the Gradle build.
There appear to be configuration issues or missing dependencies related to the test environment setup. I was not able to
fully resolve this in time for the submission, so REST Assured integration testing remains partially incomplete.

## Pending Issues

While the API implementation is complete and behaves as expected, the REST Assured integration tests are not fully
working. This is something I would like to improve in the future by reviewing the Gradle test setup and possibly
restructuring the test configuration.

Additionally, although the CI/CD pipeline builds and tests the project, it does not currently support deployment steps
or Docker integration. These would be valuable additions to explore in a more production-oriented setting.
