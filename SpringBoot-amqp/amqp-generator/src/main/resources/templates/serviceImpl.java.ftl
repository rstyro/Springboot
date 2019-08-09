package top.lrshuai.amqp.commons.${package.ModuleName}.service.impl;
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${cfg.commons}.entity.${entity};
import ${cfg.commons}.mapper.${table.mapperName};
import ${cfg.commons}.service.${table.serviceName};

/**
* <p>
    * ${table.comment!} 服务实现类
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Service
@Transactional
<#if kotlin>
    open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

    }
<#else>
    public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    }
</#if>
