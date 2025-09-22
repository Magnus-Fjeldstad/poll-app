# DAT250: Software Technology Experiment Assignment 4

## Introduction
The goal of this experiment was to learn the basics of the Java Jakarta Persistence API (JPA) and apply it to an existing domain model by using Hibernate with an in-memory H2 database. The objective was to annotate the domain classes, persist data, and verify functionality using test cases.

## Technical Problems and Solutions
I did not encounter major technical issues in this experiment. The main challenge was to set up the dependencies correctly while ensuring that `spring-boot-starter-data-jpa` was not included, since Spring auto-configuration was not allowed. Once Hibernate, Jakarta Persistence, and H2 were added to the `build.gradle.kts` file, the project built and executed without problems.

Implementing the missing methods in the domain model (`User.createPoll`, `User.voteFor`, `Poll.addVoteOption`) and adding the required JPA annotations resolved the initial compilation errors. After these changes, the `PollsTest` test suite ran successfully.

## Link to Code
[Link to User class as an example](https://github.com/Magnus-Fjeldstad/poll-app/blob/oblig-4/pollapp/src/main/java/com/poll/pollapp/jpa/polls/Poll.java)  

[Link to JPA package](https://github.com/Magnus-Fjeldstad/poll-app/tree/oblig-4/pollapp/src/main/java/com/poll/pollapp/jpa/polls)  


## Inspecting the Database
The project uses an in-memory H2 database (`jdbc:h2:mem:polls`). After running the tests, the database exists only during the JVM process. To inspect it, I used the **H2 Console**:

1. Started the H2 console with the dependency `com.h2database:h2`.  
2. Connected to `jdbc:h2:mem:polls` with user `sa` and empty password.  
3. Verified that the following tables were created:
   - `USERS`
   - `POLL`
   - `VOTE`
   - `VOTEOPTION`

These tables matched the annotated domain classes. Relationships between users, polls, vote options, and votes were correctly represented.


## Pending Issues
There are no pending issues. All test cases passed successfully, and the domain model integrated correctly with JPA and Hibernate.
