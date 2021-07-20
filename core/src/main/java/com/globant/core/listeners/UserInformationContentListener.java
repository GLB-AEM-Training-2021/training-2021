package com.globant.core.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    name = "[AEM Training] User Information Content Listener",
    service = ResourceChangeListener.class,
    property = {
        ResourceChangeListener.PATHS + "=" + UserInformationContentListener.LISTENER_ROOT_PATH,
        ResourceChangeListener.CHANGES + "=" + "ADDED",
        ResourceChangeListener.CHANGES + "=" + "CHANGED",
        ResourceChangeListener.CHANGES + "=" + "REMOVED"
    }
)
public class UserInformationContentListener implements ResourceChangeListener {

    public static final String LISTENER_ROOT_PATH = "/content/training-2021";

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Listener callback for the configPage properties change.
     */
    public void onChange(List<ResourceChange> changes) {
        ResourceResolver resolver = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "training-observation");
        try {
            resolver = resourceResolverFactory.getServiceResourceResolver(param);
            //resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            for (ResourceChange change : changes) {
                LOGGER.debug("{} -> {}.", change.getType(), change.getPath());
                
            }
        } catch (Exception e) {
            LOGGER.error("Error handling config page change", e);
            resolver.close();
        }
    }

}