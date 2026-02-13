# AI Logistics Triage Java

Production-ready PoC Spring Boot app for:
- Smart Replenishment (deterministic math + LLM explanation)
- Transport Incident Triage (LLM + RAG with pgvector)

## Prerequisites
- Java 21
- Maven 3.9+
- Docker / Docker Compose
- OPENAI_API_KEY

## Run locally
```bash
cd ai-logistics-triage-java
export OPENAI_API_KEY=your_key
docker compose up --build
```

## Endpoints
- `POST /v1/incidents/triage?promptVersion=v1`
- `POST /v1/replenishment/recommend`

## Sample payloads
### Incident triage
```json
{
  "incidentText": "Shipment delayed 12 hours due to customs check",
  "incidentJson": {"shipmentId": "SHP-123", "route": "BR->US"},
  "sopQuery": "customs delay clearance"
}
```

### Replenishment
```json
{
  "stock": 50,
  "inbound": 20,
  "forecast7d": 140
}
```

## Kubernetes
```bash
kubectl apply -f infra/k8s/secret.yaml
kubectl apply -f infra/k8s/postgres.yaml
kubectl apply -f infra/k8s/deployment.yaml
kubectl apply -f infra/k8s/service.yaml
```
