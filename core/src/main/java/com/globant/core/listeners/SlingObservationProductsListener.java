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
import org.osgi.service.event.EventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration Page Changed listener. 
 * In charge of notifying the config pages update to any interested component
 * 
 * @author <a href=
 *         "mailto:mauricio.rodriguez@globant.com">mauricio.rodriguez</a>
 */
/**
 * The type Page activation listener.
 */
@Component(
    service = ResourceChangeListener.class,
    property = {
        ResourceChangeListener.PATHS + "=" + SlingObservationProductsListener.LISTENER_ROOT_PATH,
        ResourceChangeListener.CHANGES + "=" + "ADDED",
        ResourceChangeListener.CHANGES + "=" + "CHANGED",
        ResourceChangeListener.CHANGES + "=" + "REMOVED "       
    }
)
public class SlingObservationProductsListener implements ResourceChangeListener {

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
        param.put(ResourceResolverFactory.SUBSERVICE, "training-2021-observation");
        try {
            resolver = resourceResolverFactory.getServiceResourceResolver(param);
            //resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            for (ResourceChange change : changes) {
                LOGGER.debug("{} -> {}.", change.getType(), change.getPath());
                
                // remove all /*
                
                // send an email
                // modify a property
            }
        } catch (Exception e) {
            LOGGER.error("Error handling config page change", e);
            resolver.close();
        }
    }

}