package ${Configuration.packageName}.${Configuration.path.service};

import ${Configuration.packageName}.${Configuration.path.mapper}.${MapperClassName};
import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
${InterfaceImport}
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
<#else>
import java.io.Serializable;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
@Service
<#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
public class ${ServiceClassName} extends ServiceImpl<${MapperClassName}, ${ClassName}> ${Implements} {

}
<#else><#-- mybatis或jpa模式 -->
public class ${ServiceClassName} ${Implements} {
    @Autowired
    private ${MapperClassName} ${MapperAttrName};
    <#if Configuration.jpaEnable><#-- jpa模式 -->
    ${Override}
    public ${ClassName} get(Serializable id) {
        return ${MapperAttrName}.findById(id).orElse(null);
    }
    ${Override}
    public List<${ClassName}> findAll() {
        return ${MapperAttrName}.findAll();
    }
    ${Override}
    public ${ClassName} insert(${ClassName} ${ClassAttrName}) {
        return ${MapperAttrName}.save(${ClassAttrName});
    }
    ${Override}
    public List<${ClassName}> insertBatch(List<${ClassName}> ${ClassAttrName}s){
        return ${MapperAttrName}.saveAll(${ClassAttrName}s);
    }
    ${Override}
    public ${ClassName} update(${ClassName} ${ClassAttrName}) {
        return ${MapperAttrName}.save(${ClassAttrName});
    }
    ${Override}
    public void delete(${ClassName} ${ClassAttrName}) {
        ${MapperAttrName}.delete(${ClassAttrName});
    }
    <#else><#-- mybatis模式 -->
    ${Override}
    public ${ClassName} get(Serializable id) {
        return ${MapperAttrName}.get(id);
    }
    ${Override}
    public List<${ClassName}> findAll() {
        return ${MapperAttrName}.findAll();
    }
    ${Override}
    public int insert(${ClassName} ${ClassAttrName}) {
        return ${MapperAttrName}.insert(${ClassAttrName});
    }
    ${Override}
    public int insertBatch(List<${ClassName}> ${ClassAttrName}s) {
        return ${MapperAttrName}.insertBatch(${ClassAttrName}s);
    }
    ${Override}
    public int update(${ClassName} ${ClassAttrName}) {
        return ${MapperAttrName}.update(${ClassAttrName});
    }
    ${Override}
    public int delete(${ClassName} ${ClassAttrName}) {
        return ${MapperAttrName}.delete(${ClassAttrName});
    }
    </#if>
}
</#if>