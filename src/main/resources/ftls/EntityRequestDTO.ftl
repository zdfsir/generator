package ${Configuration.packageName}.${Configuration.path.entityRequestDTO};

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
import com.rzhkj.facade.base.core.dto.SignInAccountDTO;

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
@ApiModel(value="${ClassName}RequestDTO", description="${Remarks} 请求对象")
</#if>
<#if Configuration.mybatisPlusEnable>
<#elseif Configuration.jpaEnable>
@Entity
</#if>
public class ${ClassName}RequestDTO implements Serializable {
    ${Properties}

    ${Methods}

    @ApiModelProperty(value="当前登录操作人", notes="", required = true, hidden = true)
    private SignInAccountDTO signInAccount;
}