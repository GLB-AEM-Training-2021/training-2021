package com.globant.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import com.day.cq.replication.ReplicationAction;

@Component(
    name= "[AEM Training] Replication Events logger",
    service = EventHandler.class, 
    immediate = true, 
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC 
    }
)
public class ReplicationEventsLoggerListener implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    protected ResourceResolverFactory resourceResolverFactory;

    public void handleEvent(final Event event) {
        logger.debug("Resource event: {} at: {}", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));
        
        ReplicationAction action = ReplicationAction.fromEvent(event);
        Map<String, Object> userParams = new HashMap<>();
        userParams.put(ResourceResolverFactory.SUBSERVICE, "training-observation");

        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(userParams)) {
            Session session = resourceResolver.adaptTo(Session.class);
            Node eventsNode = session.getNode("/var/aem-training/events/replication");
            
            UUID randomUUID = UUID.randomUUID();
            String nodename = randomUUID.toString();
            Node eventNode = eventsNode.addNode(nodename, "nt:unstructured"); 
            eventNode.setProperty("path", action.getPath());
            eventNode.setProperty("user", action.getUserId());
            eventNode.setProperty("timestamp", action.getTime());

            session.save();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
