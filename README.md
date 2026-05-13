# 🏋️ EasyGym — Plataforma de Gestão de Academias

Sistema web completo para gerenciamento de academias, desenvolvido em Java com Spring Boot.

## 👥 Integrantes
- Luis Felipe Pontes 
- Erick Yudi Nishimura Arakaki 
- Felipe Sperati Vieira da Costa 
- Leonardo Barroso de Almeida 

## 🚀 Como executar

### Pré-requisitos
- **JDK 21** instalado → https://adoptium.net (escolha JDK 21 LTS)
- Conexão com internet (para baixar o Maven automaticamente na primeira vez)

### Passos
1. Clone o repositório:
git clone https://github.com/leonardoba04/EasyGym.git

2. Entre na pasta e execute:
cd EasyGym
set JAVA_HOME=%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-21.0.11.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
mvn clean spring-boot:run


3. Acesse no navegador: **http://localhost:8080**

### Credenciais de acesso

| Perfil | Login | Senha |
|---|---|---|
| Administrador | admin@fitzone.com | admin123 |
| Funcionário | joao@fitzone.com | func123 |
| Aluno | MAT00001 | 123456 |
| Aluno | MAT00002 | 123456 |

## 🛠️ Tecnologias
- Java 21
- Spring Boot 3.2
- Spring Security
- Thymeleaf
- HTML/CSS/JavaScript

## 📋 Funcionalidades
- **Login** com 3 níveis de acesso: Admin, Funcionário e Aluno
- **Dashboard** com métricas em tempo real
- **Gestão de Alunos** com IMC, plano e histórico
- **Planos** com benefícios e contagem de alunos
- **Modalidades** com horários e controle de ocupação
- **Professores** por modalidade e unidade
- **Check-in** com validação de pagamento
- **Portal do Aluno** com frequência, horários e chat com professores
- **Configuração da Academia** com logo, cores e filiais
