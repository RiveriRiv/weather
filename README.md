# weather application

# How tu use

1. Start the spring-boot application by building with 'gradle clean build' and running Application.class 
or running docker container
2. Go to localhost:8080/weather in browser
3. Choose location in a rendered google map by using search and clicking to a random place near a found area 
4. Tap submit button

# Docker
To run docker container use:
1. docker load < weather.tar
2. docker run -p 127.0.0.1:8080:8080 localhost:8080/weather:v1

