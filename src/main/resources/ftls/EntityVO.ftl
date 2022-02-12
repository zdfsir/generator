package ${Configuration.packageName}.${Configuration.path.entityVO};

<#if Configuration.lombokEnable>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#if Configuration.mybatisPlusEnable>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#elseif Configuration.jpaEnable>
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
</#if>
import java.io.Serializable;
import java.math.BigDecimal;

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
@ApiModel(value="${ClassName}VO", description="${Remarks} 视图")
</#if>
<#if Configuration.mybatisPlusEnable>
<#elseif Configuration.jpaEnable>
@Entity
</#if>
public class ${ClassName}VO implements Serializable {
    ${Properties}

    ${Methods}
}