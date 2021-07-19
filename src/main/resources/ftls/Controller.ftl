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
import com.rzhkj.discovery.consumer.web.tools.JwtTools;
import com.rzhkj.facade.base.base.dto.SignInAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.rzhkj.discovery.consumer.web.base.ctrl.BaseCtrl;
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
@ApiOperation(value = "${ClassName}管理接口", tags = "")
</#if>
@RestController
@RequestMapping(value = "/${ClassMappingName}")
public class ${ControllerClassName} extends BaseCtrl {

    @DubboReference(version = "${r'${project.version}'}")
    private ${ServiceClassName} ${ServiceEntityName};

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "筛选${Remarks}", notes="")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<${ClassName}VO> list(@RequestBody(required = false) ${ClassName}SearchDTO searchDTO) {
        SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        List<${ClassName}VO> list = ${ServiceEntityName}.selectList(searchDTO);
        <#else><#-- mybatis或jpa模式 -->
        List<${ClassName}> list = ${ServiceEntityName}.findAll();
        </#if>
        return Result.list(list);
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "分页筛选${Remarks}", notes="")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "/page")
    @ResponseBody
    public Result<IPage<${ClassName}VO>> page(@RequestBody(required = false) PageSelect<${ClassName}SearchDTO> pageSelect) {
        SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        IPage<${ClassName}VO> page = ${ServiceEntityName}.selectPage(pageSelect);
        <#else><#-- mybatis或jpa模式 -->
        </#if>
        return Result.page(page);
    }

<#if uniqueColumnList?size = 0>
<#if Configuration.swaggerEnable>
    @ApiOperation(value = "查看一个${Remarks}", httpMethod = "GET")
</#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<${ClassName}VO> get(@PathVariable("id") ${pkType} id) {
    SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
    <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.getById(id);
    <#else><#-- mybatis或jpa模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.get(id);
    </#if>
        ${ClassName}VO vo = BeanUtil.copyProperties(${ClassAttrName}, ${ClassName}VO.class);
        return Result.data(vo);
    }

<#else>
<#list uniqueColumnList as item>
<#if Configuration.swaggerEnable>
    @ApiOperation(value = "根据唯一键${item.propertyName}查询一个${Remarks}", notes = "")
</#if>
    @SystemControllerLog(actionType = ActionTypeEnum.QUERY, roleType = RoleTypeEnum.ADMIN)
    @GetMapping(value = "/${item.propertyName}/{${item.propertyName}}")
    @ResponseBody
    public Result<${ClassName}VO> getBy${item.propertyName?cap_first}(@PathVariable("${item.propertyName}") ${item.propertyType} ${item.propertyName}) {
        SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
        ${ClassName}VO vo = ${ServiceEntityName}.getBy${item.propertyName?cap_first}(${item.propertyName});
        return Result.data(vo);
    }

</#list>
</#if>
    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "保存${Remarks}", notes = "")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.SAVE, roleType = RoleTypeEnum.ADMIN)
    @PostMapping(value = "")
    @ResponseBody
    public Result<Boolean> save(@RequestBody ${ClassName}RequestDTO requestDTO) {
        SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        boolean status = ${ServiceEntityName}.save(requestDTO);
        <#else><#-- mybatis或jpa模式 -->
        ${ServiceEntityName}.insert(${ClassAttrName});
        </#if>
        return Result.data(status);
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "删除${Remarks}", notes = "")
    </#if>
    @SystemControllerLog(actionType = ActionTypeEnum.DEL, roleType = RoleTypeEnum.ADMIN)
    @DeleteMapping(value = "")
    public Result<Boolean> delete(@RequestBody ${ClassName}RequestDTO requestDTO) {
        SignInAccountDTO signInAccount = JwtTools.decodeTokenToAccount(request);
    <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        boolean status = ${ServiceEntityName}.delete(requestDTO);
    <#else><#-- mybatis或jpa模式 -->
        ${ServiceEntityName}.delete(${ClassAttrName});
    </#if>
        return Result.data(status);
    }
}