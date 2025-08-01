# 🍕 Pizzaria Web
Este é um sistema completo de uma pizzaria com galeria de slides categorizados, painel administrativo para adicionar novas imagens, e autenticação simples de administrador.

## 🚀 Tecnologias Utilizadas

- **Java 24**
- **MySQL 8+**
- **HTML5, CSS3, JavaScript**
- **Swiper.js** (carrossel responsivo)
- **Maven** (para build e dependências)
- **Spring Boot (Dashboard, Extension Pack e Tools)**

## Extensões no visual studio:

--VS Code - Extensões Instaladas--
- **Java Extension Pack (Microsoft) - Pacote essencial para desenvolvimento Java.**
- **Debugger for Java (Microsoft) - Debugger leve para Java.**
- **Spring Boot Dashboard e Tools (Microsoft / VMware) - Facilita execução e depuração de aplicações Spring Boot.**
- **Gradle for Java (Microsoft) - Suporte para projetos Gradle.**
- **IntelliCode (Microsoft) - AI-assisted development e sugestões inteligentes.**
- **Container Tools & Dev Containers (Microsoft) - Gerenciamento e debug de containers Docker.**
- **Database Client JDBC (Database Client) - Ferramenta para acessar bancos via JDBC.**
- **MySQL (Jun Han) - Gerenciamento específico do MySQL.**
- **SQLTools & SQLTools MySQL/MariaDB/TiDB (Matheus Teixeira) - Integração e execução de queries SQL dentro do VS Code.**
- **SQLite Viewer (Florian Klampfer) - Visualizador SQLite (se necessário).**
- **Jinja (wholroyd) - Suporte para templates Jinja.**

## 🧱 Configuração do Banco de Dados

1. **Crie o banco de dados**:

```sql
CREATE DATABASE bancopizzaria;

CREATE TABLE usuario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100)
);

CREATE TABLE slides (
  id INT AUTO_INCREMENT PRIMARY KEY,
  texto VARCHAR(255),
  imagem LONGBLOB,
  categoria VARCHAR(50)
);

## Exemplo:

INSERT INTO usuario (username, password, email) VALUES ('admin', 'admin123', 'admin@pizzaria.com');

```
## ⚙️ Configuração do Projeto
Conexão com banco no código Java (Conexao.java):

conn = DriverManager.getConnection(
  "jdbc:mysql://localhost:3306/bancopizzaria",
  "root",
  "GustavoZlb@123"
);

🚀 Rodando o Projeto
--Pré-requisitos

Java 24 instalado e configurado (JAVA_HOME)
MySQL rodando localmente
Visual Studio Code com as extensões listadas acima

## ▶️ Como Rodar o Projeto

1. Clone o repositório ou baixe os arquivos.
2. Abra o projeto no **Visual Studio Code**.
3. Certifique-se de que o **MySQL** está rodando localmente.
4. Crie o banco de dados conforme instruções acima.
5. No terminal, navegue até a pasta do projeto:

## cd "C:\Users\usuario\Desktop\pizzaria\spring initializr\adminlogin"
## mvn exec:java -Dexec.mainClass="br.com.pizzaria.adminlogin.TesteConexao"
## mvn spring-boot:run

Para funcionar o localhost não se deve fechar o mvn spring-boot:run

## 📦 Funcionalidades
-- Visualização de slides por categoria
-- Inserção e remoção de slides via painel administrativo
-- Painel administrativo flutuante e arrastável
-- Filtro dinâmico por categoria com atualização instantânea via API REST (/api/slides?categoria=nome)
-- Armazenamento das imagens no banco de dados no formato BLOB

[sitepizzaria.zip](https://github.com/user-attachments/files/21554468/sitepizzaria.zip)

