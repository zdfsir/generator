package ${Configuration.packageName}.${Configuration.path.daoInterface};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
import ${Configuration.packageName}.${Configuration.path.entityVO}.${ClassName}VO;
import ${Configuration.packageName}.${Configuration.path.entitySearchDTO}.${ClassName}SearchDTO;
import ${Configuration.packageName}.${Configuration.path.entityRequestDTO}.${ClassName}RequestDTO;
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rzhkj.facade.base.mybatisplus.PageSelect;
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
public interface ${ClassName}Dao {

    /**
     * 保存${Remarks}
     *
     * @param ${ClassName?uncap_first}
     * @return boolean
     */
    boolean save(${ClassName} ${ClassName?uncap_first});

    /**
     * 修改${Remarks}
     *
     * @param ${ClassName?uncap_first} 修改sql中set的值
     * @param updateWrapper 修改sql中where的条件
     * @return int
     */
    int update(${ClassName} ${ClassName?uncap_first}, UpdateWrapper<${ClassName}> updateWrapper);

<#list uniqueColumnList as item>
        <#--/**
        columnName:${item.columnName},
        propertyName: ${item.propertyName},
        propertyType:${item.propertyType},
        remarks:${item.remarks}
        */-->
    /**
     * 根据唯一键${item.propertyName}查询一个${Remarks}
     *
     * @param ${item.propertyName}
     * @return ${ClassName}VO
     */
    ${ClassName}VO getBy${item.propertyName?cap_first}(${item.propertyType} ${item.propertyName});

</#list>
    /**
     * 筛选${Remarks}
     *
     * @param queryWrapper
     * @return List<${ClassName}VO>
     */
    List<${ClassName}VO> selectList(QueryWrapper<${ClassName}> queryWrapper);

    /**
     * 分页筛选${Remarks}
     *
     * @param page
     * @param queryWrapper
     * @return IPage<${ClassName}VO>
     */
    IPage<${ClassName}VO> selectPage(Page<${ClassName}> page, QueryWrapper<${ClassName}> queryWrapper);
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
