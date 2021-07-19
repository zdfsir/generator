package ${Configuration.packageName}.${Configuration.path.mapper};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
<#if Configuration.mybatisPlusEnable>
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;
<#elseif Configuration.jpaEnable>
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
<#else>
import org.apache.ibatis.annotations.Mapper;
</#if>

import java.io.Serializable;
import java.util.List;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
<#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
@Mapper
public interface ${MapperClassName} extends MPJBaseMapper<${ClassName}> {

}
<#elseif Configuration.jpaEnable><#-- jpa模式 -->
@Repository
public interface ${MapperClassName} extends JpaRepository<${ClassName}, Serializable> {

}
<#else><#-- mybatis模式 -->
@Mapper
public interface ${MapperClassName} {

    ${ClassName} get(Serializable id);

    List<${ClassName}> findAll();

    int insert(${ClassName} ${ClassAttrName});

    int insertBatch(List<${ClassName}> ${ClassAttrName}s);

    int update(${ClassName} ${ClassAttrName});

    int delete(${ClassName} ${ClassAttrName});

}
</#if>