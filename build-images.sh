#! /bin/bash

dirs=("veterinaria-naming-server" "api-gateway" "user-service" "especie-service" "animal-service"  "cliente-service" "consulta-service" "veterinario-service")

for dir in ${dirs[*]}; do
	echo "###############################"
	echo "Build image on ${dir}"
	mvn -ff -f "${dir}" clean spring-boot:build-image
	mvn -f "${dir}" clean
	echo "###############################"
done
