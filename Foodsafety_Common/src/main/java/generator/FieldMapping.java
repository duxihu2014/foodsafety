package generator;



import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by walker on 17-7-11.
 *
 */
public enum  FieldMapping
{
    SMALLINT("smallint",Long.class,"long"), INT("int",Long.class,"long"), MEDIUMINT("mediumint",Long.class,"long"), STRING("varchar",String.class,null), DATETIME("datetime", Date.class,null),TIMESTAMP("timestamp", Date.class,null),DATE("date", Date.class,null),
    LONG("bigint", Long.class,"long"),FLOAT("float", Float.class,"float"),DECIMAL("decimal", BigDecimal.class,null),INTEGER("tinyint", Integer.class,"int"), CHAR("char",String.class,null),
    LONGTEXT("longtext",String.class,null),TEXT("text",String.class,null),TINYTEXT("tinytext",String.class,null),MEDIUMTEXT("mediumtext",String.class,null),//double float...没有的再加
    LONGBLOB("longblob",Byte[].class,"byte[]");
    FieldMapping(String dbFieldType, Class classType, String notNullType){
        this.dbFieldType = dbFieldType;
        this.classType = classType;
        this.notNullType = notNullType!=null?notNullType:classType.getSimpleName();
    }

    private String dbFieldType;
    private Class classType;
    private String notNullType;

    public static FieldMapping getByDbFieldType(String dbFieldType){
        FieldMapping[] arr = FieldMapping.values();
        for(FieldMapping item : arr){
            if(StringUtils.equalsIgnoreCase(dbFieldType, item.getDbFieldType())){
                return item;
            }
        }
        return null;
    }

    public String getDbFieldType()
    {
        return dbFieldType;
    }

    public void setDbFieldType(String dbFieldType)
    {
        this.dbFieldType = dbFieldType;
    }

    public Class getClassType()
    {
        return classType;
    }

    public void setClassType(Class classType)
    {
        this.classType = classType;
    }

    public String getNotNullType()
    {
        return notNullType;
    }

    public void setNotNullType(String notNullType)
    {
        this.notNullType = notNullType;
    }

}
