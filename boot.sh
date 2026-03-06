curl https://start.spring.io/starter.zip \
  -d dependencies=web,devtools,security,data-jpa,lombok \
  -d bootVersion=3.5.11 \
  -d javaVersion=21 \
  -d type=gradle-project \
  -d groupId=com.moviegoer \
  -d artifactId=backend \
  -d packageName=com.moviegoer.backend \
  -o backend.zip
