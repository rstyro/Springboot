package top.lrshuai.amqp.commons.${package.ModuleName}.mapper;

import ${cfg.commons}.entity.${entity};
import ${superMapperClassPackage};

/**
* <p>
    * ${table.comment!} Mapper 接口
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
    interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
    public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {
    }
</#if>
