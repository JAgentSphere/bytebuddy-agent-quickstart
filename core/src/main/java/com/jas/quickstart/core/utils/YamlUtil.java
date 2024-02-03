package com.jas.quickstart.core.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @author ReaJason
 * @since 2024/1/29
 */
public class YamlUtil {

    public static Map<String, Object> load(InputStream is) {
        Yaml yaml = new Yaml();
        return yaml.load(is);
    }

    public static <T> T loadAs(InputStream is, Class<T> clazz) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(is, clazz);
    }
}
