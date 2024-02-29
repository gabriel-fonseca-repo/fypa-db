package com.fypa.projdb.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtil {

    public static File getFileFromRsc(String file) {
        try {

            ClassLoader classLoader = FileUtil.class.getClassLoader();
            URL resource = classLoader.getResource(file);

            if (resource == null) {
                throw new IllegalArgumentException("Arquivo '" + file + "'não encontrado!" + file);
            }

            return new File(resource.toURI());

        } catch (URISyntaxException ignored) {
            throw new RuntimeException("URL do arquivo " + file + " mal formatada!");
        }
    }

}
