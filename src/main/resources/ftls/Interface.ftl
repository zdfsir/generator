package ${Configuration.packageName}.${Configuration.path.interf};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.extension.service.IService;
<#else>
import java.io.Serializable;
</#if>
import java.io.Serializable;
import java.util.List;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
<#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
public interface ${InterfaceClassName} extends IService<${ClassName}> {

}
<#else><#-- mybatis或jpa模式 -->
public interface ${InterfaceClassName} {

    <#if Configuration.jpaEnable><#-- jpa模式 -->
    ${ClassName} get(Serializable id);

    List<${ClassName}> findAll();

    ${ClassName} insert(${ClassName} ${ClassAttrName});

    List<${ClassName}> insertBatch(List<${ClassName}> ${ClassAttrName}s);

    ${ClassName} update(${ClassName} ${ClassAttrName});

    void delete(${ClassName} ${ClassAttrName});

    <#else><#-- mybatis模式 -->
    ${ClassName} get(Serializable id);

    List<${ClassName}> findAll();

    int insert(${ClassName} ${ClassAttrName});

    int insertBatch(List<${ClassName}> ${ClassAttrName}s);

    int update(${ClassName} ${ClassAttrName});

    int delete(${ClassName} ${ClassAttrName});

    </#if>
}
</#if>
