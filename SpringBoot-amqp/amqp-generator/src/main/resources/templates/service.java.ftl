package top.lrshuai.amqp.commons.${package.ModuleName}.service;

import ${cfg.commons}.entity.${entity};
import ${superServiceClassPackage};

/**
* <p>
    * ${table.comment!} 服务类
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
    interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
    public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    }
</#if>
