<#include "utils.ftl" />
<@javaDocLicense model.license />

package ${model.packageName};

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 */
@XmlEnum
public enum ${name}
{
    <#list values as v>
    /**
     *
     */
    ${v.name}<#sep>,</#sep>

    </#list>
    ;
}