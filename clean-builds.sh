#! /bin/bash

dirs=("veterinaria-naming-server" "animal-service" "api-gateway" "cliente-service" "consulta-service" "especie-service" "veterinario-service")

for dir in ${dirs[*]}; do
	mvn -f "${dir}" clean
done
