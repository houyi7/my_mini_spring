package com.springframework.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemResource implements Resource{
    private final String filePath;

    public FileSystemResource(String path) {
        filePath = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try{
            Path path=new File(filePath).toPath();
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());

        }
    }
}
