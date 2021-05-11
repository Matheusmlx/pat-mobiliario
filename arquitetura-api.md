### 1. Objetivo

Este documento tem como objetivo repassar o conhecimento técnico da arquitetura adotada no back-end do produto Patrimônio Mobiliário.

### 2. Sobre a arquitetura

Este projeto é baseado nos princípios da **clean architecture (C.A)** descritos por Martin (2018).  
Dividida em quatro camadas (application, domain, entrypoint, gateway), a arquitetura adotada neste projeto impõe algumas restrições de implementação que devem ser seguidas para garantir que a manutenção e a evolução do software seja feita de forma rápida e eficaz.

### 3. Descrição das camadas

A seguir uma breve descrição de cada camada, suas tecnologias e responsabilidades.

#### 3.1 Application
Trata-se da camada de infraestrutura do sistema. É responsável por importar as demais camadas
do back-end por meio de dependências do Maven, portanto ao visualizar o arquivo **pom.xml** presente nesta camada, pode-se observar as outras camadas como dependência:

```
  <dependencies>
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>patrimonio-mobiliario-api-domain</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>patrimonio-mobiliario-api-entrypoint</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>patrimonio-mobiliario-api-gateway</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
```

Responsabilidades:

* Iniciar a aplicação Spring por meio do arquivo **PatrimonioMobiliarioApiApplication.java**;
* Realizar a injeção de dependências por meio das factories.

#### 3.2 Domain

Nesta camada encontra-se as regras de negócio do sistema.
Diferente da camada de aplicação, esta camada não possui o framework Spring, portanto sua implementação se restringe apenas a linguagem Java e algumas ferramentas de teste (ex: junit, mockito). Assim, caso seja necessário alterar o Spring em sua versão ou migrar para outro framework, a camada que contém a regra de negócio não é afetada.

Responsabilidades:

* Conter a regra de negócio do projeto.
* Implementar os casos de uso.
* Implementar as entidades.
* Implementar **apenas as interfaces** dos DataProviders.

#### 3.3 Entrypoint

Semelhante a camada de interface. Esta é a camada **REST** da aplicação.

Responsabilidades:

* Implementar os controllers.
* Implementar o tratamento global de exceções da aplicação.

#### 3.4 Gateway

Da mesma forma que a camada Entrypoint é reponsável por se comunicar com a aplicação de UI, o Gateway é responsável por prover a comunicação da API com o banco de dados e API's de terceiros.

Responsabilidades:

* Implementar o mapeamento objeto-relacional.
* Implementar os DataProviders utilizando as interfaces da camada de domínio.
* Implementar a comunicação com o banco de dados.
* Implementar a comunicação com outros sistemas.

#### Referências
MARTIN, Robert C. Clean architecture: a craftsman's guide to software structure and design. Prentice Hall, 2018.

-----

© Copyright 2020 - All rights reserved | Todos os direitos Reservados

__AZ Tecnologia em Gestão__
