package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Country;
import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import com.luv2code.ecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unsupportedMethods = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT};

        //disable HTTP methods for Product, ProductCategory: DELETE, POST, PUT
        disableHttpMethods(Product.class, config, unsupportedMethods);
        disableHttpMethods(ProductCategory.class, config, unsupportedMethods);
        disableHttpMethods(Country.class, config, unsupportedMethods);
        disableHttpMethods(State.class, config, unsupportedMethods);

        // expose ids
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType)
                .toArray(Class[]::new));

    }

    private static void disableHttpMethods(Class<?> theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedMethods) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)));
    }
}
