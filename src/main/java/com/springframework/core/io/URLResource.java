package com.springframework.core.io;

import cn.hutool.db.handler.RsHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLResource implements Resource    {
    private final URL url;

    public URLResource(URL URL) {
        this.url = URL;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection connection=url.openConnection();
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
