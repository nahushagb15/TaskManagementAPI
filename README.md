# Task Management API – DevOps Project

A Spring Boot REST API for managing tasks, built and deployed using DevOps tools and technologies. This project demonstrates source code management, continuous integration, containerization, automation, Kubernetes orchestration, and persistent database storage.

## 1. Project Overview

The Task Management API allows users to create, retrieve, and monitor tasks through RESTful endpoints.

The application uses PostgreSQL for persistent storage, Docker for containerization, Kubernetes for deployment and orchestration, Jenkins for continuous integration, and Ansible for automation.

### Key Features

- Create new tasks through REST APIs.
- Retrieve all tasks or a specific task by ID.
- View task statistics, including completed and pending tasks.
- Store task data in PostgreSQL.
- Deploy the application using Kubernetes.
- Run three application replicas for availability.
- Use persistent storage for PostgreSQL data.
- Monitor application health through a health-check endpoint.

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 4.0.9 | REST API development |
| Maven | Dependency management and build |
| Git | Version control |
| GitHub | Source code hosting |
| Jenkins | Continuous Integration |
| Docker | Application containerization |
| Ansible | Automation and configuration |
| Kubernetes | Container orchestration |
| Minikube | Local Kubernetes cluster |
| PostgreSQL 16 | Persistent database storage |

## 3. System Architecture

```text
                 Developer
                     |
                     v
                Git / GitHub
                     |
                     v
                  Jenkins
             (CI Build Process)
                     |
                     v
                  Maven
                     |
                     v
               Spring Boot API
                     |
                     v
                   Docker
                     |
                     v
                 Kubernetes
                     |
          +----------+----------+
          |          |          |
          v          v          v
       API Pod    API Pod    API Pod
          |          |          |
          +----------+----------+
                     |
                     v
                PostgreSQL
                     |
                     v
             Persistent Volume
```

Ansible is used for automation, while Kubernetes manages application deployment, replicas, and service access.

## 4. Project Structure

```text
TaskManagementAPI/
│
├── ansible/
│   ├── inventory/
│   │   └── hosts
│   └── site.yml
│
├── k8s/
│   ├── deployment.yaml
│   ├── postgres.yaml
│   └── service.yaml
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/devops/taskmanager/
│       │       ├── Task.java
│       │       ├── TaskController.java
│       │       ├── TaskRepository.java
│       │       └── TaskStatsController.java
│       │
│       └── resources/
│           └── application.properties
│
├── Dockerfile
├── pom.xml
├── mvnw
└── README.md
```

## 5. Prerequisites

Install or configure the following tools:

- Java Development Kit 17
- Maven
- Git
- Docker
- Jenkins
- Ansible
- Kubernetes
- Minikube
- kubectl

## 6. Clone the Repository

Clone the project:

```bash
git clone https://github.com/nahushagb15/TaskManagementAPI.git
```

Navigate to the project directory:

```bash
cd TaskManagementAPI
```

To access the PostgreSQL-enhanced version:

```bash
git checkout feature/postgresql-persistence
```

## 7. Build the Application

Build the Spring Boot application using Maven:

```bash
./mvnw clean package -DskipTests
```

The generated JAR file is available at:

```text
target/taskmanager-0.0.1-SNAPSHOT.jar
```

## 8. Docker Containerization

The application uses a Dockerfile based on Eclipse Temurin Java 17.

### Build Docker Image

```bash
docker build -t taskmanagement-api:v2 .
```

### Run the Container

The application requires PostgreSQL to be accessible. Configure the database environment variables before running the container.

```bash
docker run -d \
  --name taskmanagement-api \
  -p 8081:8081 \
  -e DB_HOST=<database-host> \
  -e DB_PORT=5432 \
  -e DB_NAME=taskmanager \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=<database-password> \
  taskmanagement-api:v2
```

## 9. Jenkins – Continuous Integration

Jenkins is used to automate the application's CI build process.

The configured Jenkins job is:

```text
TaskManagementAPI-CI
```

Jenkins is used to execute the configured build process and display build results.

Access the Jenkins dashboard locally:

```text
http://localhost:8080
```

## 10. Ansible Automation

Ansible is used to automate project configuration and execution tasks.

The Ansible playbook is located at:

```text
ansible/site.yml
```

Inventory configuration:

```text
ansible/inventory/hosts
```

Execute the playbook using:

```bash
ansible-playbook -i ansible/inventory/hosts ansible/site.yml
```

## 11. Kubernetes Deployment

The application is deployed on a local Kubernetes cluster using Minikube.

### Start Minikube

