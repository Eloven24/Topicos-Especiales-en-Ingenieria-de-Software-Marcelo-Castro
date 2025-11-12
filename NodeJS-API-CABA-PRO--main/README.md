# API CABA PRO - Uso rápido (Postman / cURL)

Este README explica cómo levantar la API Node y cómo probar los endpoints (registro, login y endpoints de árbitros) desde cURL o Postman. La API Node actúa como proxy/cliente hacia una API Spring Boot (configurable vía `SPRING_API_URL`).

## Requisitos
- Node.js 18+ / npm
- (Opcional) Docker

## Preparar y ejecutar
Desde la carpeta `API`:

Instalar dependencias:
```powershell
npm install
```

Ejecutar localmente (usa puerto 3000 por defecto):
```powershell
# Usar la API Spring en localhost:8080
$env:SPRING_API_URL = 'localhost'
node server.js

# O apuntar a la IP pública (por ejemplo 3.237.233.7)
$env:SPRING_API_URL = '3.237.233.7'
node server.js

# Si Spring corre en otro puerto:
$env:SPRING_API_URL = '3.237.233.7:9000'
node server.js
```

En Docker (build & run):
```powershell
# construir imagen (desde carpeta API)
docker build -t caba-pro-node:latest .
# ejecutar y pasar la variable SPRING_API_URL
docker run --rm -p 3000:3000 -e SPRING_API_URL='3.237.233.7' caba-pro-node:latest
```

## Conceptos
- La API Node expone rutas bajo `/api/*` y delega al backend Spring Boot.
- `SPRING_API_URL` puede ser `localhost`, una IP (ej. `3.237.233.7`), o una URL completa. La forma `3.237.233.7` se tratará como `http://3.237.233.7:8080` por defecto.
- La autenticación se realiza usando token JWT proporcionado por la API Spring. Tras login la respuesta incluye `data.token`, y el servidor Node también establecerá una cookie `auth_token` (solo para uso en navegadores).

## Endpoints principales (Node API)
- POST /api/auth/register  -> Registrar árbitro
- POST /api/auth/login     -> Login y obtener token
- POST /api/auth/logout    -> Logout (limpia cookie)

Rutas protegidas (requieren Authorization: Bearer <token> o cookie auth_token):
- GET /api/arbitros/me
- PUT /api/arbitros/me
- GET /api/arbitros/asignaciones
- POST /api/arbitros/asignaciones/:id/aceptar
- POST /api/arbitros/asignaciones/:id/rechazar
- GET /api/arbitros/liquidaciones
- GET /api/arbitros/liquidaciones/:id
- GET /api/arbitros/liquidaciones/:id/pdf
- GET /api/arbitros/partidos
- GET /api/arbitros/partidos/:id
- GET /api/arbitros/dashboard

> Nota: la ruta base expuesta por Node es `http://localhost:3000` cuando se ejecuta localmente.

## Ejemplos con cURL
Sustituye `http://localhost:3000` por la URL donde corra tu servicio Node.

1) Registro
```powershell
curl -X POST "http://localhost:3000/api/auth/register" -H "Content-Type: application/json" -d @- <<'JSON'
{
  "correo": "arbitro@example.com",
  "contrasena": "123456",
  "nombre": "Juan Perez",
  "especialidad": "Futbol",
  "escalafon": "A"
}
JSON
```

2) Login (recibirás token en `data.token`)
```powershell
curl -X POST "http://localhost:3000/api/auth/login" -H "Content-Type: application/json" -d @- <<'JSON'
{
  "correo": "arbitro@example.com",
  "contrasena": "123456"
}
JSON
```
Respuesta esperada (ejemplo):
```json
{
  "success": true,
  "message": "OK",
  "data": {
    "token": "eyJhbGci...",
    "profile": { "correo": "arbitro@example.com", "nombre": "Juan Perez" }
  }
}
```

3) Usar el token para llamar a rutas protegidas
```powershell
# Supongamos que TOKEN contiene el JWT obtenido
$env:TOKEN = 'eyJhbGci...'

curl -H "Authorization: Bearer $env:TOKEN" "http://localhost:3000/api/arbitros/me"
```

4) Aceptar una asignación (POST)
```powershell
curl -X POST -H "Authorization: Bearer $env:TOKEN" "http://localhost:3000/api/arbitros/asignaciones/123/aceptar"
```

5) Descargar PDF de liquidación (se puede recibir stream/binario)
```powershell
curl -H "Authorization: Bearer $env:TOKEN" "http://localhost:3000/api/arbitros/liquidaciones/456/pdf" --output liquidacion-456.pdf
```

## Uso desde Postman
1. Crear un nuevo Environment (por ejemplo `local`) y añadir variable `baseUrl` = `http://localhost:3000`.
2. Crear una request `POST {{baseUrl}}/api/auth/login` con body tipo `raw` JSON y poner las credenciales.
3. En la pestaña `Tests` de la request de login, pega este script para guardar el token en la variable de entorno `token` automáticamente:
```javascript
if (pm.response.code === 200) {
  try {
    const body = pm.response.json();
    if (body && body.data && body.data.token) {
      pm.environment.set('token', body.data.token);
    }
  } catch (e) {
    // ignore
  }
}
```
4. Para las requests protegidas, añade en la pestaña `Authorization` tipo `Bearer Token` y usa `{{token}}` como token, o añade header manual `Authorization: Bearer {{token}}`.
5. Postman guarda cookies por host, así que si prefieres usar la cookie `auth_token` devuelta por el servidor, Postman la mantendrá para las siguientes requests a `{{baseUrl}}`.

## Ejemplo de colección mínima (manual)
- Auth / Register (POST) -> `{{baseUrl}}/api/auth/register` (body JSON)
- Auth / Login (POST) -> `{{baseUrl}}/api/auth/login` (body JSON, Tests script guarda token)
- Arbitro / Me (GET) -> `{{baseUrl}}/api/arbitros/me` (Auth: Bearer {{token}})
- Arbitro / List Asignaciones (GET) -> `{{baseUrl}}/api/arbitros/asignaciones` (Auth required)
- Arbitro / Aceptar Asignacion (POST) -> `{{baseUrl}}/api/arbitros/asignaciones/:id/aceptar`

## Errores comunes y troubleshooting
- Error de conexión al intentar que Node hable con Spring: comprueba `SPRING_API_URL` y que la IP/puerto de Spring estén accesibles desde la máquina donde ejecutas Node (puedes usar `Invoke-WebRequest` / `curl` directamente contra `http://<SPRING_API_URL>/health`).
- Token inválido / 401: asegúrate de usar `Authorization: Bearer <token>` o la cookie `auth_token` y que el token no haya expirado.
- Si usas Docker y Spring está en tu máquina local, asegúrate de que la red entre contenedores/host esté configurada (ej. usar la IP del host o `host.docker.internal` en Docker Desktop).

## ¿Qué más puedo generar?
Puedo generar una colección Postman exportable (.json) con todas las requests ya configuradas y con un script para guardar el token. ¿Quieres que la genere y la añada al repo?

---
Archivo generado automáticamente para ayudar pruebas básicas con la API Node que se comunica con tu API Spring.
