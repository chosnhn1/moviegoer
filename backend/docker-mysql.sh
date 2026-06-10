docker run --name moviegoer-db \
    -e MYSQL_ROOT_PASSWORD=dev_password \
    -e MYSQL_DATABASE=moviegoer-db \
    -e MYSQL_USER=moviegoer-admin \
    -e MYSQL_PASSWORD=dev_password \
    -p 3307:3306 \
    -v moviegoer-data:/var/lib/mysql \
    -d mysql:8.0