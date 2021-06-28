package com.greedystar.generator.task;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Singleton;
import com.greedystar.generator.dto.EntityData;
import com.greedystar.generator.entity.Constant;
import com.greedystar.generator.invoker.base.AbstractInvoker;
import com.greedystar.generator.task.base.AbstractTask;
import com.greedystar.generator.utils.*;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * @author GreedyStar
 * @since 2019/1/24
 */
public class DaoInterfaceTask extends AbstractTask {

    private static DataReaderUtil dataReaderUtil = Singleton.get(DataReaderUtil.class, "DaoInterfaceTask");

    public DaoInterfaceTask(AbstractInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 构造Service接口填充数据
        EntityData entityDataObject = dataReaderUtil.reader(null, invoker);
        Map<String, Object> data = Convert.toMap(String.class, Object.class, entityDataObject);
        String filePath = FileUtil.getSourcePath() + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName())
                + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getDaoInterface());
        String fileName = ConfigUtil.getConfiguration().getName().getDaoInterface().replace(Constant.PLACEHOLDER, invoker.getClassName()) + ".java";
        // 生成Service接口文件
        FileUtil.generateToJava(FreemarkerConfigUtil.TYPE_DAO_INTERFACE, data, filePath, fileName);
    }
}