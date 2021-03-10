package com.globant.core.services;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = HelloWorldService.class)
@Designate(ocd = TrainingAppHelloWorldService.HelloConfig.class)
public class TrainingAppHelloWorldService implements HelloWorldService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingAppHelloWorldService.class);
    String prefix = StringUtils.EMPTY;
    String suffix = StringUtils.EMPTY;
    int number;
    
    @ObjectClassDefinition(name = "[AEM Training 2021] hello world configuration", description = "This is sample configuration")
    public @interface HelloConfig {

        @AttributeDefinition(name = "Prefix", defaultValue = "Hello ", description = "Prefix of the salute")
        String prefix();
        
        @AttributeDefinition(name = "Suffix", defaultValue = "!!", description = "Sufix of the salute")
        String suffix();
        
        @AttributeDefinition(name = "Dummy number field", defaultValue = "1", description = "Random number...", type = AttributeType.INTEGER)
        int number();
        
        @AttributeDefinition(name = "String Array Value", defaultValue = {""}, description = "Random number...")
        String[] dummyStringArray();

    }
    
    public String getCustomHelloWorld(String name) {
        return prefix + " " +  name + suffix + " (" + number + ")";
    }
    
    @Activate
    @Modified
    protected void activate(final HelloConfig config) {
        LOGGER.info("Deactivating component...");
        LOGGER.info("Loading properties:");
        prefix = config.prefix();
        LOGGER.info("   * prefix: {}", prefix);
        suffix = config.suffix();
        LOGGER.info("   * suffix: {}", suffix);
        number = config.number();
        LOGGER.info("   * number: {}", number);
        
        // we can load a database url from the properties here...
        // and stablish the connection
        
    }
    
    @Deactivate
    protected void deactivate(final HelloConfig config) {
        LOGGER.info("Deactivating component...");
        
        // close connection, or resources in general
        
    }

}
