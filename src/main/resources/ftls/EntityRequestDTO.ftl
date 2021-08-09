package ${Configuration.packageName}.${Configuration.path.entityRequestDTO};

<#if Configuration.lombokEnable>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#if Configuration.mybatisPlusEnable>
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#elseif Configuration.jpaEnable>
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
</#if>
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
}