# This Branch has the Kuberentes deployments and Docker-compose files for all external services used by our mini-services

In the same folder "DevTree" you will have a folder for each mini-service which has the dockerfile used to build the corredponding service.
Check the tree shown at the bottom.

1) For building the image you will need to be in the docker env of minikube.
2) The docker files can be built from the DevTree. e.g. ```docker build --no-cache -f Order/Walgreens-Replica/Docker/order-mini-service/Dockerfile -t  walgreens/mini-service/order .```

DevTree\
|-- Order\
|    |-- Walgreens-Replica\
|    |    |-- Docker\
|    |    |    |-- order-mini-serivce\
|    |    |    |    |-- Dockerfile\
|-- Cart\
|    |-- Walgreens-Replica\
|    |    |-- Docker\
|    |    |    |-- cart-mini-serivce\
|    |    |    |    |-- Dockerfile\
|-- Payment\
|    |-- Walgreens-Replica\
|    |    |-- Docker\
|    |    |    |-- payment-mini-serivce\
|    |    |    |    |-- Dockerfile\
|-- Product\
|    |-- Walgreens-Replica\
|    |    |-- Docker\
|    |    |    |-- product-mini-serivce\
|    |    |    |    |-- Dockerfile\
|-- User\
|    |-- Walgreens-Replica\
|    |    |-- Docker\
|    |    |    |-- userManagement-mini-serivce\
|    |    |    |    |-- Dockerfile\
|-- External Docker\
|-- Kubernetes
