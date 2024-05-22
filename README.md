# This Branch has the Kuberentes deployments and Docker-compose files for all external services used by our mini-services

In the same folder "DevTree" you will have a folder for each mini-service which has the dockerfile used to build the corredponding service.
Check the tree shown at the bottom.

1) For building the image you will need to be in the docker env of minikube.
2) The docker files can be built from the DevTree. e.g. ```docker build --no-cache -f Order/Walgreens-Replica/Docker/order-mini-service/Dockerfile -t  walgreens/mini-service/order .```

DevTree\
|-- Order\
|  &emsp;  |-- Walgreens-Replica\
|  &emsp;  |  &emsp;  |-- Docker\
|  &emsp;  |  &emsp;  |  &emsp;  |-- order-mini-serivce\
|  &emsp;  |  &emsp;  |  &emsp;  |  &emsp;  |-- Dockerfile\
|-- Cart\
|  &emsp;  |-- Walgreens-Replica\
|  &emsp;  |  &emsp;  |-- Docker\
|  &emsp;  |  &emsp;  |  &emsp;  |-- cart-mini-serivce\
|  &emsp;  |  &emsp;  |  &emsp;  |  &emsp;  |-- Dockerfile\
|-- Payment\
|  &emsp;  |-- Walgreens-Replica\
|  &emsp;  |  &emsp;  |-- Docker\
|  &emsp;  |  &emsp;  |  &emsp;  |-- payment-mini-serivce\
|  &emsp;  |  &emsp;  |  &emsp;  |  &emsp;  |-- Dockerfile\
|-- Product\
|  &emsp;  |-- Walgreens-Replica\
|  &emsp;  |  &emsp;  |-- Docker\
|  &emsp;  |  &emsp;  |  &emsp;  |-- product-mini-serivce\
|  &emsp;  |  &emsp;  |  &emsp;  |  &emsp;  |-- Dockerfile\
|-- User\
| &emsp;   |-- Walgreens-Replica\
|  &emsp;  |  &emsp;  |-- Docker\
|  &emsp;  |  &emsp;  |  &emsp;  |-- userManagement-mini-serivce\
|  &emsp;  |  &emsp;  |  &emsp;  |  &emsp;  |-- Dockerfile\
|-- External Docker\
|-- Kubernetes
