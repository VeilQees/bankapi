# Bank API — Fullstack Banking Application

## Overview

Fullstack banking application built with microservice architecture.

Project includes:

* user registration and authentication
* JWT authorization
* account management
* deposits
* money transfers between accounts
* transaction history
* categories
* statistics
* API Gateway routing
* Dockerized infrastructure

---

# Tech Stack

## Backend

* Java 17
* Spring Boot 3
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Redis
* Kafka

## Frontend

* React
* TypeScript
* Axios
* Vite

## Infrastructure

* Docker Compose
* API Gateway
* Redis
* PostgreSQL
* Kafka
* Zookeeper

---

# Features

## Authentication

* Register user
* Login
* JWT access token
* Protected API routes

---

## Accounts

* Create account
* View all user accounts
* Deposit money
* Transfer money between accounts

---

## Transactions

* Transaction history
* Transfer records
* Category support

---

## Statistics

* Income / expense statistics
* Financial overview

---

# Run Project

## Start all containers

```bash
docker compose up -d
```

---

## Run frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173/login
```

---

# Health Check

Run:

```powershell
.\health-check.ps1
```

Checks:

* Docker containers
* auth-service
* bankapi
* api-gateway
* login
* accounts
* categories
* transactions
* stats

---

# API Endpoints

## Auth

```http
POST /api/auth/register
POST /api/auth/login
```

---

## Accounts

```http
GET /api/accounts
POST /api/accounts
POST /api/accounts/deposit
POST /api/accounts/transfer
```

---

## Transactions

```http
GET /api/transactions
```

---

## Categories

```http
GET /api/categories
```

---

## Statistics

```http
GET /api/stats/transactions
```

---

# Project Status

## Stable Release: v1.0.0

Current status:

* auth-service working
* bankapi working
* api-gateway working
* docker compose working
* JWT working
* PostgreSQL working
* Redis working
* frontend login working
* dashboard working

---

# Author

Personal Fullstack Banking Pet Project
