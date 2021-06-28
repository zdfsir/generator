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
 * @since 2018/4/20
 */
public class DaoImplTask extends AbstractTask {

    private static DataReaderUtil dataReaderUtil = Singleton.get(DataReaderUtil.class, "DaoImplTask");

    public DaoImplTask(AbstractInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 构造Service填充数据

        EntityData entityDataObject = dataReaderUtil.reader(null, invoker);
        Map<String, Object> data = Convert.toMap(String.class, Object.class, entityDataObject);
        String filePath = FileUtil.getSourcePath() + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName())
                + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getDaoImpl());
        String fileName;
        /*
         * 根据用户是否配置了path节点下的interf属性来判断是否采用接口+实现类的方式
         */
        String serviceClassName = ConfigUtil.getConfiguration().getName().getDaoImpl().replace(Constant.PLACEHOLDER, invoker.getClassName());
        if (StringUtil.isEmpty(ConfigUtil.getConfiguration().getPath().getDaoInterface())) {
            data.put("ServiceClassName", serviceClassName);
            data.put("Implements", "");
            data.put("InterfaceImport", "");
            data.put("Override", "");
            fileName = ConfigUtil.getConfiguration().getName().getDaoImpl().replace(Constant.PLACEHOLDER, invoker.getClassName()) + ".java";
        } else {
            // Service接口实现类默认由Impl结尾
            serviceClassName = serviceClassName.contains("Impl") ? serviceClassName : serviceClassName + "Impl";
            data.put("ServiceClassName", serviceClassName);
            data.put("Implements", "implements " + ConfigUtil.getConfiguration().getName().getDaoInterface()
                    .replace(Constant.PLACEHOLDER, invoker.getClassName()));
            data.put("InterfaceImport", "import " + ConfigUtil.getConfiguration().getPackageName() + "."
                    + ConfigUtil.getConfiguration().getPath().getDaoInterface() + "."
                    + ConfigUtil.getConfiguration().getName().getDaoInterface().replace(Constant.PLACEHOLDER, invoker.getClassName()) + ";");
            data.put("Override", "\n    @Override");
            fileName = serviceClassName + ".java";
        }
        // 生成Service文件
        FileUtil.generateToJava(FreemarkerConfigUtil.TYPE_DAO_IMPL, data, filePath, fileName);
    }
}
