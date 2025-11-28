# TP Investigación - Redis


## Funcionalidad

Los espiritus demoniacos ahora pueden poseer a los mediums. 

Los mediums puede ilumnar a los espiritus demoniacos.

## NightBringer
Un Night Bringer se encarga de spawnear espiritus demoniacos, de ellos se sabe lo siguiente.

- Su nombre que debe ser unico.
- Los espiritus que spawneó.

## LightBringer
un Light Bringer es el encargado de iluminar a los mediums poseidos por un espiritu, de ellos se sabe lo siguiente.

- Su nombre que debe ser unico.
- La fuerza de ataque con la cual "ilumina" al medium poseido.

## Servicios

Se debe agregar los siguientes servicios

## NightBringerService

- Métodos CRUD + `recuperarTodos`.

- `Espiritu spawnearEspirituEnUbicacion(Long nightbringerId, Long ubicacionId, String nombreEspiritu);` - El night bringer spawnea un espiritu con ese nombre en la ubicación dada.

## LightBringerService

- Métodos CRUD + `recuperarTodos`.

Y se debe actualizar los siguientes servicios

## MediumService

- adentrarseAlNether(Long mediumId) El medium se adentra al Nether

## EspirituService

- poseerMedium(Long espirituId, Long mediumId) El espiritu posee al medium

## Se pide:
- Implementar las nuevas interfaces
- Actualizar los servicios MediumService y EspirituService
- Crear una clase RedisConfig que gestione las conexiones con Redis, incluyendo la serialización y deserialización de datos en formato JSON
- Crear una clase RedisPubSubConfig que configure el sistema de publicación/suscripción (Pub/Sub) de Redis, registrando listeners para manejar mensajes publicados en los canales ubicacion:*:espiritus y ubicacion:*:mediums
- Creen test unitarios para cada unidad de código entregada que prueben todas las funcionalidades pedidas, con casos favorables y desfavorables.