# Walmart-Replica
## To run Postgres and pgAdmin
1. Navigate in the terminal to the repo root folder which contains the docker-compose.yaml file.
2. Run ```docker-compose up``` in the terminal.
3. Wait for the containers to run.
4. Open ```http://localhost:8080/``` on your browser to open pgadmin.
5. Use ```root@root.com``` as the email and ```root``` as the password.
6. Right-click on the server tab in the object explorer and choose register then server.
7. Type the name as ```Walgreens```.
8. Switch to the connection tab.
9. Write ```postgres_database``` in the hostname field. Leave the port as ```5432```.
10. Write both the username and password as ```root```.
11. Now, you will have the pgadmin connected to the user_management database so that you can visualize some statistics and run queries from pgadmin.
