package org.example.config;

import org.example.holder.JavaClassesHolder;
import org.example.loader.JarLibraryClassLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public JavaClassesHolder holder() {
        return new JavaClassesHolder();
    }

    @Bean
    public JarLibraryClassLoader loader() {
        return new JarLibraryClassLoader(holder());
    }
}
