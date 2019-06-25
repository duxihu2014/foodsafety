package generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

/**
 * Created by walker on 17-7-11.
 *
 */
public class EntityGenerator
{
//    private static final Logger log = LoggerFactory.getLogger(EntityGenerator.class);

    private static String tablePrefix="t_";

    public static void generator(Connection conn, String schema, String mainPackage, String module, String...tables)  {

        for(String tableName : tables){
            tableName = tableName.toLowerCase();
            Table tablePojo = generatorSingleTable(conn, tableName,schema);
            //生成enitty
            writeJavaFile(mainPackage+".entity."+module, loadTableTemplate(tablePojo, mainPackage, module), tablePojo.getTableClassName()+".java", true);
            //生成xml
//            writeJavaFile(mainPackage+".mapper."+module, loadXmlTemplate(tablePojo, mainPackage, module), tablePojo.getTableClassName()+"Mapper.xml", false);
        }
    }

    private static void writeJavaFile(String packageName, String content, String fileName, boolean overwrite){
        String configPath = EntityGenerator.class.getClassLoader().getResource("").getFile();
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

    private static String loadTableTemplate(Table table, String mainPackage, String module){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        VelocityContext context = new VelocityContext();
        context.put("table", table);
        context.put("mainPackage", mainPackage);
        context.put("module", module);
        context.put("pkId", camelCaseName(table.getPkId()));
        StringWriter stringWriter = new StringWriter();
        engine.evaluate(context, stringWriter, "ram template", entityTemplate);
//        System.out.println(stringWriter.toString());
        return stringWriter.toString();
    }

    private static String loadXmlTemplate(Table table, String mainPackage, String module){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        VelocityContext context = new VelocityContext();
        context.put("table", table);
        context.put("mainPackage", mainPackage);
        context.put("module", module);
        context.put("pkId", camelCaseName(table.getPkId()));
        StringWriter stringWriter = new StringWriter();
        engine.evaluate(context, stringWriter, "xml template", mapperXmlTemplate);
        return stringWriter.toString();
    }

    private static Table generatorSingleTable(Connection conn, String tableName,String schema){
        String sql = "select DISTINCT * from INFORMATION_SCHEMA.COLUMNS where lower(TABLE_NAME) = lower(?) and lower(TABLE_SCHEMA)=lower(?)";
        try
        {
            Table table = new Table();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, tableName);
            statement.setString(2, schema);
            ResultSet rs = statement.executeQuery();
            List<Column> columns = Lists.newArrayList();
            Set<String> imports = Sets.newHashSet("java.io.Serializable");

            while (rs.next()){
                Column temp = new Column();
                String column_name = rs.getString("COLUMN_NAME");
                String data_type = rs.getString("DATA_TYPE");
                String is_null = rs.getString("IS_NULLABLE");
                String column_key = rs.getString("COLUMN_KEY");
                String column_comment = rs.getString("COLUMN_COMMENT");
                FieldMapping fieldMapping = FieldMapping.getByDbFieldType(data_type);
                if(fieldMapping == null){
                    System.out.println("不支持的字段类型映射,数据库类型"+data_type);
                    return null;
                }

                temp.setColumnName(column_name);
                temp.setFieldName(camelCaseName(column_name));
                if(StringUtils.isNotBlank(column_comment)){
//                    temp.setFieldComment(StringUtils.substringAfter(column_comment, "@cn "));
                    temp.setFieldComment(column_comment);
                }else {
                    temp.setFieldComment(" ");
                }
                //字段是否为空
                boolean notNull = StringUtils.equalsIgnoreCase(is_null, "NO");
                if(notNull){
                    temp.setSimpleJavaTypeName(fieldMapping.getNotNullType());
                }else {
                    temp.setSimpleJavaTypeName(fieldMapping.getClassType().getSimpleName());
                }
                List<String> columnAnnotations = Lists.newArrayList();
                //主键
                if(StringUtils.equalsIgnoreCase(column_key, "PRI")){
                    table.setPkId(column_name);
                }
                if(fieldMapping == FieldMapping.DATETIME || fieldMapping == FieldMapping.TIMESTAMP || fieldMapping == FieldMapping.DATE){
                    imports.add("java.util.Date");
                    columnAnnotations.add("@DateTimeFormat(pattern=\"yyyy-MM-dd HH:mm:ss\")");
                }
                if(fieldMapping == FieldMapping.DECIMAL){
                    imports.add("java.math.BigDecimal");
                }
                temp.setColumnAnnotations(columnAnnotations);
                temp.setSetMethodName("set" + upperCaseFirst(temp.getFieldName()));
                temp.setGetMethodName("get" + upperCaseFirst(temp.getFieldName()));
                columns.add(temp);
            }

            if(StringUtils.isNotBlank(tablePrefix)){
                table.setTableClassName(upperCaseFirst(camelCaseName(StringUtils.substringAfter(tableName, tablePrefix))));
            }else {
                table.setTableClassName(upperCaseFirst(camelCaseName(tableName)));
            }
            table.setTableAnnotation(String.format("@Table(\"%s\")", tableName));
            table.setTableName(tableName);
            table.setColumns(columns);
            table.setImports(imports);

            return table;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 驼峰变蛇底
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
     *
     * 蛇底变驼峰
     */
    public static String camelCaseName(String underscoreName) {
        underscoreName = StringUtils.lowerCase(underscoreName);
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

    public static class Table implements Serializable{
        private String tableAnnotation;
        private String tableName;
        private String tableClassName;
        private List<Column> columns;
        private Set<String> imports;
        private String pkId;

        public String getTableAnnotation()
        {
            return tableAnnotation;
        }

        public void setTableAnnotation(String tableAnnotation)
        {
            this.tableAnnotation = tableAnnotation;
        }

        public String getTableName()
        {
            return tableName;
        }

        public void setTableName(String tableName)
        {
            this.tableName = StringUtils.upperCase(tableName);
        }

        public List<Column> getColumns()
        {
            return columns;
        }

        public void setColumns(List<Column> columns)
        {
            this.columns = columns;
        }

        public String getTableClassName()
        {
            return tableClassName;
        }

        public void setTableClassName(String tableClassName)
        {
            this.tableClassName = tableClassName;
        }

        public Set<String> getImports()
        {
            return imports;
        }

        public void setImports(Set<String> imports)
        {
            this.imports = imports;
        }

        public String getPkId() {
            return pkId;
        }

        public void setPkId(String pkId) {
            this.pkId = pkId;
        }
    }

    public static class Column implements Serializable{
        private List<String> columnAnnotations;
        private String simpleJavaTypeName;
        private String fieldName;
        private String fieldComment;
        private String setMethodName;
        private String getMethodName;
        private String columnName;

        public List<String> getColumnAnnotations()
        {
            return columnAnnotations;
        }

        public void setColumnAnnotations(List<String> columnAnnotations)
        {
            this.columnAnnotations = columnAnnotations;
        }

        public String getSimpleJavaTypeName()
        {
            return simpleJavaTypeName;
        }

        public void setSimpleJavaTypeName(String simpleJavaTypeName)
        {
            this.simpleJavaTypeName = simpleJavaTypeName;
        }

        public String getFieldName()
        {
            return fieldName;
        }

        public void setFieldName(String fieldName)
        {
            this.fieldName = fieldName;
        }

        public String getSetMethodName()
        {
            return setMethodName;
        }

        public void setSetMethodName(String setMethodName)
        {
            this.setMethodName = setMethodName;
        }

        public String getGetMethodName()
        {
            return getMethodName;
        }

        public void setGetMethodName(String getMethodName)
        {
            this.getMethodName = getMethodName;
        }

        public String getFieldComment() {
            return fieldComment;
        }

        public void setFieldComment(String fieldComment) {
            this.fieldComment = fieldComment;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
    }

    public String getTablePrefix()
    {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }
    private static final String mapperXmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper\n" +
            "\t\tPUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
            "\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
            "\n" +
            "<mapper namespace=\"${mainPackage}.mapper.${module}.${table.tableClassName}Mapper\">\n" +
            "\t\n" +
            "\t<select id=\"getPage\" resultMap=\"resultPage\" parameterType=\"${mainPackage}.entity.${module}.${table.tableClassName}\">\n" +
            "\t\tselect * from ${table.tableName} t where 1=1\t\t\n" +
            "\t</select>\n" +
            "\t<resultMap type=\"${mainPackage}.entity.${module}.${table.tableClassName}\" id=\"resultPage\">\n" +
            "\t#foreach ($column in $table.columns) \n" +
            "\t\t<result property=\"${column.fieldName}\" column=\"${column.columnName}\"></result>\t\t\t\n" +
            "\t#end\n" +
            "\t</resultMap>\n" +
            "\n" +
            "</mapper>";
    private static final String entityTemplate = "package ${mainPackage}.entity.${module};\n" +
            "\n" +
            "#foreach ($import in $table.imports)\n" +
            "#if ($import)\n" +
            "import $import;\n" +
            "#else\n" +
            "\n" +
            "#end\n" +
            "#end\n" +
            "import org.apache.ibatis.annotation.myibatis.DataBase;\n" +
            "import org.apache.ibatis.annotation.myibatis.Table;\n" +
            "import org.springframework.format.annotation.DateTimeFormat;\n" +
            "\n" +
            "@Table(dataBase = DataBase.MYSQL, tabName = \"${table.tableName}\", pkId = \"${pkId}\", seqName = \"\",notColumn = {\"\"})\n" +
            "public class ${table.tableClassName} implements Serializable {\n" +
            "\n" +
            "private static final long serialVersionUID = 1L;\n" +
            "\t\n" +
            "#foreach ($column in $table.columns)\t\t\n" +
            "#foreach($columnAnnotation in $$column.columnAnnotations)  \n" +
            "\t$columnAnnotation\t\n" +
            "#end \n" +
            "\tprivate $column.simpleJavaTypeName ${column.fieldName}#if ($column.defaultValue) = ${column.defaultValueCode}#end;\n" +
            "\t\n" +
            "#end\n" +
            "#foreach ($column in $table.columns)\n" +
            "\tpublic void ${column.setMethodName}($column.simpleJavaTypeName $column.fieldName) {\n" +
            "\t\tthis.$column.fieldName = ${column.fieldName};\n" +
            "\t}\n" +
            "\t \n" +
            "\tpublic $column.simpleJavaTypeName ${column.getMethodName}() {\n" +
            "\t\treturn this.${column.fieldName};\n" +
            "\t}\n" +
            "\n" +
            "#end\n" +
            "\n" +
            "}";

    public static void main(String[] args){
        System.out.println(entityTemplate);
        System.out.println(underscoreName("userId"));
        System.out.println(camelCaseName("T_USER"));
        System.out.println(camelCaseName(StringUtils.substringAfter("t_user_role", "t_")));
        System.out.println(StringUtils.replace("com.wjj.aaa", ".", "/"));
    }
}
