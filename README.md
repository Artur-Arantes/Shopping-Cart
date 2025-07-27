# Shopping Cart

## Descrição

Este projeto foi desenvolvido como parte de um teste prático. Ele consiste em uma aplicação que simula um carrinho de compras, permitindo a criação, edição e gerenciamento de usuários, produtos e carrinhos.

A aplicação conta com autenticação para geração de tokens e oferece um **Swagger UI** para facilitar a exploração dos endpoints.

A aplicação foi construída utilizando **Spring Boot** e utiliza **MySQL** como banco de dados, com suporte para execução local via Docker.

O Swagger da aplicação de produção pode ser acessado [aqui](https://liven-shopping-cart-e1003610f15e.herokuapp.com/swagger-ui/index.html).

---

## Funcionalidades

- **Usuários**
    - Criar, deletar e buscar usuários.
    - Cada usuário possui permissões específicas.
    - Endpoint de autenticação (`/auth`) para geração de tokens.

- **Produtos**
    - Criar, editar e buscar produtos.

- **Carrinhos**
    - Criar carrinhos.
    - Adicionar produtos aos carrinhos.
    - Realizar o checkout de um carrinho, nesse endpoint existe controle de concorrência por versionamento.

---

## Executando o projeto localmente

### Pré-requisitos

- **Docker** e **Docker Compose** instalados.

### Como executar pelo docker

1. Clone este repositório:
   ```bash
   git clone <URL-DO-REPOSITORIO>
   cd shopping-cart

2. Rode o comando do Maven para clean e package   
      ```bash
    mvn clean
    mvn package
   
3. Certifique-se que exista um jar na pasta /target Execute o comando:
   ```bash
   docker-compose up --build
   
Isso inicializará a aplicação localmente, sem a necessidade de configurar o banco de dados manualmente.

---
### Como executar pela IDE
####  Váriaves de ambiente
        DB_USER
        DB_URL
        DB_KEY
        TOKEN_KEY
        CART_EXPIRE_IN 


### Tecnologias e Ferramentas
#### Principais Tecnologias
- #####  Spring Boot
- ##### MySQL
- ##### Groovy
- ##### JAVA 17
- ##### Docker e Docker Compose
- ##### Spock
- ##### Swagger
- ##### Flyway


### Outras ferramentas usadas : 
- Spring Boot Starter Data JPA.
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Lombok
- Hibernate Core
- Auth0 Java JWT
- Testcontainers
- Rest Assured
- AssertJ 

---
#### Testes
- Testes Unitários
- Testes de Integração
---
### Deploy
A aplicação foi deployada em produção no Heroku e pode ser acessada visual mente pelo Swagger:
https://liven-shopping-cart-e1003610f15e.herokuapp.com/swagger-ui/index.html

URL da produção: Swagger UI

---
### Pipeline CI/CD
O projeto possui uma pipeline configurada com as seguintes etapas:

Build: Realiza a construção da aplicação e execução dos testes.
Docker Build: Gera a imagem Docker da aplicação e sobe a imgem para o dockerhub :
**arturarantes/shopping_cart:latest**
