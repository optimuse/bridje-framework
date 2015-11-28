
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEnum
{
    @XmlAttribute
    private String name;
    
    @XmlElements(
    {
        @XmlElement(name = "value", type = HEnumValue.class)
    })
    private java.util.List<HEnumValue> values;
    
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public java.util.List<HEnumValue> getValues()
    {
        return this.values;
    }

    public void setValues(java.util.List<HEnumValue> values)
    {
        this.values = values;
    }

    @XmlTransient
    private HModel model;

    public HModel getModel()
    {
        return model;
    }

    public void setModel(HModel model)
    {
        this.model = model;
    }

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        setModel((HModel)parent);
    }
}