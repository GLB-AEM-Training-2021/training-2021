package com.globant.core.listeners;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

/**
 * TODO
 *
 * @author <a href=
 *         "mailto:mauricio.rodriguez@globant.com">mauricio.rodriguez</a>
 */
@Component(immediate = true,service = EventListener.class)
public class JcrObservationProductListener implements EventListener {

    public static final String LISTENER_ROOT_PATH = "/content/training-2021";

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private Session session;
    ResourceResolver resolver;

    @Activate
    public void activate(ComponentContext context) throws Exception {
        log.info("Activating JcrObservationProductListener...");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "training-2021-observation");
        try {
            resolver = resourceResolverFactory.getServiceResourceResolver(param);
            //resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            session = resolver.adaptTo(Session.class);
            session.getWorkspace().getObservationManager().addEventListener(this,
                Event.PROPERTY_CHANGED | Event.PROPERTY_ADDED , LISTENER_ROOT_PATH, true, null, null, false);
        } catch (Exception e) {
            log.error("unable to register session", e);
            throw new Exception(e);
        }
        log.info("Activating JcrObservationProductListener done!");
    }

    @Deactivate
    public void deactivate() {
        log.info("Deactivating JcrObservationProductListener...");
        if (session != null && session.isLive()) {
            try {
                session.getWorkspace().getObservationManager().removeEventListener(this);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                session.logout();
            }
        }
        log.info("Deactivating JcrObservationProductListener done!");
    }

    /**
     * Listener callback for the configPage properties change.
     */
    public void onEvent(EventIterator eventIterator) {
        // TODO do not place heavy operations here
        try {

            while(eventIterator.hasNext()) {
                log.debug("Processing event...");
                Event event = eventIterator.nextEvent();
                log.debug("Path: {}", event.getPath());
                log.debug("Idenntifier: {}", event.getIdentifier());
                log.debug("Type: {}", event.getType());
            }

            session.save();
        } catch (Exception e) {
            log.error("Error while treating events", e);
        }
    }
}