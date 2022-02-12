package ${Configuration.packageName}.${Configuration.path.entitySearchDTO};

import com.rzhkj.base.core.dto.SearchDTO;
<#if Configuration.mybatisPlusEnable>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#elseif Configuration.jpaEnable>
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
</#if>
<#if Configuration.lombokEnable>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

import java.io.Serializable;

/**
 * ${Remarks}
 *
 * @author ${Configuration.author}
 * @date ${.now?date}
 */
<#if Configuration.lombokEnable>
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="${ClassName}SearchDTO", description="${Remarks} 搜索请求对象")
</#if>
<#if Configuration.mybatisPlusEnable>
<#elseif Configuration.jpaEnable>
@Entity
</#if>
public class ${ClassName}SearchDTO implements Serializable {

    @ApiModelProperty(value = "筛选日期")
    private SearchDTO.RangeDTO.Calendar.DateTime dateTimeRange;

    @ApiModelProperty(required = false, value = "是否查询扩展参数（默认不查询）", notes = "")
    private Boolean searchExtend = false;
}