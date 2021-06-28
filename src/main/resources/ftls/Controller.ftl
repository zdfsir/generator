package ${Configuration.packageName}.${Configuration.path.controller};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
import ${Configuration.packageName}.${Configuration.path.entityVO}.${ClassName}VO;
import ${Configuration.packageName}.${Configuration.path.entitySearchDTO}.${ClassName}SearchDTO;
import ${Configuration.packageName}.${Configuration.path.entityRequestDTO}.${ClassName}RequestDTO;
${ServiceImport}
<#if Configuration.swaggerEnable>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.rzhkj.facade.base.base.enums.ActionTypeEnum;
import com.rzhkj.facade.base.base.enums.RoleTypeEnum;
import com.rzhkj.facade.base.base.result.Result;
import com.rzhkj.facade.base.mybatisplus.PageSelect;
import com.rzhkj.facade.base.syslog.annotation.SystemControllerLog;
import org.apache.dubbo.config.annotation.DubboReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
<#if Configuration.swaggerEnable>
@Api(value = "/${ClassAttrName}", tags = "${ClassName}管理接口")
</#if>
@RestController
@RequestMapping(value = "/${ClassMappingName}")
public class ${ControllerClassName} {

    @DubboReference(version = "${r'${project.version}'}")
    private ${ServiceClassName} ${ServiceEntityName};

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "筛选${Remarks}", notes="")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "/list")
    public Result<${ClassName}VO> list(@RequestBody(required = false) ${ClassName}SearchDTO searchDTO) {
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        List<${ClassName}VO> list = ${ServiceEntityName}.selectList(searchDTO);
        <#else><#-- mybatis或jpa模式 -->
        List<${ClassName}> list = ${ServiceEntityName}.findAll();
        </#if>
        return Result.success(list);
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "分页筛选${Remarks}", notes="")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "/page")
    public Result<IPage<${ClassName}VO>> page(@RequestBody(required = false) PageSelect<${ClassName}SearchDTO> pageSelect) {
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        IPage<${ClassName}VO> page = ${ServiceEntityName}.selectPage(pageSelect);
        <#else><#-- mybatis或jpa模式 -->
        </#if>
        return Result.success(page);
    }

<#if uniqueColumnList?size = 0>
<#if Configuration.swaggerEnable>
    @ApiOperation(value = "查看一个${Remarks}", httpMethod = "GET")
</#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @GetMapping(value = "/{id}")
    public Result<${ClassName}VO> get(@PathVariable("id") ${pkType} id) {
    <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.getById(id);
    <#else><#-- mybatis或jpa模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.get(id);
    </#if>
        ${ClassName}VO vo = BeanUtil.copyProperties(${ClassAttrName}, ${ClassName}VO.class);
        return Result.success(vo);
    }

<#else>
<#list uniqueColumnList as item>
<#if Configuration.swaggerEnable>
    @ApiOperation(value = "根据唯一键${item.propertyName}查询一个${Remarks}", notes = "")
</#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @GetMapping(value = "/{${item.propertyName}}")
    public Result<${ClassName}VO> getBy${item.propertyName?cap_first}(@PathVariable("${item.propertyName}") ${item.propertyType} ${item.propertyName}) {
        ${ClassName}VO vo = ${ServiceEntityName}.getBy${item.propertyName?cap_first}(${item.propertyName});
        return Result.success(vo);
    }

</#list>
</#if>
    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "保存${Remarks}", notes = "")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.SAVE, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "")
    public Result<Boolean> save(@RequestBody ${ClassName}RequestDTO requestDTO) {
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        boolean status = ${ServiceEntityName}.save(requestDTO);
        <#else><#-- mybatis或jpa模式 -->
        ${ServiceEntityName}.insert(${ClassAttrName});
        </#if>
        return Result.success(status);
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "删除${Remarks}", notes = "")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.DEL, roleType = RoleTypeEnum.ADMIN)
    @DeleteMapping(value = "")
    public Result<Boolean> delete(@RequestBody ${ClassName}RequestDTO requestDTO) {
    <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        boolean status = ${ServiceEntityName}.delete(requestDTO);
    <#else><#-- mybatis或jpa模式 -->
        ${ServiceEntityName}.delete(${ClassAttrName});
    </#if>
        return Result.success(status);
    }

}
