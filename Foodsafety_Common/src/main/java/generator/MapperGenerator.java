package generator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by walker on 17-7-11.
 *
 */
public class MapperGenerator
{
//    private static final Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

    private static String tablePrefix="t_";

    public static void generator(String mainPackage, String module,  String...tables)  {

        for(String tableName : tables){
            tableName = tableName.toLowerCase();
            //生成service
            String className = null;
            if(StringUtils.isNotBlank(tablePrefix)){
                className = upperCaseFirst(camelCaseName(StringUtils.substringAfter(tableName, tablePrefix)));
            }else {
                className = upperCaseFirst(upperCaseFirst(camelCaseName(tableName)));
            }
            writeJavaFile(mainPackage+".mapper."+module, loadServiceTemplate(className, mainPackage, module), className+"Mapper.java", true);
//            writeSqlFile(loadSqlTemplate(tableName, className), StringUtils.substringAfter(tableName, tablePrefix)+".sqls", false);
        }
    }

    private static String loadServiceTemplate(String entity, String mainPackage, String module){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        VelocityContext context = new VelocityContext();
        context.put("entity", entity);
        context.put("mainPackage", mainPackage);
        context.put("module", module);
        StringWriter stringWriter = new StringWriter();
        engine.evaluate(context, stringWriter, "ram template", serviceTemplate);

        return stringWriter.toString();
    }

    private static String loadSqlTemplate(String table, String entity){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        VelocityContext context = new VelocityContext();
        context.put("table", table);
        context.put("entity", lowerCaseFirst(entity));
        StringWriter stringWriter = new StringWriter();
        engine.evaluate(context, stringWriter, "ram template", sqlTemplate);

        return stringWriter.toString();
    }

    private static void writeJavaFile(String packageName, String content, String fileName, boolean overwrite){
        String configPath = MapperGenerator.class.getClassLoader().getResource("").getFile();
        String projectDir = StringUtils.substringBefore(configPath,"target");
        String javaDir = projectDir+"src/main/java/";
        try
        {
            File javaFile = new File(javaDir+StringUtils.replace(packageName, ".", "/")+"/"+fileName);
            if(overwrite || !javaFile.exists()){
                FileUtils.writeStringToFile(javaFile, content,"utf-8");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void writeSqlFile(String content, String fileName, boolean overwrite){
        String configPath = MapperGenerator.class.getClassLoader().getResource("").getFile();
        String projectDir = StringUtils.substringBefore(configPath,"target");
        String fileDir = projectDir+"src/main/resources/sqls/";
        try
        {
            File javaFile = new File(fileDir+fileName);
            if(overwrite || !javaFile.exists()){
                FileUtils.writeStringToFile(javaFile, content,"utf-8");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 蛇底变驼峰
     */
    public static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰变蛇底
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    public static String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String lowerCaseFirst(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public String getTablePrefix()
    {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }

    private static final String sqlTemplate = "/* ${entity}.page */\n" +
            "select * from ${table}";

    private static final String serviceTemplate = "package ${mainPackage}.mapper.${module};\n" +
            "\n" +
            "import ${mainPackage}.entity.${module}.${entity};\n" +
            "import ${mainPackage}.plugin.utils.PageList;\n" +
            "import org.apache.ibatis.annotation.myibatis.GenericMapper;\n" +
            "import org.apache.ibatis.session.RowBounds;\n" +
            "\n" +
            "public interface ${entity}Mapper extends GenericMapper<${entity},Long>{\n" +
            "\n" +
            "\tPageList<${entity}> getPage(${entity} entity, RowBounds rowBounds);\n" +
            "}";

    public static void main(String[] args){
        System.out.println(underscoreName("userId"));
        System.out.println(camelCaseName("t_user"));
        System.out.println(camelCaseName(StringUtils.substringAfter("t_user_role", "t_")));
        System.out.println(StringUtils.replace("com.wjj.aaa", ".", "/"));
        System.out.println(serviceTemplate);

    }
}
