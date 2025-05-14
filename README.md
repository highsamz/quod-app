# 📸 Sistema de Validação Biométrica e Documental – QUOD

Este projeto é uma aplicação backend desenvolvida em **Java (Spring Boot)** para simular a captura e validação de **biometria facial**, **biometria digital** e **documentos**, com foco na **detecção de fraudes**. O sistema realiza análises de imagens, valida metadados e envia notificações para o sistema interno da **QUOD** em casos de sucesso ou fraude.

---

## ✅ Funcionalidades

- **Biometria Facial**
    - Simula a captura de imagem do rosto.
    - Realiza validação por comparação com base de dados.

- **Biometria Digital**
    - Simula a captura de impressões digitais.
    - Realiza validação da digital com imagens salvas na base.

- **Análise Documental (Documentoscopia)**
    - Validação de documentos por imagem.
    - Comparação facial com a foto do documento.
    - Verificação de autenticidade e integridade.

- **Validação de Imagens**
    - **Básica:** formato, tamanho, qualidade, metadados (data, local, fabricante).
    - **Avançada:** simulação de fraudes como Deepfake, máscaras e foto de foto.

- **Notificação**
    - Em caso de fraude detectada: envia requisição HTTP para sistema de monitoramento interno.
    - Em caso de sucesso: registra apenas a validação, sem notificação.

- **Persistência de Dados**
    - Banco de dados **MongoDB** (NoSQL).
    - Todos os registros são armazenados, com **prioridade para casos de fraude** para fins de auditoria.

- **Aviso importante**
  - Adicionar imagens com o tamanho de até 500kb
  - Estamos enviando também imagens para testes. Disponíveis na pasta src/main/resources/imagensTeste

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Web / WebFlux
- Spring Data MongoDB
- Lombok
- Metadata Extractor (`com.drewnoakes`)
- OpenCV 4.7.0
- Swagger OpenAPI (`springdoc`)
- MongoDB (Docker)

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos

- Java 21+
- Maven
- Docker (opcional para MongoDB)
- Sistema Operacional compatível com a biblioteca nativa do OpenCV

### 1. Subir o MongoDB via Docker (opcional)

```bash
docker run --name mongo-test -d -p 27017:27017 mongo

### 2 Deve buildar o projeto
mvn clean package

### Rodar o script abaixo:
java -Djava.library.path="libs/opencv_java470.dll" -jar target/quod-app-0.0.1-SNAPSHOT.jar