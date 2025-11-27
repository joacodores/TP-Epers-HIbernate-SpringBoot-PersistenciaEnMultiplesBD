-- Script para limpiar espíritus corruptos (sin NightBringer)
-- Ejecutar en la base de datos PostgreSQL

-- 1. Ver los espíritus corruptos antes de eliminar
SELECT e.id, e.nombre, e.night_bringer_id 
FROM espiritu e 
WHERE e.night_bringer_id IS NULL;

-- 2. Ver espíritus que referencian NightBringers que no existen
SELECT e.id, e.nombre, e.night_bringer_id
FROM espiritu e
LEFT JOIN night_bringer nb ON e.night_bringer_id = nb.id
WHERE e.night_bringer_id IS NOT NULL AND nb.id IS NULL;

-- 3. Eliminar espíritus con night_bringer_id NULL
DELETE FROM espiritu 
WHERE night_bringer_id IS NULL;

-- 4. Eliminar espíritus que referencian NightBringers inexistentes
DELETE FROM espiritu e
WHERE e.night_bringer_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM night_bringer nb WHERE nb.id = e.night_bringer_id
  );

-- 5. Ver NightBringers existentes
SELECT id, nombre FROM night_bringer ORDER BY id;

-- 6. Verificar que quedaron solo espíritus válidos
SELECT COUNT(*) as espiritus_validos FROM espiritu e
WHERE e.night_bringer_id IS NOT NULL
  AND EXISTS (SELECT 1 FROM night_bringer nb WHERE nb.id = e.night_bringer_id);
