<#ftl encoding="UTF-8">

package ${widget.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.bridje.web.view.Defines;
import org.bridje.web.view.widgets.*;

<#if widget.rootElement != "">
@XmlRootElement( name = "${widget.rootElement}" )
</#if>
@XmlAccessorType(XmlAccessType.FIELD)
public class ${widget.name} extends ${widget.base}
{
    <#if widget.hasChildren>
    @XmlTransient
    private List<Widget> childs;

    </#if>
    <#if widget.hasInputs>
    @XmlTransient
    private List<UIInputExpression> inputs;

    </#if>
    <#if widget.hasEvents>
    @XmlTransient
    private List<UIEvent> events;

    </#if>
    <#if widget.hasResources>
    @XmlTransient
    private List<String> resources;

    </#if>
    <#list widget.fields as f>
    <#if f.fieldType == "attribute">
    @XmlAttribute
    <#elseif f.fieldType == "value">
    @XmlValue
    <#elseif f.fieldType == "children">
    <#if f.wrapper != "">
    @XmlElementWrapper( name = "${f.wrapper}" )
    </#if>
    @XmlElements(
    {
        <#list f.children?keys as k>
        @XmlElement( name = "${k}", type = ${f.children[k]}.class ),
        </#list>
        <#if f.allowPlaceHolder>
        @XmlElement( name = "placeholder", type = WidgetPlaceHolder.class ),
        </#if>
    })
    <#elseif f.fieldType == "child">
    @XmlElements(
    {
        @XmlElement( name = "${f.name}", type = ${f.javaType}.class ),
        <#if f.allowPlaceHolder>
        @XmlElement( name = "placeholder", type = WidgetPlaceHolder.class ),
        </#if>
    })
    </#if>
    private ${f.javaType} ${f.name};

    </#list>
    <#list widget.fields as f>
    <#if f.javaType == "UIExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public ${f.resultType} get${f.name?cap_first}()
    {
        return get(${f.name}, ${f.resultType}.class, ${f.defaultValue});
    }
    <#elseif f.javaType == "UIInputExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public ${f.resultType} get${f.name?cap_first}()
    {
        return get(${f.name}, ${f.resultType}.class, ${f.defaultValue});
    }
    <#elseif f.javaType == "UIEvent">
    public ${f.javaType} get${f.name?cap_first}()
    {
        return ${f.name};
    }
    <#else>
    public ${f.javaType} get${f.name?cap_first}()
    {
        if(${f.name} == null)
        {
            ${f.name} = ${f.defaultValue};
        }
        return ${f.name};
    }
    </#if>

    </#list>
    <#if widget.hasChildren>
    @Override
    public List<? extends Widget> childs()
    {
        if(childs == null)
        {
            childs = new ArrayList<>();
            <#list widget.fields as f>
            <#if f.fieldType == "children">
            if(${f.name} != null)
            {
                <#if f.isSingle>
                childs.add(${f.name});
                <#else>
                childs.addAll(${f.name});
                </#if>
            }
            </#if>
            <#if f.fieldType == "child">
            if(${f.name} != null)
            {
                childs.add(${f.name});
            }
            </#if>
            </#list>
        }
        return childs;
    }

    </#if>
    <#if widget.hasInputs>
    @Override
    public List<UIInputExpression> inputs()
    {
        if(inputs == null)
        {
            inputs = new ArrayList<>();
            <#list widget.fields as f>
            <#if f.javaType == "UIInputExpression">
            if(${f.name} != null && ${f.name}.isValid())
            {
                inputs.add(${f.name});
            }
            </#if>
            </#list>
        }
        return inputs;
    }

    </#if>
    <#if widget.hasEvents>
    @Override
    public List<UIEvent> events()
    {
        if(events == null)
        {
            events = new ArrayList<>();
            <#list widget.fields as f>
            <#if f.javaType == "UIEvent">
            if(${f.name} != null)
            {
                events.add(${f.name});
            }
            </#if>
            </#list>
        }
        return events;
    }

    </#if>
    <#if widget.hasResources>
    @Override
    public List<String> resources()
    {
        if(resources == null)
        {
            resources = new ArrayList<>();
            <#list widget.resources as r>
            resources.add("${r}");
            </#list>
        }
        return resources;
    }

    </#if>
    @Override
    public void doOverride(Map<String, Defines> definesMap)
    {
        <#list widget.fields as f>
        <#if f.fieldType == "children">
        <#if f.allowPlaceHolder>
        if(${f.name} != null)
        {
            <#if f.isSingle>
            ${f.name} = (${f.javaType})Widget.doOverride(${f.name}, definesMap);
            <#else>
            ${f.name} = Widget.doOverride(${f.name}, definesMap);
            </#if>
        }
        </#if>
        </#if>
        <#if f.fieldType == "child">
        <#if f.allowPlaceHolder>
        if(${f.name} != null)
        {
            ${f.name} = (${f.javaType})Widget.doOverride(${f.name}, definesMap);
        }
        </#if>
        </#if>
        </#list>
    }
}