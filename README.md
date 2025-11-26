# ZenoTime - Sistema de Folha de Ponto

## 📋 Descrição

ZenoTime é um sistema completo de gestão de folha de ponto desenvolvido como trabalho final do curso. O sistema permite que empresas gerenciem funcionários, projetos, times e sprints, com controle de ponto integrado e análise de produtividade através de dashboards avançados.

### Público-alvo
Empresas que precisam de controle de ponto integrado com gestão de projetos e análise de produtividade.

## 🏗️ Arquitetura

O sistema é composto por 3 serviços Spring Boot e um frontend React:

### Serviços Backend

1. **zenotime-core-service** (Porta 8080)
   - Serviço principal com todas as APIs REST
   - Autenticação JWT
   - CRUD de todas as entidades
   - Produção de mensagens RabbitMQ

2. **zenotime-report-service** (Porta 8081)
   - Consome mensagens de registros de ponto
   - Gera relatórios CSV diários (agendado)
   - Exportação de relatórios (PDF, Excel, CSV)

3. **zenotime-notification-service** (Porta 8082)
   - Consome eventos críticos (solicitações, sprints)
   - Alerta administradores sobre eventos que requerem atenção

### Frontend

- **zenotime-frontend** (Porta 3000)
  - Interface React com Material-UI
  - Dashboard administrativo com gráficos
  - CRUD completo de todas as entidades
  - Registro de ponto para funcionários

## 🛠️ Tecnologias

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security (JWT)
- Spring AMQP (RabbitMQ)
- MySQL 8.0
- Maven

### Frontend
- React 18
- React Router
- Axios
- Recharts (gráficos)
- Material-UI
- React Query

### Infraestrutura
- Docker
- Docker Compose
- RabbitMQ
- MySQL

## 📦 Estrutura do Projeto

```
ZenoTime/
├── zenotime-core-service/       # Serviço principal
├── zenotime-report-service/      # Serviço de relatórios
├── zenotime-notification-service/ # Serviço de notificações
├── zenotime-frontend/           # Frontend React
├── docker-compose.yml           # Orquestração dos serviços
└── README.md                    # Este arquivo
```

## 🚀 Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Maven (para build local, opcional)
- Node.js (para desenvolvimento do frontend, opcional)

### Executando com Docker Compose

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd ZenoTime
```

2. Execute o docker-compose:
```bash
docker-compose up -d
```

3. Aguarde alguns segundos para todos os serviços iniciarem.

4. Acesse:
   - Frontend: http://localhost:3000
   - API Core: http://localhost:8080
   - RabbitMQ Management: http://localhost:15672 (admin/admin)
   - MySQL: localhost:3306 (zenotime/zenotime)

### Executando Localmente (Desenvolvimento)

#### Backend

1. Certifique-se de que MySQL e RabbitMQ estão rodando:
```bash
docker-compose up mysql rabbitmq -d
```

2. Entre em cada serviço e execute:
```bash
cd zenotime-core-service
mvn spring-boot:run
```

#### Frontend

```bash
cd zenotime-frontend
npm install
npm start
```

## 📡 Portas dos Serviços

- **8080**: Core Service (API principal)
- **8081**: Report Service
- **8082**: Notification Service
- **3000**: Frontend React
- **3306**: MySQL
- **5672**: RabbitMQ
- **15672**: RabbitMQ Management UI

## 🔐 Autenticação

O sistema utiliza JWT para autenticação. Para fazer login:

1. Acesse http://localhost:3000/login
2. Use as credenciais:
   - Email: admin@zenotime.com
   - Senha: (criar usuário inicial via API ou banco)

### Criando Usuário Inicial

Você pode criar um usuário inicial diretamente no banco de dados ou via API:

```bash
POST http://localhost:8080/api/usuarios
{
  "nome": "Administrador",
  "email": "admin@zenotime.com",
  "senha": "senha123",
  "tipo": "ADMINISTRADOR",
  "ativo": true
}
```

## 📊 Mensageria (RabbitMQ)

### Filas Configuradas

- `ponto.registrado`: Quando funcionário registra entrada/saída
- `solicitacao.criada`: Solicitações de férias/atestado
- `sprint.criada`: Nova sprint criada
- `horas.administrativas`: Registro de horas administrativas

### Fluxos

1. **Registro de Ponto** → `ponto.registrado` → report-service (gera CSV diário)
2. **Solicitação de Férias/Atestado** → `solicitacao.criada` → notification-service (alerta admin)
3. **Criação de Sprint** → `sprint.criada` → notification-service (notifica times)

## 📈 Features Principais

### Para Funcionários
- Registro de ponto (entrada/saída)
- Visualização de registros pessoais
- Solicitação de férias e atestados
- Visualização de projetos e times atribuídos

### Para Administradores
- Dashboard analítico com gráficos:
  - Produtividade ao longo do tempo
  - Distribuição de horas por projeto
  - Comparativo de produtividade entre times
- CRUD completo de:
  - Empresas
  - Projetos
  - Times
  - Sprints
  - Funcionários
- Aprovação/rejeição de solicitações
- Exportação de relatórios (PDF, Excel, CSV)

## 🗄️ Modelo de Dados

### Entidades Principais

- **Usuario**: Funcionários e administradores
- **Empresa**: Empresas clientes
- **Projeto**: Projetos dentro de empresas
- **Time**: Times dentro de projetos
- **Sprint**: Sprints de times
- **RegistroPonto**: Registros de entrada/saída
- **Solicitacao**: Solicitações de férias/atestados

### Relacionamentos N:N

- Funcionário ↔ Empresa (com datas de início/fim)
- Funcionário ↔ Projeto (com datas de início/fim)
- Funcionário ↔ Time (com datas de início/fim)

## 🧪 Testando a API

### Exemplo de Requisições

#### Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@zenotime.com",
  "senha": "senha123"
}
```

