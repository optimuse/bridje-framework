/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.web.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.widgets.UIEvent;
import org.bridje.web.view.widgets.UIInputExpression;
import org.bridje.web.view.widgets.Widget;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from widgets. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebView extends AbstractWebView
{
    @XmlTransient
    private String name;

    @XmlAttribute(name = "title")
    private String title;

    @XmlElements(
            @XmlElement(name = "meta", type = MetaTag.class)
    )
    private List<MetaTag> metaTags;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    @XmlTransient
    private Set<Class<?>> widgets;

    @XmlTransient
    private Set<String> resources;

    @XmlAttribute(name = "theme")
    private String theme;
    
    /**
     * Gets a list of meta information tags information to be rendered with this
     * view.
     *
     * @return A list of meta information tags assigned to this view.
     */
    public List<MetaTag> getMetaTags()
    {
        return metaTags;
    }

    /**
     * The title for this view.
     *
     * @return The title for this view.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * The theme to be use to render this view.
     * 
     * @return The name of the theme for this view.
     */
    public String getTheme()
    {
        return theme;
    }

    /**
     * The name of this view.
     *
     * @return The name of this view.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this view. for internal use of this API only.
     *
     * @param name The name to be set.
     */
    void setName(String name)
    {
        this.name = name;
    }

    /**
     * Finds the set of resources used in this view by all the widgets defined
     * in it.
     *
     * @return A set with all the names of the resources.
     */
    public Set<String> getResources()
    {
        if (resources == null)
        {
            initResources();
        }
        return resources;
    }

    /**
     * Gets the set of widgets classes used in this view.
     *
     * @return All the widgets classes used in this view.
     */
    public Set<Class<?>> getWidgets()
    {
        if (widgets == null)
        {
            initWidgets();
        }
        return widgets;
    }

    /**
     * Finds the input expression that match the given string.
     *
     * @param exp The expression.
     * @return The UIInputExpression object that match whit the given String if
     * any.
     */
    public UIInputExpression findInput(String exp)
    {
        if (inputs == null)
        {
            initInputs();
        }
        return inputs.get(exp);
    }

    /**
     * Finds the event that match with the given action.
     *
     * @param action The name of the action.
     * @return The UIEvent object that match the given expression.
     */
    public UIEvent findEvent(String action)
    {
        if (events == null)
        {
            initEvents();
        }
        return events.get(action);
    }

    private synchronized void initEvents()
    {
        if (events == null)
        {
            Map<String, UIEvent> eventsMap = new HashMap<>();
            findEvents(getRoot(), eventsMap);
            events = eventsMap;
        }
    }

    private synchronized void initWidgets()
    {
        if (widgets == null)
        {
            Set<Class<?>> widgetsSet = new HashSet<>();
            findWidgets(getRoot(), widgetsSet);
            widgets = widgetsSet;
        }
    }

    private synchronized void initResources()
    {
        if (resources == null)
        {
            Set<String> resourcesSet = new HashSet<>();
            findResources(getRoot(), resourcesSet);
            resources = resourcesSet;
        }
    }

    private synchronized void initInputs()
    {
        if (inputs == null)
        {
            Map<String, UIInputExpression> inputsMap = new HashMap<>();
            findInputs(getRoot(), inputsMap);
            inputs = inputsMap;
        }
    }

    private void findEvents(Widget widget, Map<String, UIEvent> eventsMap)
    {
        widget.events().forEach((ev) -> eventsMap.put(ev.getExpression(), ev));
        widget.childs().forEach((child) -> findEvents(child, eventsMap));
    }

    private void findInputs(Widget widget, Map<String, UIInputExpression> inputsMap)
    {
        widget.inputs().forEach((in) -> inputsMap.put(in.getParameter(), in));
        widget.childs().forEach((child) -> findInputs(child, inputsMap));
    }

    private void findResources(Widget widget, Set<String> resourcesSet)
    {
        widget.resources().forEach((r) -> resourcesSet.add(r));
        widget.childs().forEach((child) -> findResources(child, resourcesSet));
    }

    private void findWidgets(Widget widget, Set<Class<?>> widgetsSet)
    {
        widgetsSet.add(widget.getClass());
        widget.childs().forEach((child) -> findWidgets(child, widgetsSet));
    }
}
