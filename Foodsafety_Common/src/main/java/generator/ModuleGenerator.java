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
public class ModuleGenerator
{
//    private static final Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

    private static String tablePrefix="t_";

    public static void generator(String mainPackage, String module,  String...tables)  {

        for(String tableName : tables){
            //生成service
            String className = null;
            if(StringUtils.isNotBlank(tablePrefix)){
                className = upperCaseFirst(camelCaseName(StringUtils.substringAfter(tableName, tablePrefix)));
            }else {
                className = upperCaseFirst(upperCaseFirst(camelCaseName(tableName)));
            }
            writeJavaFile(mainPackage+".restful.v1p0."+module, loadServiceTemplate(className, mainPackage, module), className+"Service.java", true);
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

    private static void writeJavaFile(String packageName, String content, String fileName, boolean overwrite){
        String configPath = ModuleGenerator.class.getClassLoader().getResource("").getFile();
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
        String configPath = ModuleGenerator.class.getClassLoader().getResource("").getFile();
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

    private static final String serviceTemplate = "package ${mainPackage}.restful.v1p0.${module};\n" +
            "\n" +
            "import ${mainPackage}.entity.PageEntity;\n" +
            "import ${mainPackage}.entity.${module}.${entity};\n" +
            "import ${mainPackage}.plugin.utils.PageList;\n" +
            "import ${mainPackage}.service.${module}.${entity}LocalService;\n" +
            "import ${mainPackage}.util.LogUtil;\n" +
            "import org.springframework.beans.BeanUtils;\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "\n" +
            "import javax.ws.rs.*;\n" +
            "import javax.ws.rs.core.MediaType;\n" +
            "import javax.ws.rs.core.Response;\n" +
            "import java.text.SimpleDateFormat;\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.List;\n" +
            "import java.util.Map;\n" +
            "\n" +
            "/**\n" +
            " * \n" +
            " * \n" +
            " * @author Administrator\n" +
            " *\n" +
            " */\n" +
            "#set ($entityVar = $entity.substring(0,1).toLowerCase() + $entity.substring(1,$entity.length())) \n" +
            "@Path(\"/${entityVar}\")\n" +
            "public class ${entity}Service {\n" +
            "\t@Autowired\n" +
            "\tprivate ${entity}LocalService ${entityVar}LocalService;\n" +
            "\n" +
            "\tSimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd\");\n" +
            "\n" +
            "\t/**\n" +
            "\t * \n" +
            "\t * @param id\n" +
            "\t * @return\n" +
            "\t */\n" +
            "\t@GET\n" +
            "\t@Path(\"/get/{id}\")\n" +
            "\t@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})\n" +
            "\tpublic Response get(@PathParam(\"id\") Long id) {\n" +
            "\t\t${entity} entity = ${entityVar}LocalService.get(id);\n" +
            "\t\treturn Response.ok(entity).build();\n" +
            "\t}\n" +
            "\n" +
            "\t/**\n" +
            "\t * 添加修改，提交表单，保存到数据库\n" +
            "\t * \n" +
            "\t * @return\n" +
            "\t * @throws Exception\n" +
            "\t */\n" +
            "\t@POST\n" +
            "\t@Path(\"/add\")\n" +
            "\t@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })\n" +
            "\tpublic Response save(@BeanParam ${entity} entity) {\n" +
            "\t\ttry {\n" +
            "\t\t\t${entityVar}LocalService.add(entity);\n" +
            "\t\t\treturn Response.ok(\"true\").build();\n" +
            "\t\t} catch (Exception ex) {\n" +
            "\t\t\tLogUtil.error(getClass(), ex.getMessage());\n" +
            "\t\t\treturn Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\t/**\n" +
            "\t * 修改\n" +
            "\t * \n" +
            "\t * @return\n" +
            "\t */\n" +
            "\t@POST\n" +
            "\t@Path(\"/update\")\n" +
            "\t@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })\n" +
            "\tpublic Response update(@BeanParam ${entity} entity) {\n" +
            "\t\ttry {\n" +
            "\t\t\t${entityVar}LocalService.update(entity);\n" +
            "\t\t\treturn Response.ok(\"true\").build();\n" +
            "\t\t} catch (Exception ex) {\n" +
            "\t\t\tLogUtil.error(getClass(), ex.getMessage());\n" +
            "\t\t\treturn Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\t/**\n" +
            "\t * 删除\n" +
            "\t * \n" +
            "\t * @param id\n" +
            "\t * @return\n" +
            "\t */\n" +
            "\t@POST\n" +
            "\t@Path(\"/delete/{id}\")\n" +
            "\t@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })\n" +
            "\tpublic Response remove(@PathParam(\"id\") long id) {\n" +
            "\t\ttry {\n" +
            "\t\t\t${entityVar}LocalService.delete(id);\n" +
            "\t\t\treturn Response.ok(\"true\").build();\n" +
            "\t\t} catch (Exception ex) {\n" +
            "\t\t\tLogUtil.error(getClass(), ex.getMessage());\n" +
            "\t\t\treturn Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();\n" +
            "\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\t@GET\n" +
            "\t@Path(\"/list/{pageSize}/{pageNo}\")\n" +
            "\t@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})\n" +
            "\tpublic Response list(@PathParam(\"pageSize\") Integer pageSize,@PathParam(\"pageNo\") Integer currentPage, @BeanParam ${entity} entityParam){\n" +
            "\t\tInteger start=currentPage==0?0:(currentPage-1)*pageSize;\n" +
            "\t\tPageList<${entity}> pageList = ${entityVar}LocalService.getPage(entityParam, start, pageSize);\n" +
            "\t\tPageEntity pe = new PageEntity();\n" +
            "\t\tpe.setStart(start);\n" +
            "\t\tpe.setCurrentPage(currentPage);\n" +
            "\t\tpe.setPageSize(pageSize);\n" +
            "\t\tpe.setTotalPageCount(pageList.getPaginator().getTotalPages());\n" +
            "\t\tpe.setTotalCount(pageList.getPaginator().getTotalItems());\n" +
            "\n" +
            "\t\tList<Map<Object,Object>> listMap=new ArrayList<Map<Object,Object>>();\n" +
            "\t\tfor (${entity} entity : pageList) {\n" +
            "\t\t\ttry {\n" +
            "\t\t\t\tlistMap.add(org.apache.commons.beanutils.BeanUtils.describe(entity));\n" +
            "\t\t\t} catch (Exception e) {\n" +
            "\t\t\t\te.printStackTrace();\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t\tpe.setItems(listMap);\n" +
            "\t\treturn Response.ok(pe).build();\n" +
            "\t}\n" +
            "}";

    public static void main(String[] args){
        System.out.println(underscoreName("userId"));
        System.out.println(camelCaseName("t_user"));
        System.out.println(camelCaseName(StringUtils.substringAfter("t_user_role", "t_")));
        System.out.println(StringUtils.replace("com.wjj.aaa", ".", "/"));
        System.out.println(serviceTemplate);

    }
}
