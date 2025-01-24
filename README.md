**calculator-ipv4-app**


calculator-ipv4-app is a private project created for educational purposes and as part of a portfolio. It is the first project where I used Angular. The application is designed for calculations related to IPv4 addressing.


**Table of Contents**
- Technologies
- Installation
- Running the Application
- Project Structure
- Author


**Technologies**

The project uses the following technologies:

- Frontend:
  - Angular
  - TypeScript
  - HTML
  - CSS
- Backend:
  - Java
  - Spring Boot
- Others:
  - Docker


**Installation**

To run the project locally, follow these steps:

Clone the repository:

git clone https://github.com/KacperKiec/calculator-ipv4-app.git

cd calculator-ipv4-app


Prerequisites:

Ensure you have installed:

- Node.js (with npm)
- Angular CLI
- Java Development Kit (JDK)
- Docker


**Running the Application**

Run with Docker Compose:

In the root directory of the project, there is a docker-compose.yml file. To start all services, run:

docker-compose up

This will launch the backend, frontend, and database in Docker containers.


Manual Run:

If you prefer running without Docker:

- Backend:

cd server

./mvnw spring-boot:run

The backend will be available at http://localhost:8080.


- Frontend:

cd client

npm install

ng serve

The frontend will be available at http://localhost:4200.


**Project Structure**

client/ – Frontend source code (Angular).

server/ – Backend source code (Spring Boot).

docker-compose.yml – Docker Compose configuration file.


**Author**
This project was created by:

Kacper Kieć
