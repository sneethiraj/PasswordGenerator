docker build -t infotekies/password-generator-service .
docker scan infotekies/password-generator-service


docker run --name=passwordgen -p 8080:8080 -t infotekies/password-generator-service 

docker-compose -f docker-compose.passwordgen.yaml  down
docker-compose -f docker-compose.passwordgen.yaml  up

#
# cleanup commands
#
docker system df
docker image prune
docker container prune

