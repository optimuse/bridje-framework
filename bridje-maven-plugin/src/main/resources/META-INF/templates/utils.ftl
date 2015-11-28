<#macro entityContent>
    <#if fields??>
    <#list fields as f>
    <#if f.transient>
    @XmlTransient
    <#elseif f.isList >
    @XmlElements(
    {
        <#list f.elements as e>
        @XmlElement(name = "${e.name}", type = ${e.type}.class)<#sep>, </#sep>
        </#list>
    })
    <#elseif f.access == "ATTRIBUTE" >
    @XmlAttribute
    </#if>
    private ${f.javaType} ${f.name}<#if !f.isNullable && f.defaultValue??> = ${f.defaultValueExp}</#if>;
    
    </#list>
    <#list fields as f>
    public ${f.javaType} get${f.name?cap_first}()
    {
        <#if f.isNullable && f.defaultValue??>
        if(this.${f.name} == null)
        {
            this.${f.name} = ${f.defaultValueExp};
        }
        </#if>
        return this.${f.name};
    }

    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} ${f.name})
    {
        this.${f.name} = ${f.name};
    }

    </#list>
    </#if>
</#macro>

<#macro xmlFields fields indent=0>
    <#local spc>${""?left_pad(indent * 4)}</#local>
    <#list fields as field>
    <#if field.isList>
        ${spc}<xs:sequence>
            ${spc}<xs:element name="${field.name}" minOccurs="0">
                ${spc}<xs:complexType>
                    ${spc}<xs:sequence>
                        <#list field.elements![] as element >
                        ${spc}<xs:element name="${element.name}" type="tns:${element.type?uncap_first}" minOccurs="0" maxOccurs="unbounded"/>
                        </#list>
                    ${spc}</xs:sequence>
                ${spc}</xs:complexType>
            ${spc}</xs:element>
        ${spc}</xs:sequence>
    </#if>
    </#list>
    <#list fields as field>
    <#if field.access?? && field.access == "ATTRIBUTE">
        ${spc}<xs:attribute name="${field.name}" type="xs:${field.xmlType}"/>
    </#if>
    </#list>
</#macro>