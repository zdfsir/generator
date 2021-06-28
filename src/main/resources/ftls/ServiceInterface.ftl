package ${Configuration.packageName}.${Configuration.path.interf};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
import ${Configuration.packageName}.${Configuration.path.entityVO}.${ClassName}VO;
import ${Configuration.packageName}.${Configuration.path.entitySearchDTO}.${ClassName}SearchDTO;
import ${Configuration.packageName}.${Configuration.path.entityRequestDTO}.${ClassName}RequestDTO;
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface ${InterfaceClassName} extends IService<${ClassName}> {

    /**
     * 保存${Remarks}
     *
     * @param requestDTO
     * @return boolean
     */
    boolean save(${ClassName}RequestDTO requestDTO);

<#list uniqueColumnList as item>
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
     * @param searchDTO
     * @return List<${ClassName}VO>
     */
    List<${ClassName}VO> selectList(${ClassName}SearchDTO searchDTO);

    /**
     * 分页筛选${Remarks}
     *
     * @param pageSelect
     * @return IPage<${ClassName}VO>
     */
    IPage<${ClassName}VO> selectPage(PageSelect<${ClassName}SearchDTO> pageSelect);

    /**
     * 删除${Remarks}
     *
     * @param requestDTO
     * @return boolean
     */
    boolean delete(${ClassName}RequestDTO requestDTO);
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
