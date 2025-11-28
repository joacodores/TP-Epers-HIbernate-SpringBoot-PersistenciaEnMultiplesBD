# TP Investigación - Redis

# Registro oficial de 'Fellowship of the String'

### -- 2025-11-29 16:00hs --

Durante décadas, un extraño período de paz se extendió sobre la región. Ninguna aparición, ningún susurro sobrenatural, ningún rastro de espíritus. El silencio fue tan profundo que muchos llegaron a creer que la oscuridad había desaparecido para siempre.

Hasta que un nuevo rumor comenzó a circular. Se decía que la gloriosa Universidad Nacional de Quilmes había sido construida sobre un terreno cuyo pasado era mucho más antiguo, y mucho más inquietante, de lo que cualquiera imaginaba. Según las historias, allí funcionó un cementerio ancestral que albergaba miles de tumbas y espíritus invisibles al ojo humano. Pero eso no era todo: en ese mismo sitio existió un portal hacia lo que algunos llaman el inframundo, aunque quienes conocemos de estos asuntos preferimos referirnos a él simplemente como el Nether.

Este portal permitía a los espíritus perdidos viajar entre planos, e incluso comunicarse con aquellos mortales dispuestos a escuchar los susurros de la oscuridad. Con el tiempo, surgieron individuos capaces —o al menos convencidos— de establecer contacto con estas entidades. Fanáticos de lo oculto, capaces de invocar espíritus en zonas malditas para perturbar nuestro mundo.
A estos seguidores de la sombra se los conoció, desde entonces, como los temidos NightBringers.

Pero la oscuridad nunca queda sin respuesta. La leyenda cuenta que surgieron seres luminosos, incapaces de destruir a un espíritu directamente, pero dotados del don de purificar y liberar a quienes estos habían poseído. Eran los LightBringers, y la tradición afirma que sólo la luz de los cuatro legendarios podía enfrentar a un espíritu poderoso.

El enfrentamiento entre luz y oscuridad culminó, dicen, con el triunfo de los LightBringers. Lo que hoy escuchamos es una simple leyenda…
O no...?.

## Volviendo al presente

Impulsados por los rumores sobre el oscuro pasado del terreno donde hoy yace lo que conocemos como UNQ, como fanaticos de la persistencia y de la resistencia ante las fuerzas oscuras y ocultistas, decidimos investigar por nuestra cuenta.

Y lo que encontramos fue alarmante...
Lo que parecía un mito tomó forma.

Los NightBringers nunca desaparecieron. Siguen entre nosotros, ocultos como personas comunes, aguardando pacientemente el momento de recuperar su antiguo poder...
Y desde las sombras han conspirado durante siglos, esperando la oportunidad de regresar.

Interesados en el enorme potencial energético del terreno universitario, comenzaron nuevamente a comunicarse con los espíritus…
Y actualmente están invocando entidades en diferentes zonas de la universidad, debilitando los sellos que alguna vez mantuvieron al Nether bajo control.

## Nuestra misión

Ante esta amenaza, nos pusimos manos a la obra. Desarrollamos una interfaz capaz de visualizar en tiempo real la presencia de espíritus dentro del campus de la UNQ.

Y como especialistas en persistencia —tanto de datos como en la lucha contra la oscuridad— logramos desarrollar un metodo para entrar en un plano intermedio entre el Nether y el nuestro, para estudiar de cerca el plano espiritual y comprender a las entidades que atormentan nuestro mundo.

Para esto elaboramos una pocion magica, "poti" para los que saben, una fuente de mana que permite desplazarnos dentro de este plano y visualizar a los espiritus. Pero su uso es delicado:

Por cada movimiento, como bien sabran, como medium consumimos esta mana. Si el mana se agota, podriamos entrar en un terreno desconocido, y las consecuencias podrían ser irreversibles…

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
- Crear una clase RedisPubSubConfig que configure el sistema de publicación/suscripción (Pub/Sub) de Redis, registrando listeners para manejar mensajes publicados en los canales ubicacion:_:espiritus y ubicacion:_:mediums
- Creen test unitarios para cada unidad de código entregada que prueben todas las funcionalidades pedidas, con casos favorables y desfavorables.
