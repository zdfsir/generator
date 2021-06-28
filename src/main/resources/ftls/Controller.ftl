package ${Configuration.packageName}.${Configuration.path.controller};

import ${Configuration.packageName}.${Configuration.path.entity}.${ClassName};
${ServiceImport}
<#if Configuration.swaggerEnable>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "/${ClassAttrName}")
public class ${ControllerClassName} {
    @Autowired
    private ${ServiceClassName} ${ServiceEntityName};

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "查询${ClassName}列表", httpMethod = "GET")
    </#if>
    @GetMapping(value = "")
    public Object list() {
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        List<${ClassName}> ${ClassAttrName}s = ${ServiceEntityName}.list();
        <#else><#-- mybatis或jpa模式 -->
        List<${ClassName}> ${ClassAttrName}s = ${ServiceEntityName}.findAll();
        </#if>
        Map<String, Object> result = new HashMap<>();
        result.put("data", ${ClassAttrName}s);
        result.put("status", 200);
        result.put("message", "OK");
        return result;
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "查看${ClassName}详情", httpMethod = "GET")
    </#if>
    @GetMapping(value = "/{id}")
    public Object get(@PathVariable("id") ${pkType} id) {
        <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.getById(id);
        <#else><#-- mybatis或jpa模式 -->
        ${ClassName} ${ClassAttrName} = ${ServiceEntityName}.get(id);
        </#if>
        Map<String, Object> result = new HashMap<>();
        result.put("data", ${ClassAttrName});
        result.put("status", 200);
        result.put("message", "OK");
        return result;
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "创建${ClassName}", httpMethod = "POST")
    </#if>
    @PostMapping(value = "")
    public Object post(@RequestBody ${ClassName} ${ClassAttrName}) {
        Map<String, Object> result = new HashMap<>();
        try {
            <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
            ${ServiceEntityName}.save(${ClassAttrName});
            <#else><#-- mybatis或jpa模式 -->
            ${ServiceEntityName}.insert(${ClassAttrName});
            </#if>
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }

    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "修改${ClassName}信息", httpMethod = "PUT")
    </#if>
    @PutMapping(value = "")
    public Object put(@RequestBody ${ClassName} ${ClassAttrName}) {
        Map<String, Object> result = new HashMap<>();
        try {
            <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
            ${ServiceEntityName}.updateById(${ClassAttrName});
            <#else><#-- mybatis或jpa模式 -->
            ${ServiceEntityName}.update(${ClassAttrName});
            </#if>
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }


    <#if Configuration.swaggerEnable>
    @ApiOperation(value = "删除${ClassName}", httpMethod = "DELETE")
    </#if>
    @DeleteMapping(value = "")
    public Object delete(@RequestBody ${ClassName} ${ClassAttrName}) {
        Map<String, Object> result = new HashMap<>();
        try {
            <#if Configuration.mybatisPlusEnable><#-- mybatis-plus模式 -->
            ${ServiceEntityName}.removeById(${ClassAttrName}.getId());
            <#else><#-- mybatis或jpa模式 -->
            ${ServiceEntityName}.delete(${ClassAttrName});
            </#if>
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }

}
