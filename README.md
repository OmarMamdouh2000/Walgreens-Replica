# Walgreens-Replica: A walgreens Replica

![SpringBoot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![ApacheCassandra](https://img.shields.io/badge/cassandra-%231287B1.svg?style=for-the-badge&logo=apache-cassandra&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-FFA611.svg?style=for-the-badge&logo=firebase&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-%23FF6600.svg?style=for-the-badge&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=Kubernetes&logoColor=white)
![JMeter](https://img.shields.io/badge/JMeter-D22128?style=for-the-badge&logo=apachejmeter&logoColor=white)



## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Implementation](#implementation)
   - [Databases](#databases)
     - [PostgreSQL](#postgreSQL)
     - [Apache Cassandra](#apache-cassandra)
     - [Firebase](#firebase)
     - [Redis](#redis)
   - [Microservices](#microservices)
     - [User Management Microservice](#user-management-microservice)
     - [Payment Microservice](#payment-microservice)
     - [Cart Microservice](#cart-microservice)
     - [Orders Microservice](#orders-microservice)
     - [Products Microservice](#products-microservice)
   - [Media Server](#media-server)
   - [Message Queues](#message-queues)
   - [Dockerizing The App](#dockerizing-the-app)
     - [Cleaning & Packaging Microservices](#cleaning-&-packaging-microservices) 
     - [Building Docker Images](#building-docker-images)
     - [Uploading Images on DockerHub](#uploading-image-dockerhub)
   - [Deployment](#deployment)
     - [Kubernetes](#kubernetes)
     - [Auto Scalar](#auto-scalar)
     - [Service Discovery](#service-discovery)
     - [Load Balancer](#load-balancer)
     - [Web Server](#web-server)
     - [CI/CD](#contionous-integration)
   - [Logging & Monitoring](#logging&monitoring)
     - [SLF4J](#slf4j)


## Project Overview

**Walgreens-Replica** is a microservices-based platform designed to replicate the core functionalities of the Walgreens pharmacy management system. 🏥 Focused on efficiency and user accessibility, the system provides comprehensive services ranging from administrative controls to customer interactions. Engineered to handle robust traffic and secure data transactions, Walgreens-Replica utilizes cutting-edge technologies to ensure operational reliability and scalability. 🌟

## 🏛️ Architecture

#### User Management Architecture

View the [User Management Architecture diagram](https://drive.google.com/file/d/1HI00kpG8-NS1b66_aAPfi-HBECqZjdIW/view?usp=sharing).

#### Payment Architecture

View the [Payment Architecture diagram](https://drive.google.com/file/d/1w_wqur5WADjQbF_G26jKbfrpmjmf4vMO/view?usp=sharing).

#### Product Architecture

View the [Product Architecture diagram](https://drive.google.com/file/d/1jsrCwFj9r7thlRNdVK2F2TypnOXnf6hC/view?usp=sharing).

#### Order and Cart Architectures

View the [Order and Cart Architectures](https://drive.google.com/file/d/1w2W2aP3YkwUcmH1aQBfH3rc9zjihwPQT/view?usp=sharing).




## 🛠️ Implementation

### 🛢️ Databases
The system utilizes a variety of databases tailored to specific needs:

- **PostgreSQL**: Employs a robust relational centralized database for structured data storage and management. Used in User Management and Payment microservices to ensure data consistency and integrity due to their need for complex transactional operations.
- **NoSQL (Apache Cassandra)**: Utilizes a scalable and distributed NoSQL database for handling large volumes of unstructured data with high availability and fault tolerance. This is employed by the Product, Order, and Cart microservices, where quick data access and high scalability are crucial.
- **Firebase**: Integrates a cloud-based database solution for media storage, facilitating efficient storage and retrieval of media files with minimal latency.
- **Redis**: Implements an in-memory data store used as a cache across all aforementioned services, optimizing response times and reducing computational overhead by caching frequently accessed data.

<details>
   <summary>
      🐘 PostgreSQL
   </summary>
  In the system architecture, PostgreSQL is employed for the User Management and Payment microservices, which require consistent and reliable data handling capabilities. This centralized database supports complex queries and transactions, ensuring data integrity and consistency necessary for sensitive operations such as user data management and financial transactions.
</details>

<details>
   <summary>
      👁 Apache Cassandra
   </summary>
  Apache Cassandra is chosen for the Product, Order, and Cart microservices due to its high performance in environments that demand scalability and high-speed access to large volumes of data. Its distributed nature supports rapid growth and data distribution across multiple nodes, ensuring reliability and speed during high-demand periods.
</details>

<details>
   <summary>
      🔥 Firebase
   </summary>
  Firebase serves as the media server for the system, providing a cloud-based solution for storing and serving media content. Its real-time database and storage capabilities enable seamless integration with the application, allowing users to upload, retrieve, and stream media files with minimal latency. Firebase's scalability and reliability ensure uninterrupted access to media content, while its authentication and security features safeguard sensitive data. By leveraging Firebase as a media server, the system delivers a seamless and responsive multimedia experience to users across platforms.
</details>

<details>
   <summary>
      💾 Redis
   </summary>
  Redis plays a pivotal role in the system architecture, serving as a high-performance caching layer for optimizing data access and response times. Utilized across various microservices, Redis efficiently stores and retrieves frequently accessed data, such as session information, user preferences, and temporary application state. Its in-memory data storage and support for data structures enable fast and reliable caching, reducing the need for repeated computations and database queries. By leveraging Redis, the system enhances scalability, resilience, and overall performance, ensuring a seamless and responsive user experience. This caching mechanism significantly reduces the load on the database servers, alleviating potential bottlenecks and enhancing overall system performance by minimizing the need for repetitive and resource-intensive database queries.
</details>


### 🏗️ Microservices

- All microservices are implemented using Java Spring Boot ☕.
- They consume messages from the Apache Kafka message broker and respond through Apache Kafka MQ as well.
- Requests are cached in Redis, so if the same request is sent more than once, there is no need to recompute the response every time and fetch the data again from the database as it can be retrieved directly from the cache 🗃️.
- In some cases, a microservice would have to communicate with another one to complete a certain functionality, which is done through Apache Kafka MQ. Every microservice can be scaled up or down independently of other microservices to adapt to the amount of incoming traffic.

<details>
   <summary>
      👥 User Management Microservice
   </summary>
 The User Management Microservice primarily focuses on user authentication (Login & Registration), implemented using Spring Boot Security. This service interacts mainly with PostgreSQL for user data storage and management. Additionally, to optimize authentication performance, the service caches the generated JWT tokens upon successful login in a shared Redis cache. This microservice also handles common operations shared by all users, such as changing usernames/passwords and managing user-related functionalities.
</details>

<details>
   <summary>
      💳 Payment Microservice
   </summary>
   The Payment Microservice handles all aspects of financial transactions within the system, including processing payments, managing wallets, and maintaining transaction history. It employs PostgreSQL due to its strong ACID properties, ensuring data consistency and reliability for all financial operations. To enhance performance, frequently accessed data such as transaction histories and balance checks are cached using Redis, ensuring quick access and a smooth user experience.
</details>

<details>
   <summary>
      🛒 Cart Microservice
   </summary>
   The Cart Microservice manages the shopping cart functionality, allowing users to add, update, or remove products from their cart. This service utilizes a NoSQL database for high performance and scalability, especially suitable for the dynamic nature of cart operations which require high-speed read and write capabilities. Redis is also used to cache cart data, significantly speeding up cart operations and improving response times during high traffic periods.
</details>

<details>
   <summary>
      📦 Orders Microservice
   </summary>
   The Orders Microservice oversees the order processing workflow, from order placement to final delivery tracking. It leverages NoSQL databases to handle large volumes of orders with high availability and fault tolerance. This microservice is optimized for quick access to order data and scalability, using Redis to cache order statuses and summary data for fast retrieval.
</details>

<details>
   <summary>
      🛍️ Products Microservice
   </summary>
   The Products Microservice is responsible for managing product listings, including creating, updating, and deleting product information. It uses NoSQL databases to store product data, ensuring scalability and rapid access when fetching product details. Redis is utilized here to cache frequently accessed data like product prices and descriptions, allowing for efficient and fast user interactions.
</details>




 ### 🎬 Media Server

The Media Server, powered by Firebase, serves as the cornerstone for handling multimedia content within the Guru.com replica platform. Leveraging Firebase's cloud-based storage and real-time database capabilities, this server facilitates seamless uploading, storage, and retrieval of media files, including images, videos, and documents. With Firebase's robust security features and scalability, the Media Server ensures secure and reliable access to multimedia content across devices and platforms. 

### 📬 Message Queues

Apache Kafka serves as the backbone for asynchronous communication between various microservices in this project 🚀. Kafka's message queues play a pivotal role in facilitating communication. Kafka consumes messages from different sources, including the User Management, Orders, Payment, Cart, and Products Microservices, each with its dedicated topic.

### 🐋 Dockerizing The App

<details>
   <summary>
      📦 Cleaning & Packaging Microservices
   </summary>
  To Dockerize the application, the first step involved executing Maven commands to clean and package the microservices. This process entailed running mvn clean to remove any previously compiled artifacts and mvn package to compile the source code, run tests, and package the application into executable .jar files. Each microservice, structured as a Maven project, underwent this meticulous process to ensure that all dependencies were resolved and included in the packaged artifacts. These .jar files served as the executable units encapsulating the microservices, ready for containerization within Docker. This methodical approach laid a solid foundation for seamless integration and deployment within Docker containers.
</details>

<details>
   <summary>
      🔨 Building Docker Images
   </summary>
  After cleaning and packaging the microservices, the next step involved building Docker images for each microservice. This process was accomplished using Dockerfile configurations, which specified the environment and dependencies required to run the microservice within a Docker container. Leveraging Docker's build capabilities, the Docker images were created with efficiency and consistency. Each Docker image encapsulated the packaged microservice artifact, ensuring that it could be executed within a containerized environment. This step ensured that the microservices were properly containerized and ready for deployment across various environments.
</details>

<details>
   <summary>
      📤 Uploading Images on DockerHub
   </summary>
  Once the Docker images for the microservices were built, the final step involved uploading these images to DockerHub. DockerHub served as the central repository for storing and sharing Docker images, providing a convenient platform for managing and distributing containerized applications. Each Docker image was tagged with version information and securely uploaded to DockerHub. This process made the Docker images accessible online and facilitated seamless deployment across different environments. By leveraging DockerHub, it was ensured that the Docker images were readily available for deployment, streamlining the sharing and collaboration of containerized applications.
</details>

### 🚀 Deployment
In this project, the microservices were deployed locally on a Docker Kubernetes cluster along with Postgres, PgAdmin, RabbitMQ, Redis, Prometheus, and Grafana, enabling efficient management and scalability of the application components. Leveraging Docker containers orchestrated by Kubernetes, each microservice was encapsulated and deployed as a scalable and isolated unit. This deployment approach facilitated seamless integration and testing of the microservices in a controlled environment, allowing for rapid development iterations and ensuring consistency across deployments. Additionally, the use of Kubernetes provided automated deployment, scaling, and management capabilities, empowering the team to efficiently manage and orchestrate the deployment lifecycle of the microservices. Overall, deploying the microservices locally on a Docker Kubernetes cluster enhanced development agility and reliability, laying a solid foundation for future scalability and production deployment.

<details>
   <summary>
       ☸ Kubernetes
   </summary>
  The Kubernetes deployment configuration outlines the deployment details for the microservices within the application ecosystem. With a starting replica count of 2, Kubernetes ensures high availability by maintaining two instances of the microservice to handle incoming requests. Pods are selected based on the specified label, ensuring consistency in pod selection and management across the cluster. Each pod is based on a Docker image, configured to expose its own port for incoming traffic. The deployment incorporates a readiness probe, configured to check the "/actuator/health" endpoint of the Spring Boot application (That endpoint is predefined if the app uses spring boot actuator). This probe ensures that the deployed service is fully initialized and ready to accept requests before being added to the load balancer rotation. With an initial delay of 30 seconds and subsequent checks every 10 seconds, Kubernetes waits for the service to become ready before directing traffic to it. This approach prevents premature routing of requests to the service, guaranteeing a seamless user experience once the service is fully operational. Additionally, the deployment specifies resource requests and limits to manage the memory and CPU utilization of the deployed pods effectively. Resource requests are set to 128Mi of memory and 250m of CPU, while resource limits are set to 512Mi of memory and 750m of CPU. By defining these resource constraints, Kubernetes ensures efficient resource utilization and prevents resource contention among pods within the cluster.
</details>

<details>
   <summary>
       📈 Auto Scalar
   </summary>
 Before configuring the Horizontal Pod Autoscaler (HPA) for our microservices within the application ecosystem, we ensured the Kubernetes Metrics Server was enabled to gather resource utilization metrics across the cluster. With this prerequisite in place, The Horizontal Pod Autoscaler (HPA) configuration outlines the scaling behavior for a microservice within the application ecosystem. With a scale target reference to the corresponding Deployment, the HPA ensures dynamic scaling based on resource utilization metrics. The HPA is configured to scale up or down ⬆️⬇️ based on average CPU utilization, targeting a utilization threshold of 80% and scaling up to 5 parallel pod in total (limited to only 5 due to the host machine capabilites and RAM). When resource utilization exceeds this threshold, the HPA initiates scaling actions ⬆️ to increase the number of replicas, ensuring optimal performance and resource utilization. To prevent excessive scaling, the HPA incorporates scaling policies with stabilization windows for both scaling up and scaling down. These policies aim to stabilize the system before initiating scaling actions, avoiding rapid fluctuations in replica counts and ensuring stability under varying workload conditions. Overall, this HPA configuration enables adaptive scaling of microservices based on resource utilization, enhancing efficiency and performance within the Kubernetes cluster.
</details>

<details>
   <summary>
       🔎 Service Discovery
   </summary>
  In Kubernetes, service discovery is a crucial aspect of managing distributed applications. Kubernetes provides built-in service discovery mechanisms that allow applications to locate and communicate with each other dynamically. This is achieved through Kubernetes Services, which act as an abstraction layer to provide a stable endpoint for accessing pods that belong to a specific application. By using labels and selectors, Kubernetes Services automatically discover and route traffic to the appropriate pods, regardless of their underlying infrastructure or location within the cluster. This enables seamless communication between microservices and facilitates the scalability and resilience of distributed applications in Kubernetes environments.
</details>

<details>
   <summary>
       ⚖️ Load Balancer
   </summary>
  In Kubernetes, the built-in Load Balancer functionality is facilitated through the Kubernetes Service object. This component plays a pivotal role in distributing incoming traffic across multiple instances of an application or service deployed within a Kubernetes cluster. The Kubernetes Service abstracts away the complexities of load balancing by providing a stable endpoint, known as a ClusterIP, for accessing pods associated with a specific application or service. By leveraging labels and selectors defined in the Service configuration, Kubernetes dynamically routes incoming traffic to the appropriate pods, ensuring efficient load distribution and high availability. The routing algorithm used by the built-in Kubernetes Load Balancer is typically round-robin, which evenly distributes incoming requests among the available pods. This approach ensures that each pod receives a fair share of the incoming traffic, preventing overloading of any single pod and promoting scalability and resilience within the cluster.
</details>

<details>
   <summary>
       🌐 Web Server
   </summary>
  The Kubernetes Ingress configuration defines the routing rules for incoming HTTP traffic to the NGINX web server within the Kubernetes cluster. Using the Ingress resource, we exposed HTTP and HTTPS routes from outside the cluster to services within the cluster, enabling external access to applications and microservices. In this configuration, the Ingress resource specifies routing rules based on the requested host and URL path. Annotations are used to configure additional behavior, such as rewriting URL paths. The Ingress resource abstracts away the complexities of managing external access to services and provides a centralized configuration for routing HTTP traffic within the Kubernetes cluster.
</details>


### 📊 Logging & Monitoring

<details>
   <summary>
       📚 SLF4J
   </summary>
  We implemented logging functionality using the Simple Logging Facade for Java (SLF4J) framework, a widely adopted logging abstraction layer. This allows us to decouple the logging implementation from the application code, providing flexibility to switch between different logging frameworks such as Logback, Log4j, or Java Util Logging (JUL) without modifying the codebase. Our logging aspect, represented by the AppLogger class, utilizes SLF4J's logger interface to record method invocations, arguments, return values, exectution time, and exceptions. By leveraging SLF4J, we ensure consistent and standardized logging across our microservices, facilitating troubleshooting, monitoring, and performance analysis.
</details>
