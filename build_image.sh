mvn clean install

docker container stop service_a_container
docker container rm service_a_container
docker rmi service_a

docker build -t service_a .
# docker run --network my_service_service_ab_network --name service_a_container -p 8080:8080 -d service_a
#docker run --name service_a_container -p 8080:8080 -d service_a


