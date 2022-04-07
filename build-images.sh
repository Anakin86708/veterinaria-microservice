#! /bin/bash

dirs=("veterinaria-naming-server" "animal-service" "api-gateway" "cliente-service" "consulta-service" "especie-service" "veterinario-service")

trap "exit" INT
for dir in ${dirs[*]}; do
	echo "###############################"
	echo "Build image on ${dir}"
	mvn -ff -f "${dir}" clean spring-boot:build-image
	mvn -f "${dir}" clean
	echo "###############################"
done
