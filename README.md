To build and run:

Docker and docker-compose should be installed and running.
Before build run 

    docker-compose up -d kafka1 redis

in project root folder. Then

    mvn clean package spring-boot:repackage docker:build
    
then to start built services run

    docker-compose up -d data-collector rating-calculator
    
To test service manually post

    {"firstName":"hannes","lastName":"tark","age":34}
    
to

    http://localhost:8080/api/rating
    
and check what is stored in redis with request

    http://localhost:8081/api/ratings
    