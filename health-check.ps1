Write-Host ""
Write-Host "==============================="
Write-Host " BANK APP FULL HEALTH CHECK"
Write-Host "==============================="
Write-Host ""

# -----------------------------
# DOCKER
# -----------------------------

Write-Host "Docker containers:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# AUTH
# -----------------------------

Write-Host "AUTH SERVICE HEALTH"

curl http://localhost:8082/actuator/health

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# BANK API
# -----------------------------

Write-Host "BANK API HEALTH"

curl http://localhost:8081/actuator/health

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# GATEWAY
# -----------------------------

Write-Host "API GATEWAY HEALTH"

curl http://localhost:8080/actuator/health

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# API CHECKS
# -----------------------------

Write-Host "AUTH LOGIN TEST"

$body = @{
    username = "ivan123"
    password = "123456"
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "http://localhost:8082/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

Write-Host "LOGIN SUCCESS"

$token = $response.accessToken

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# ACCOUNTS
# -----------------------------

Write-Host "ACCOUNTS TEST"

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/accounts" `
    -Headers @{
        Authorization = "Bearer $token"
    }

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# CATEGORIES
# -----------------------------

Write-Host "CATEGORIES TEST"

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/categories" `
    -Headers @{
        Authorization = "Bearer $token"
    }

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# TRANSACTIONS
# -----------------------------

Write-Host "TRANSACTIONS TEST"

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/transactions" `
    -Headers @{
        Authorization = "Bearer $token"
    }

Write-Host ""
Write-Host "-------------------------------"

# -----------------------------
# STATS
# -----------------------------

Write-Host "STATS TEST"

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/stats/transactions" `
    -Headers @{
        Authorization = "Bearer $token"
    }

Write-Host ""
Write-Host "==============================="
Write-Host " ALL CHECKS FINISHED"
Write-Host "==============================="