./mvnw install


docker container stop service_a_container
docker container rm service_a_container
docker rmi service_a

docker build -t service_a .
docker run --network service_ab_network --name service_a_container -p 3030:8080 -d service_a

