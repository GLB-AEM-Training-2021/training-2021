/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.globant.core.exporters;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.globant.core.services.HelloWorldService;

@Exporter(
    name = "jackson", 
    extensions = "json", 
    selector = "userinfo.userexporter",
    options = {
        @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
        @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value="false")
    }
)
@Model(
    adaptables = {Resource.class, SlingHttpServletRequest.class},
    resourceType = "training-2021/components/userinfocomponent",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserExporter {
    
    @Self
    SlingHttpServletRequest selfRequest;

    @ValueMapValue(name="name")
    @Default(values="<name>")
    protected String name;
    
    @ValueMapValue(name="surname")
    @Default(values="<surname>")
    protected String surname;
    
    @ValueMapValue(name="email")
    @Default(values="<email>")
    protected String email;
    
    @ValueMapValue(name="cellphone")
    @Default(values="<cellphone>")
    protected String cellphone;

    @ValueMapValue(name="avatar")
    @Default(values="<avatar>")
    protected String avatar;
    
    @OSGiService
    private SlingSettingsService settings;
    
    @SlingObject
    private Resource currentResource;
    
    @SlingObject
    private ResourceResolver resourceResolver;
    
    @OSGiService
    private HelloWorldService helloWorldService;

    public String getName() {
        return name;
    }
    
    public String getRenderer() {
        return "servlet-v2-exporter";
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getAvatar() {
        return avatar;
    }

}
