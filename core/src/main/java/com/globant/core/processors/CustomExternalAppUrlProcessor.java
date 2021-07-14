
package com.globant.core.processors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.services.embed.UrlProcessor;

@Component(
    service = UrlProcessor.class
)
public class CustomExternalAppUrlProcessor implements com.adobe.cq.wcm.core.components.services.embed.UrlProcessor {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(CustomExternalAppUrlProcessor.class);
    
    protected static final String NAME_ID = "name";
    protected static final String NAME = "externalApp";
    protected static final String SCHEME = "localhost:8080/greet?name=(\\d+)";
    private Pattern pattern = Pattern.compile(SCHEME);
    
    @SuppressWarnings("serial")
    @Override
    public Result process(String url) {
        LOGGER.error("INSIDE DotDigitalUrlProcessor");
        String[] allowedDomains = new String[]{"comms.castrol.com", "r1.dotdigital-pages.com"};
        if (StringUtils.isNotEmpty(url)) {
            final Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                
                if (ArrayUtils.isEmpty(allowedDomains)) {
                    LOGGER.error("DotDigital Config not setup");
                    return null;
                }
                final Optional<String> domain = Arrays.asList(allowedDomains).stream()
                    .filter(myUrl -> myUrl.startsWith("https:" + myUrl)).findFirst();
                if (domain.isPresent()) {
                    return new UrlProcessor.Result() {
                        
                        @Override
                        public String getProcessor() {
                            return NAME;
                        }
                        
                        @Override
                        public Map<String, Object> getOptions() {
                            return new HashMap<String, Object>() {{
                                        this.put("dotDigitalUrl", "http://dummy.com");
                                }
                            };
                        }
                    };
                }
            }
        }
        return null;
    }
}