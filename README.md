[![ci-jvm](https://github.com/ccjmne/fun7-services/actions/workflows/ci-jvm.yml/badge.svg)](https://github.com/ccjmne/fun7-services/actions/workflows/ci-jvm.yml) [![ci-native](https://github.com/ccjmne/fun7-services/actions/workflows/ci-native.yml/badge.svg)](https://github.com/ccjmne/fun7-services/actions/workflows/ci-native.yml)

# fun7-services

A native (!) backend [REST](https://en.wikipedia.org/wiki/Representational_state_transfer) server of Fun7's fictional services, written in [Java](https://openjdk.org/).

```log
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2022-07-25 13:12:19,813 INFO  [io.quarkus] (main) fun7-services 1.0.0-SNAPSHOT native started in 0.019s.
```

## Technologies

- [Quarkus](https://quarkus.io/) framework for development.
  - with [Jib](https://github.com/GoogleContainerTools/jib) for containerisation
- [MongoDB](https://www.mongodb.com/) for data management.
- [Cloud Run](https://cloud.google.com/run) for [serverless](https://en.wikipedia.org/wiki/Serverless_computing) computing.
- [GitHub Actions](https://github.com/features/actions) for [continuous integration](https://www.atlassian.com/continuous-delivery/continuous-integration).

## Play With It

1. Edit [docker-compose.yml](docker-compose.yml) to tweak the [environment variables](#environment-variables)
   - notably, the `ADPARTNER_URL`
2. `docker-compose up --detach`
3. [Query it with your browser](http://localhost:8080?cc=SI&userId=123456789012345678901234&timezone=Europe/Ljubljana)
   - by default, you can access the user administration with `admin`:`pwd`.
4. `docker-compose down`

OR

- Query the [Cloud Run](https://cloud.google.com/run) endpoint where it is (temporarily) hosted:  
  [https://fun7-jsppj7p4zq-od.a.run.app](https://fun7-jsppj7p4zq-od.a.run.app/?cc=SI&userId=123456789012345678901234&timezone=Europe/Ljubljana)
  - you'll likely experience some ~0.040s delay (40ms 😱) the first time, because Cloud Run needs to start up the server :)
  - this endpoint actually queries Fun7's ad partner

## Endpoints

This application exposes two endpoints:

| Where    | What                  | Requires Auth |
| -------- | --------------------- | ------------- |
| `/`      | Services Availability | ❌            |
| `/users` | User Administration   | ✔             |

### User Administration

| Method   | Path Params | What                            |
| -------- | ----------- | ------------------------------- |
| `GET`    | `/`         | List all users                  |
| `GET`    | `/{userId}` | Retrieve a specific user's data |
| `DELETE` | `/{userId}` | Delete a specific user          |

### Services Availability

| Method | Path Params | What                                  |
| ------ | ----------- | ------------------------------------- |
| `GET`  | `/`         | Check availability of Fun7's services |

| Query Param | Meaning           | Format                                                                                       | Required |
| ----------- | ----------------- | -------------------------------------------------------------------------------------------- | -------- |
| `userId`    | User unique ID    | 24 hexadecimal characters<br />e.g.: `123456789012345678901234`                              | ✔        |
| `cc`        | User country code | [ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) alpha-2 or -3<br />e.g.: `SI` | ✔        |
| `timezone`  | User time zone    | "IANA Time Zones"<br />e.g.: `Europe/Ljubljana`                                              | ❌       |

## Environment Variables

| Name                      | Meaning                                                                                       |
| ------------------------- | --------------------------------------------------------------------------------------------- |
| `USERSUPPORT_TZ`          | Time zone to consider for user support availability<br />e.g.: `Europe/Ljubljana`             |
| `USERSUPPORT_FROM`        | Start time for the live support<br />e.g.: `09:00`                                            |
| `USERSUPPORT_TO`          | End time for the live support<br />e.g.: `15:00`                                              |
|                           |                                                                                               |
| `ADPARTNER_URL`           | URL queried to check support for ads by the partner<br />e.g.: `https://postman-echo.com/get` |
| `ADPARTNER_USERNAME`      | Username to authenticate query to ad partner<br />e.g.: `user`                                |
| `ADPARTNER_PASSWORD`      | Password to authenticate query to ad partner<br />e.g.: `pass`                                |
|                           |                                                                                               |
| `MULTIPLAYER_COUNTRY`     | Country where multiplayer is enabled<br />e.g.: `US`                                          |
| `MULTIPLAYER_MINCHECKINS` | The minimum amount of "check-ins" that constitutes a veteran player<br />e.g.: `5`            |
|                           |                                                                                               |
| `MONGO_URL`               | The supporting database connection string<br />e.g.: `mongodb://db:27017`                     |
| `MONGO_DB`                | The DB name it uses<br />e.g.: `users`                                                        |
|                           |                                                                                               |
| `ADMIN_PASSWORD`          | The password for the `admin` account that can access `/users`<br />e.g.: `pwd`                |

### Implementation Details

- A user in the database has 3 properties:

  - `id`: their unique identifier
  - `country`: the code of the country they last "checked in" from
  - `apiCalls`: the number of times they "checked in"

- A user therefore looks like this:

```json
{
  "id": "12345678901234567890abcd",
  "country": "SI",
  "apiCalls": 22
}
```

- The `timezone` query parameter supplied by the user is discarded and serves no purpose

  - `// TODO:` maybe at least store it in the database?

- Users are created/updated in the database ever time they (succesfully) query `GET /`

- The `/users` endpoint isn't publicly accessible and requires authentication

  - by default, set up w/ [basic authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) against an [embedded](https://quarkus.io/guides/security-properties#embedded-realm-configuration) security realm

- I removed the "publish to Google Cloud Registry" part of the CI workflows and only publish to GitHub Container Registry

  - Note to self

  ```shell script
  docker pull ghcr.io/ccjmne/fun7-services-native
  docker tag ghcr.io/ccjmne/fun7-services-native gcr.io/fun7-357415/fun7-services-native:latest
  docker push gcr.io/fun7-357415/fun7-services-native:latest
  ```

- Integration testing isn't my _forte_

- The relevant configuration entries are mapped to environment variables that match `[A-Z_]+`
  - ... because Google Cloud's Kubernetes Engine doesn't accept `dotdash-style.environment-vars`
  - ... despite Kubernetes itself being fine with it. Maybe it's just GKE's Web interface that doesn't play nice.
    - actually, I can't set the minimum number of pods to be `0` either
    - ... despite Kubernetes allowing it
    - (I read in some GitHub issue that GKE also did, so maybe the culprit really is only their Web interface)

## Licensing

**GPL-3.0**

You may copy, distribute and modify the software as long as you track changes/dates in source files. Any modifications to or software including (via compiler) GPL-licensed code must also be made available under the GPL along with build & install instructions.

Refer to the [LICENSE](./LICENSE) file for more details.

---

There is nothing more below but the useful parts of Quarkus' starter project's README.

---

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/fun7-services-1.0.0-SNAPSHOT-runner`
