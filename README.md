# Climalert

Sistema de **monitoreo climatico y envio automatico de alertas**, desarrollado como TP de
**Asincronismo** (Java 21 + Spring Boot).

Climalert es un servicio autonomo, **sin interfaz grafica**, que:

1. Cada **5 minutos** consulta el clima actual de una ubicacion fija (CABA) a **WeatherAPI**
   (`/current.json`) y lo guarda como registro historico.
2. Cada **1 minuto** analiza el ultimo dato y, si detecta una condicion critica
   (**temperatura > 35 °C** *y* **humedad > 60 %**), genera una alerta.
3. Al generarse la alerta, envia por **correo** el detalle del clima a
   `admin@clima.com`, `emergencias@clima.com` y `meteorologia@clima.com`.

## Conceptos de asincronismo aplicados

| Concepto | Donde |
|----------|-------|
| Tareas programadas | `@EnableScheduling` + `@Scheduled` en `ClimaScheduler` |
| Ejecucion asincronica | `@EnableAsync` + `@Async` en `EmailService.enviarAlerta` |
| Integracion REST | `RestClient` en `WeatherService` |

## Arquitectura

```
config/      -> Properties y beans (RestClient)
dto/         -> Mapeo de la respuesta de WeatherAPI
model/       -> Entidad RegistroClima (historico)
repository/  -> Acceso a datos (Spring Data JPA + H2)
service/     -> WeatherService, AlertaService, EmailService
scheduler/   -> ClimaScheduler (las dos tareas programadas)
```

## Requisitos

- Java 21
- Maven (o abrir el `pom.xml` directamente en IntelliJ IDEA, que lo gestiona solo)
- Una API key gratuita de [WeatherAPI](https://www.weatherapi.com/)

## Configuracion

Editar `src/main/resources/application.properties` o exportar variables de entorno:

```properties
climalert.weather.api-key=TU_API_KEY
climalert.weather.location=Buenos Aires
```

> Para **ver el flujo rapido** durante una demo, bajar los intervalos, por ejemplo:
> `climalert.scheduling.fetch-rate-ms=20000` y `climalert.scheduling.alert-rate-ms=10000`.

### Correo

Por defecto `climalert.mail.enabled=false`: la alerta se **imprime en el log** (no requiere
servidor SMTP). Para envio real, poner `true` y completar `spring.mail.*`.

## Ejecucion

Desde IntelliJ: boton *Run* sobre `ClimalertApplication`.

Por consola (si tenes Maven instalado):

```bash
mvn spring-boot:run
```

Historico en la consola H2: http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:mem:climalert`, usuario `sa`, sin contrasena).

## Como probar la alerta

Como en CABA rara vez hay >35 °C y >60 % de humedad a la vez, para forzar la alerta se puede:

- Cambiar temporalmente `climalert.weather.location` a una ciudad tropical
  (ej. `Singapore`, `Bangkok`), **o**
- Bajar los umbrales en `application.properties` (ej. `umbral-temperatura=0`,
  `umbral-humedad=0`) para verificar el envio del correo.
