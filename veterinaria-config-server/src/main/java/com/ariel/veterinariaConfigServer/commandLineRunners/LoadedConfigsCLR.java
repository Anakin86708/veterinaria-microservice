package com.ariel.veterinariaConfigServer.commandLineRunners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LoadedConfigsCLR implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);


    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.cloud.config.server.git.uri}")
    private String uriConfigServer;

    @Override
    public void run(String... args) throws Exception {
        String path = null;
        Path namePath = null;
        try {
            Resource resource = resourceLoader.getResource(uriConfigServer);
            path = resource.getFile().getPath();
            namePath = Paths.get(path);


            logger.info(String.format("Path: %s", path));
            logger.info("Available properties files:");

            try (Stream<Path> walk = Files.walk(namePath)) {
                List<String> files = walk.map(Path::toFile)
                        .filter(File::isFile)
                        .map(File::getName)
                        .filter(name -> name.endsWith(".properties"))
                        .collect(Collectors.toList());
                files.forEach(logger::info);
            } catch (NoSuchFileException e) {
                logger.error(e.getLocalizedMessage());
            }

        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }
}
