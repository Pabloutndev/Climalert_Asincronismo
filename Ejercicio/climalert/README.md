# Climalert

Sistema de **monitoreo climatico y envio automatico de alertas** (Java 21 + Spring Boot).

Servicio autonomo, **sin interfaz grafica**, que:

1. Cada **5 minutos** consulta el clima actual de una ubicacion fija a **WeatherAPI**
   (`/current.json`) y lo guarda como registro historico.
2. Cada **1 minuto** analiza el ultimo dato y, si detecta una condicion critica
   (**temperatura > 35 °C** y **humedad > 60 %**), genera una alerta.
3. Al generarse la alerta, envia por **correo** el detalle del clima a
   `admin@clima.com`, `emergencias@clima.com` y `meteorologia@clima.com`.

## Estructura

```
config/      -> Properties y bean del RestClient
dto/         -> Mapeo de la respuesta de WeatherAPI
model/       -> Entidad RegistroClima (historico)
repository/  -> Acceso a datos (Spring Data JPA + H2)
service/     -> WeatherService, AlertaService, EmailService
scheduler/   -> ClimaScheduler (las dos tareas programadas)
```

## Requisitos

- Java 21
- Maven (o abrir el `pom.xml` en IntelliJ IDEA, que lo gestiona solo)
- API key de [WeatherAPI](https://www.weatherapi.com/)

## Configuracion

En `src/main/resources/application.properties`:

- `climalert.weather.api-key`: API key de WeatherAPI.
- `climalert.weather.location`: ubicacion a consultar.
- `spring.mail.*`: datos del servidor SMTP para el envio de correos.

## Ejecucion

Desde IntelliJ: *Run* sobre `ClimalertApplication`.

Por consola (con Maven instalado):

```bash
mvn spring-boot:run
```

## ⚠️ Aviso sobre el envio de correos

El envio se realiza contra un servidor **SMTP real**. Para que el correo salga
efectivamente hay que completar en `application.properties`:

```properties
spring.mail.username=tu-cuenta@gmail.com
spring.mail.password=tu-app-password
```

(Con Gmail se requiere una *App Password* con verificacion en dos pasos activada;
como alternativa para pruebas puede usarse una cuenta de [Mailtrap](https://mailtrap.io/).)

Si estas credenciales no se completan, la alerta igual se **detecta y se registra en el
log**, pero el envio del correo fallara con un error de autenticacion SMTP.

## Verificacion antes de la entrega

1. **Arranque e integracion.** Abrir en IntelliJ y ejecutar `ClimalertApplication`.
   En el log debe aparecer, a los pocos segundos:
   ```
   Clima consultado y guardado -> Buenos Aires | 18.0 C | 55% humedad | Partly cloudy
   ```
   Esto confirma que compila, que la API key funciona y que se guarda el historico.

2. **Alerta.** Para forzar una condicion critica sin esperar clima extremo, bajar
   temporalmente en `application.properties`:
   ```properties
   climalert.alerta.umbral-temperatura=0
   climalert.alerta.umbral-humedad=0
   climalert.scheduling.fetch-rate-ms=20000
   climalert.scheduling.alert-rate-ms=10000
   ```
   Volver a ejecutar y verificar en el log:
   ```
   Condicion CRITICA detectada -> ...
   ```
   Al terminar, **restaurar** los valores reales (`35`, `60`, `300000`, `60000`).

3. **Repositorio.** Subir el proyecto a un repositorio **publico** de GitHub y abrir el
   link en una ventana de incognito para confirmar que se ve sin iniciar sesion.