```bash
minikube start --driver=docker
```

### Configure Docker to Use Minikube

```bash
eval $(minikube docker-env)
```

### Build the Application Image

```bash
docker build -t taskmanagement-api:v2 .
```

### Deploy PostgreSQL

```bash
kubectl apply -f k8s/postgres.yaml
```

This manifest creates:

- PostgreSQL Secret
- PersistentVolumeClaim
- PostgreSQL Deployment
- PostgreSQL ClusterIP Service

### Deploy the Application

```bash
kubectl apply -f k8s/deployment.yaml
```

### Create the Kubernetes Service

```bash
kubectl apply -f k8s/service.yaml
```

### Verify Deployment

Check the running pods:

```bash
kubectl get pods
```

Check deployments:

```bash
kubectl get deployments
```

Check services:

```bash
kubectl get services
```

Check persistent storage:

```bash
kubectl get pvc
```

The application is configured with three replicas, while PostgreSQL runs as a separate deployment with persistent storage.

### Access the Application

Get the Minikube IP:

```bash
minikube ip
```

The API is accessible through NodePort `30081`:

```text
http://<MINIKUBE-IP>:30081
```

## 12. PostgreSQL Integration

PostgreSQL is integrated using Spring Data JPA.

The application uses a repository interface extending `JpaRepository` to perform database operations.

### Database Configuration

The database connection is configured through environment variables:

| Variable | Purpose |
|---|---|
| DB_HOST | PostgreSQL service hostname |
| DB_PORT | PostgreSQL port |
| DB_NAME | Database name |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |

Within Kubernetes, the application connects to the PostgreSQL service using the hostname:

```text
postgres
```

Hibernate automatically updates the database schema using the configured JPA setting.

### Persistent Storage

A Kubernetes PersistentVolumeClaim provisions 2 GiB of storage for PostgreSQL.

This ensures that task data is not dependent on the lifecycle of individual API pods.

All three API replicas connect to the same PostgreSQL database, allowing shared access to task records.

## 13. API Endpoints

Base URL:

```text
http://<MINIKUBE-IP>:30081
```

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Check application health |
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks` | Retrieve all tasks |
| GET | `/api/tasks/{id}` | Retrieve a task by ID |
| GET | `/api/tasks/stats` | Retrieve task statistics |

### Create a Task

Request:

```bash
curl -X POST http://<MINIKUBE-IP>:30081/api/tasks \
-H "Content-Type: application/json" \
-d '{
  "title": "Buy milk",
  "description": "Purchase milk",
  "completed": false
}'
```

Example response:

```json
{
  "id": 1,
  "title": "Buy milk",
  "description": "Purchase milk",
  "completed": false
}
```

### Retrieve All Tasks

```bash
curl http://<MINIKUBE-IP>:30081/api/tasks
```

### Retrieve a Task by ID

```bash
curl http://<MINIKUBE-IP>:30081/api/tasks/1
```

### Retrieve Task Statistics

```bash
curl http://<MINIKUBE-IP>:30081/api/tasks/stats
```

Example response:

```json
{
  "totalTasks": 1,
  "completedTasks": 0,
  "pendingTasks": 1
}
```

### Check Application Health

```bash
curl http://<MINIKUBE-IP>:30081/api/health
```

## 14. Testing and Verification

The following functionality has been verified:

- Successful Maven application build.
- Successful Docker image creation.
- Successful Kubernetes deployment.
- Three application replicas running successfully.
- PostgreSQL deployment running successfully.
- PersistentVolumeClaim successfully bound.
- Successful task creation through the REST API.
- Successful retrieval of stored tasks.
- Successful task statistics response.
- Task data retained after deleting and recreating an API pod.

## 15. Key Learning Outcomes

This project demonstrates practical implementation of:

1. Version control using Git and GitHub.
2. Automated build processes using Jenkins.
3. Application packaging using Maven.
4. Containerization using Docker.
5. Infrastructure and application automation using Ansible.
6. Container orchestration using Kubernetes.
7. Database integration using PostgreSQL.
8. Persistent storage and shared data access across multiple application replicas.

## 16. Future Enhancements

- Implement task update and delete operations.
- Add automated unit and integration tests.
- Introduce a complete CI/CD deployment pipeline.
- Add monitoring and logging.
- Implement authentication and authorization.
- Deploy the application to a cloud Kubernetes environment.

## 17. Repository

GitHub Repository:

https://github.com/nahushagb15/TaskManagementAPI

**Project:** Task Management API – DevOps Implementation

**Branch containing PostgreSQL integration:** `feature/postgresql-persistence`
