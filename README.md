# OrganisationService
A Spring Boot / Spring JPA PoC project

>> Context:
The proposal of this PoC is create a web service that provides tools to achieve the follow:

A client has requested the implementation of an application that provides:

    Ability to manage a list of contacts and update their details,
        Each contact has a name, email and phone number,
        Each contact can work for one or more organisations,
        There will potentially be thousands of customers.
    Ability to manage a list of organisations and update their details,

        Each organisation has a name, address and a url,
        There will potentially be hundreds of customers.
    Ability to get a list of organisations.
    Ability to get a list of contacts by organisation.

>> Database:

The application uses an embedded H2 memory server. Hibernate generate the database and tables when the project is run for the first time. So no database setup is necessary.

>> Running:

To run the application is necessary to have Maven installed.
Once configured, it is just execute the command:

mvn spring-boot:run

>> Testing:

For testing, it is necessary a tool capable to make REST calls to a webserver. For my tests I used the command line tool 'CURL'
See examples of use below.

>> Application commands:

For Organisations:

    * Return all organisations
        GET "/organisations"
        Ex.: curl -X GET localhost:8080/organisations

    * Add a new organisation
        POST "/organisations"
        Ex.: curl -X POST localhost:8080/organisations -H 'Content-type:application/json' -d '{"name": "Google Inc", "address": "USA", "url": "www.google.com"}'

    * Return the organisation with id {id}
        GET "/organisations/{id}"
        Ex.: curl -X GET localhost:8080/organisations/3

    * Get all contacts of the organisation with id {id}
        GET "/organisations/{id}/contacts"
        Ex.: curl -X GET localhost:8080/organisations/3/contacts

    * Update organisation details of the organisation with id {id}
        PUT "/organisations/{id}"
        Ex.: curl -X POST localhost:8080/organisations/3 -H 'Content-type:application/json' -d '{"name": "CentricMinds", "address": "Auckland, ACK", "url": "www.centricminds.co.nz"}'

    * Delete the organisation with id {id}
        DELETE "/organisations/{id}"
        Ex.: curl -X DELETE localhost:8080/organisations/3

For Contacts:

    * Return all contacts
        GET "/contacts"
        Ex.: curl -X GET localhost:8080/contacts

    * Get contact of the contact id {id}
        GET "/contacts/{id}"
        Ex.: curl -X GET localhost:8080/contacts/5

    * Add a new contact to the given organisation with id {organisationId} if contact still not exists, otherwise add the existing contact to this organisation.
        POST "/organisations/{organisationId}/contacts"
        Ex. New Contact: curl -X POST localhost:8080/organisations/1/contacts -H 'Content-type:application/json' -d '{"name": "Cristiano Moraes", "email": "cristiano@gmail.com", "phoneNumber": "8888 8888"}'
        Ex. Existing Contact: curl -X POST localhost:8080/organisations/2/contacts -H 'Content-type:application/json' -d '{"id": 3}'

    * Update data of a contact with id {id}
        PUT "/contacts/{id}"
        Ex.: curl -X PUT localhost:8080/contacts/5 -H 'Content-type:application/json' -d '{"name": "Cristiano Moraes", "email": "moraescristiano@hotmail.com", "phoneNumber": "8888 8888"}'

    * Remove contact with id {contactId} from the organisation with id {organisationId}
        DELETE "/organisations/{organisationId}/contacts/{contactId}"
        Ex.: curl -X DELETE localhost:8080/organisations/1/contacts/5

    * Delete contact of the id {id}. Only delete if contact has no connected organisation
        DELETE "/contacts/{id}"
         Ex.: curl -X DELETE localhost:8080/contacts/5
