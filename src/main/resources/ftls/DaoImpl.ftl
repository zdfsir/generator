package ${Configuration.packageName}.${Configuration.path.daoImpl};

import ${Configuration.packageName}.${Configuration.path.mapper}.${MapperClassName};
import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
import ${Configuration.packageName}.${Configuration.path.daoInterface}.${ClassName}Dao;
import ${Configuration.packageName}.${Configuration.path.entityVO}.${ClassName}VO;
import ${Configuration.packageName}.${Configuration.path.entitySearchDTO}.${ClassName}SearchDTO;
import ${Configuration.packageName}.${Configuration.path.entityRequestDTO}.${ClassName}RequestDTO;
${InterfaceImport}
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
<#else>
import java.io.Serializable;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import com.rzhkj.base.core.tools.ObjectTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
@Slf4j
@Repository
<#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
public class ${ClassName}DaoImpl implements ${ClassName}Dao {

    @Autowired
    private ${ClassName}Mapper ${ClassAttrName}Mapper;

    /**
     * 保存${Remarks}
     *
     * @param ${ClassName?uncap_first}
     * @return boolean
     */
    @Override
    public boolean insert(${ClassName} ${ClassName?uncap_first}) {
        if (null == ${ClassName?uncap_first}) {
            return false;
        }
        int result = ${ClassAttrName}Mapper.insert(${ClassName?uncap_first});
        return result > 0;
    }

    /**
     * 修改${Remarks}
     *
     * @param ${ClassName?uncap_first} 修改sql中set的值
     * @param updateWrapper 修改sql中where的条件
     * @return int
     */
    @Override
    public int update(${ClassName} ${ClassName?uncap_first}, UpdateWrapper<${ClassName}> updateWrapper) {
        if (null == ${ClassName?uncap_first} || null == updateWrapper) {
            return 0;
        }
        int result = ${ClassAttrName}Mapper.update(${ClassName?uncap_first}, updateWrapper);
        return result;
    }

    <#list uniqueColumnList as item>
    /**
     * 根据唯一键${item.propertyName}查询一个${Remarks}
     *
     * @param ${item.propertyName}
     * @return ${ClassName}VO
     */
    @Override
    public ${ClassName}VO getBy${item.propertyName?cap_first}(${item.propertyType} ${item.propertyName}) {
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(new ${ClassName}().set${item.propertyName?cap_first}(${item.propertyName}));
        ${ClassName} ${ClassName?uncap_first} = ${ClassAttrName}Mapper.selectOne(queryWrapper);
        if (null == ${ClassName?uncap_first}) {
            return null;
        }
        ${ClassName}VO ${ClassName?uncap_first}VO = ObjectTools.toVo(${ClassName?uncap_first}, ${ClassName}VO.class);
        return ${ClassName?uncap_first}VO;
    }

    /**
     * 检查唯一键${item.propertyName}是否已存在于${Remarks}
     *
     * @param ${item.propertyName}
     * @return boolean
     */
    @Override
    public boolean check${item.propertyName?cap_first}IsExist(${item.propertyType} ${item.propertyName}) {
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(new ${ClassName}().set${item.propertyName?cap_first}(${item.propertyName}));
        ${ClassName} ${ClassName?uncap_first} = ${ClassAttrName}Mapper.selectOne(queryWrapper);
        return null != ${ClassName?uncap_first};
    }

    </#list>
    /**
     * 筛选${Remarks}
     *
     * @param queryWrapper
     * @return List<${ClassName}VO>
     */
    @Override
    public List<${ClassName}VO> selectList(QueryWrapper<${ClassName}> queryWrapper) {
        List<${ClassName}> list = ${ClassAttrName}Mapper.selectList(queryWrapper);
        List<${ClassName}VO> listVO = ObjectTools.convertList(list, ${ClassName}VO.class);
        return listVO;
    }

    /**
     * 筛选${Remarks}
     *
     * @param queryWrapper
     * @return List<${ClassName}VO>
     */
    @Override
    public List<${ClassName}VO> selectList(MPJQueryWrapper<${ClassName}> queryWrapper) {
        List<${ClassName}VO> listVO = ${ClassAttrName}Mapper.selectJoinList(${ClassName}VO.class, queryWrapper);
        return listVO;
    }

    /**
     * 分页筛选${Remarks}
     *
     * @param page
     * @param queryWrapper
     * @return IPage<${ClassName}VO>
     */
    @Override
    public IPage<${ClassName}VO> selectPage(Page<${ClassName}> page, QueryWrapper<${ClassName}> queryWrapper) {
        IPage<${ClassName}> iPage = ${ClassAttrName}Mapper.selectPage(page, queryWrapper);
        IPage<${ClassName}VO> iPageVO = iPage.convert(o -> ObjectTools.toVo(o, ${ClassName}VO.class));
        return iPageVO;
    }

    /**
     * 分页筛选${Remarks}
     *
     * @param page
     * @param queryWrapper
     * @return IPage<${ClassName}VO>
     */
    @Override
    public IPage<${ClassName}VO> selectPage(Page<${ClassName}> page, MPJQueryWrapper<${ClassName}> queryWrapper) {
        IPage<${ClassName}VO> iPageVO = ${ClassAttrName}Mapper.selectJoinPage(page, ${ClassName}VO.class, queryWrapper);
        return iPageVO;
    }
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