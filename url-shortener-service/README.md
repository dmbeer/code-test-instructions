# url-shotner

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)

## Features

Here's a list of features included in this project:

| Name                                                                  | Description                                                                        |
| -----------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)    | Provides automatic content conversion according to Content-Type and Accept headers |
| [Routing](https://start.ktor.io/p/routing)                            | Provides a structured routing DSL                                                  |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Request Validation](https://start.ktor.io/p/request-validation)      | Adds validation for incoming requests                                              |
| [Default Headers](https://start.ktor.io/p/default-headers)            | Adds a default set of headers to HTTP responses                                    |
| [Status Pages](https://ktor.io/docs/server-status-pages.html) | Adds Status Pages and Exception Handling  |
| [MongoDB](https://www.mongodb.com/) | MongoDB NoSQL Database |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                                    | Description                                                          |
| -----------------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`                        | Run the tests                                                        |
| `./gradlew build`                       | Build everything                                                     |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `./gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `./gradlew run`                         | Run the server                                                       |
| `./gradlew runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

