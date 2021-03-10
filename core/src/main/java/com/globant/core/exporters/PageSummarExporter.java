package com.globant.core.exporters;

import javax.annotation.Resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = { SlingHttpServletRequest.class },
        resourceType = "training-2021/components/page",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
    name = "jackson", 
    extensions = "json", 
    selector = "summary",
    options = {
        @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
        @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value="false")
    }
)
public class PageSummarExporter {
    
    @Self
    private SlingHttpServletRequest request;

    @Self
    private Resource resource;
    
    @ValueMapValue(name = "jcr:title")
    @Optional
    private String title;
    
    @ValueMapValue(name = "sling:resourceType")
    @Optional
    private String slingResourceType;

    @ValueMapValue(name = "cq:template")
    @Optional
    private String cqTemplate;

    public String getTitle() {
        return title;
    }

    public String getSlingResourceType() {
        return slingResourceType;
    }

    public String getCqTemplate() {
        return cqTemplate;
    }

}