#### Registrar Ponto
```bash
POST http://localhost:8080/api/ponto/entrada
Authorization: Bearer <token>
Content-Type: application/json

{
  "dataHoraEntrada": "2024-01-15T08:00:00",
  "projetoId": 1,
  "observacoes": "Trabalhando no projeto X"
}
```

## 📝 Endpoints da API

### Autenticação
- `POST /api/auth/login` - Login

### Usuários (Admin)
- `GET /api/usuarios` - Listar
- `POST /api/usuarios` - Criar
- `PUT /api/usuarios/{id}` - Atualizar
- `DELETE /api/usuarios/{id}` - Excluir

### Empresas (Admin)
- `GET /api/empresas` - Listar
- `POST /api/empresas` - Criar
- `PUT /api/empresas/{id}` - Atualizar
- `DELETE /api/empresas/{id}` - Excluir

### Projetos (Admin)
- `GET /api/projetos` - Listar
- `POST /api/projetos` - Criar
- `PUT /api/projetos/{id}` - Atualizar
- `DELETE /api/projetos/{id}` - Excluir

### Times (Admin)
- `GET /api/times` - Listar
- `POST /api/times` - Criar
- `PUT /api/times/{id}` - Atualizar
- `DELETE /api/times/{id}` - Excluir

### Sprints (Admin)
- `GET /api/sprints` - Listar
- `POST /api/sprints` - Criar
- `PUT /api/sprints/{id}` - Atualizar
- `DELETE /api/sprints/{id}` - Excluir

### Registro de Ponto
- `GET /api/ponto` - Listar registros do funcionário
- `POST /api/ponto/entrada` - Registrar entrada
- `POST /api/ponto/saida/{registroId}` - Registrar saída

### Solicitações
- `GET /api/solicitacoes` - Listar
- `POST /api/solicitacoes` - Criar (Funcionário)
- `PUT /api/solicitacoes/{id}/aprovar` - Aprovar (Admin)
- `PUT /api/solicitacoes/{id}/rejeitar` - Rejeitar (Admin)

### Relatórios (Admin)
- `GET /api/relatorios/export?formato=csv|pdf|excel&periodo=...` - Exportar

## 🐳 Docker

### Build Manual

Para fazer build manual dos serviços:

```bash
# Core Service
cd zenotime-core-service
mvn clean package
docker build -t zenotime-core .

# Report Service
cd zenotime-report-service
mvn clean package
docker build -t zenotime-report .

# Notification Service
cd zenotime-notification-service
mvn clean package
docker build -t zenotime-notification .

# Frontend
cd zenotime-frontend
npm run build
docker build -t zenotime-frontend .
```

## 🔧 Configuração

### Variáveis de Ambiente

As configurações podem ser ajustadas no `docker-compose.yml` ou nos arquivos `application.yml` de cada serviço.

### Banco de Dados

O banco de dados é criado automaticamente na primeira execução. O schema é gerado via Hibernate (`ddl-auto: update`).

## 📚 Desenvolvimento

### Estrutura de Camadas

Cada serviço Spring Boot segue a arquitetura em camadas:

- **Controller**: Endpoints REST
- **Service**: Lógica de negócio
- **Repository**: Acesso a dados
- **Entity**: Entidades JPA
- **DTO**: Objetos de transferência
- **Config**: Configurações (RabbitMQ, Security, etc.)

## 🐛 Troubleshooting

### Serviços não iniciam
- Verifique se as portas estão livres
- Verifique os logs: `docker-compose logs <servico>`

### Erro de conexão com banco
- Aguarde alguns segundos para o MySQL inicializar completamente
- Verifique se o container do MySQL está rodando: `docker ps`

### Erro de conexão com RabbitMQ
- Verifique se o RabbitMQ está acessível: http://localhost:15672
- Credenciais padrão: admin/admin

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico.

## 👥 Integrantes

- **João Vítor Monteiro** (Desenvolvimento individual)

---

**ZenoTime** - Sistema de Folha de Ponto com Spring Boot, RabbitMQ e React

