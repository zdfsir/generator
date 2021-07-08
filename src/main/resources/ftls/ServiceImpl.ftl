package ${Configuration.packageName}.${Configuration.path.service};

import ${Configuration.packageName}.${Configuration.path.mapper}.${MapperClassName};
import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
import ${Configuration.packageName}.${Configuration.path.daoInterface}.${ClassName}Dao;
import ${Configuration.packageName}.${Configuration.path.entityVO}.${ClassName}VO;
import ${Configuration.packageName}.${Configuration.path.entitySearchDTO}.${ClassName}SearchDTO;
import ${Configuration.packageName}.${Configuration.path.entityRequestDTO}.${ClassName}RequestDTO;
${InterfaceImport}
<#if Configuration.mybatisPlusEnable>
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
<#else>
import java.io.Serializable;
</#if>
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.rzhkj.base.core.tools.ObjectTools;
import com.rzhkj.discovery.core.tools.FunctionCheckUtil;
import com.rzhkj.facade.base.mybatisplus.PageSelect;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
@Slf4j
@DubboService
<#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
public class ${ServiceClassName} extends ServiceImpl<${MapperClassName}, ${ClassName}> ${Implements} {

    @Resource
    private FunctionCheckUtil functionCheck;

    @Autowired
    private ${ClassName}Dao ${ClassAttrName}Dao;

    /**
     * 保存${Remarks}
     *
     * @param requestDTO
     * @return
     */
    @Override
    public boolean save(${ClassName}RequestDTO requestDTO) {
        ${ClassName} ${ClassName?uncap_first} = BeanUtil.copyProperties(requestDTO, ${ClassName}.class);
        LocalDateTime now = LocalDateTime.now();
<#list dateTimeColumnList as item>
<#--    // ${item.propertyName}, ${item.isUnique?string("true", "false")}, ${item.isRequired?string("true", "false")}-->
    <#if item.isRequired>
        ${ClassName?uncap_first}.set${item.propertyName?cap_first}(now);
    </#if>
</#list>
        return ${ClassAttrName}Dao.insert(${ClassName?uncap_first});
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
        return ${ClassAttrName}Dao.getBy${item.propertyName?cap_first}(${item.propertyName});
    }

</#list>

    /**
     * 筛选${Remarks}
     *
     * @param searchDTO
     * @return List<${ClassName}VO>
     */
    @Override
    public List<${ClassName}VO> selectList(${ClassName}SearchDTO searchDTO) {
        QueryWrapper<${ClassName}> queryWrapper = this.getQueryWrapper(searchDTO);
        List<${ClassName}VO> listVO = ${ClassAttrName}Dao.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(listVO)) {
            listVO.parallelStream().forEach(o -> {
                this.setExtend(o);
            });
        }
        return listVO;
    }

    /**
     * 分页筛选${Remarks}
     *
     * @param pageSelect
     * @return IPage<${ClassName}VO>
     */
    @Override
    public IPage<${ClassName}VO> selectPage(PageSelect<${ClassName}SearchDTO> pageSelect) {
        ${ClassName}SearchDTO searchDTO = pageSelect.getSearchDTO();
        QueryWrapper<${ClassName}> queryWrapper = this.getQueryWrapper(searchDTO);
        IPage<${ClassName}VO> iPageVO = ${ClassAttrName}Dao.selectPage(new Page<>(pageSelect.getCurPage(), pageSelect.getPageSize()), queryWrapper);
        iPageVO.setTotal(this.baseMapper.selectCount(queryWrapper));
        if (CollectionUtil.isNotEmpty(iPageVO.getRecords())) {
            iPageVO.getRecords().parallelStream().forEach(o -> {
                this.setExtend(o);
            });
        }
        return iPageVO;
    }


    /**
    * 装箱扩展的查询条件
    *
    * @param searchDTO
    * @return
    */
    private QueryWrapper<${ClassName}> getQueryWrapper(${ClassName}SearchDTO searchDTO) {
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper();
        ${ClassName} entity = ObjectTools.toEntity(searchDTO, ${ClassName}.class);
        queryWrapper.setEntity(entity);
        return queryWrapper;
    }

    /**
    * 装箱扩展参数
    *
    * @param vo
    */
    private void setExtend(${ClassName}VO vo) {
    }

    /**
     * 删除${Remarks}
     *
     * @param requestDTO
     * @return boolean
     */
    @Override
    public boolean delete(${ClassName}RequestDTO requestDTO) {
        return false;
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