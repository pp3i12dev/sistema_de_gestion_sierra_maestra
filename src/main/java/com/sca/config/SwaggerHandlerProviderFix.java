package com.sca.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
public class SwaggerHandlerProviderFix {

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            private final Logger logger = LoggerFactory.getLogger(SwaggerHandlerProviderFix.class);

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeMappings(bean);
                }
                return bean;
            }

            @SuppressWarnings("unchecked")
            private void customizeMappings(Object provider) {
                try {
                    Field field = provider.getClass().getDeclaredField("handlerMappings");
                    field.setAccessible(true);
                    List<RequestMappingInfoHandlerMapping> mappings = (List<RequestMappingInfoHandlerMapping>) field.get(provider);
                    int original = mappings.size();
                    // Log which mappings have a non-null PatternParser (the ones we will drop)
                    for (RequestMappingInfoHandlerMapping mapping : mappings) {
                        boolean hasPatternParser = mapping.getPatternParser() != null;
                        logger.info("SwaggerFix: provider={} mappingClass={} patternParserPresent={}", provider.getClass().getSimpleName(), mapping.getClass().getName(), hasPatternParser);
                    }
                    List<RequestMappingInfoHandlerMapping> filtered = mappings.stream()
                            .filter(mapping -> mapping.getPatternParser() == null)
                            .collect(Collectors.toList());
                    mappings.clear();
                    mappings.addAll(filtered);
                    int removed = original - mappings.size();
                    logger.info("SwaggerFix: provider={} originalMappings={} kept={} removed={}", provider.getClass().getSimpleName(), original, mappings.size(), removed);
                } catch (NoSuchFieldException nsfe) {
                    // ignore - field not present in this implementation
                } catch (Exception e) {
                    // don't prevent app startup if fix fails
                    e.printStackTrace();
                }
            }
        };
    }
}
