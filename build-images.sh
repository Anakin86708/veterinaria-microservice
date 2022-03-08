#! /bin/bash

dirs=("veterinaria-naming-server" "animal-service" "api-gateway" "cliente-service" "consulta-service" "especie-service" "veterinario-service")

for dir in ${dirs[*]}; do
	echo "###############################"
	echo "Build image on ${dir}"
	mvn -f "${dir}" clean
	mvn -ff -f "${dir}" spring-boot:build-image
	mvn -f "${dir}" clean
	echo "###############################"
done
