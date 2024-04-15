# Medical Clinic (In progress)
This project was created using Spring Boot technology and serves as a website for a medical clinic. Users can have one of three roles: "admin", "doctor" and "patient". The first role has all possible competences and, after authorization, is able to use all elements of the website, including the admin panel. A user who logs in as a doctor can use the doctor's panel, where he can display future and already completed visits, and can also add/edit a note for completed visits. Another functionality is the ability to display visit details along with the diseases of the patient assigned to a given visit. The patient can, use the patient panel, where he can also view completed and upcoming visits and has the option to delete future visits. This user can make another appointment provided that the selected doctor has an available date. An additional functionality of the website is the ability to use an external API that presents Covid data for a given day for Poland. Unit and integration tests were written for the application.

To use this application you must authenticate yourself. To see patient's capabilities you can use this data:

login: zygmunt_pacjent

password: test

To see doctor's capabilities you can use this data:

login: adam_doktor

password: test

To see admin's capabilities you can use this data:

login: patryk_luczak

password: test

Technologies used:
* Java 17
* Spring Boot
* Spring
* Spring REST
* Spring Data JPA
* Spring Security
* Spring Validation
* Hibernate
* Postgres
* Flyway
* Thymelaef
* Lombok
* Mapstruct
* Mockito
* WireMock
* Testcontainers
* RestAssured
* Jacoco
* OpenAPI
