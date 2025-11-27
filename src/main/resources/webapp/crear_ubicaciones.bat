@echo off
set URL=http://localhost:8080/ubicacion

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Estacionamiento\",\"energia\":50,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":0.0,\"longitud\":0.0},{\"latitud\":0.0,\"longitud\":1.0},{\"latitud\":1.0,\"longitud\":1.0},{\"latitud\":1.0,\"longitud\":0.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Edificio Espora\",\"energia\":60,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":0.0,\"longitud\":2.0},{\"latitud\":0.0,\"longitud\":3.0},{\"latitud\":1.0,\"longitud\":3.0},{\"latitud\":1.0,\"longitud\":2.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"SUM\",\"energia\":55,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":1.5,\"longitud\":2.0},{\"latitud\":1.5,\"longitud\":3.0},{\"latitud\":2.5,\"longitud\":3.0},{\"latitud\":2.5,\"longitud\":2.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Sector 3\",\"energia\":50,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":1.5,\"longitud\":3.5},{\"latitud\":1.5,\"longitud\":4.5},{\"latitud\":2.5,\"longitud\":4.5},{\"latitud\":2.5,\"longitud\":3.5}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Comedor\",\"energia\":60,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":0.0,\"longitud\":4.0},{\"latitud\":0.0,\"longitud\":4.5},{\"latitud\":1.8,\"longitud\":4.5},{\"latitud\":1.8,\"longitud\":4.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Rosa de los Vientos\",\"energia\":70,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":2.0,\"longitud\":4.0},{\"latitud\":2.0,\"longitud\":5.5},{\"latitud\":3.0,\"longitud\":5.5},{\"latitud\":3.0,\"longitud\":4.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Sector 5\",\"energia\":50,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":3.2,\"longitud\":4.0},{\"latitud\":3.2,\"longitud\":4.5},{\"latitud\":5.0,\"longitud\":4.5},{\"latitud\":5.0,\"longitud\":4.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Aulas Norte\",\"energia\":60,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":-1.0,\"longitud\":6.0},{\"latitud\":-1.0,\"longitud\":7.0},{\"latitud\":1.0,\"longitud\":7.0},{\"latitud\":1.0,\"longitud\":6.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Aulas Sur\",\"energia\":60,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":2.0,\"longitud\":6.0},{\"latitud\":2.0,\"longitud\":7.0},{\"latitud\":4.0,\"longitud\":7.0},{\"latitud\":4.0,\"longitud\":6.0}]}"

curl -X POST %URL% -H "Content-Type: application/json" -d "{\"nombre\":\"Dptos\",\"energia\":55,\"tipo\":\"CEMENTERIO\",\"coordenadas\":[{\"latitud\":0.0,\"longitud\":8.0},{\"latitud\":0.0,\"longitud\":9.0},{\"latitud\":3.0,\"longitud\":9.0},{\"latitud\":3.0,\"longitud\":8.0}]}"

echo Todas las ubicaciones creadas