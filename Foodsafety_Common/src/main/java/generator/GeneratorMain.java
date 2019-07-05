package generator;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by walker on 17-7-28.
 *
 */
public class GeneratorMain
{
    public static void main(String[] args){
        Connection con = 1==2?local():remote();

//        EntityGenerator.generator(con, "com.weixin.vote.entity"
//                ,"t_activity","t_activity_gift","t_activity_guice","t_activity_order",
//                "t_activity_user","t_activity_user_file","t_activity_vote_user","t_gift","t_role","t_role_user","t_user","t_city","t_activity_team"
//        		);


//        EntityGenerator.generator(con,"zhzl", "com.rwzx.governance", "organization", "t_trade_type", "t_society_economy_organization");
//        MapperGenerator.generator("com.rwzx.governance", "organization", "t_trade_type", "t_society_economy_organization");
//        ServiceGenerator.generator("com.rwzx.governance", "organization", "t_trade_type", "t_society_economy_organization");
//        ModuleGenerator.generator("com.rwzx.governance", "rectification",  "T_RECTIFICATION");
// 连接，数据库名   ，  包名  ，模块名，  表名
        EntityGenerator.generator(con,"Intelligent_Foodsafety", "com.otec.foodsafety", "equipment",  "T_EQUIPMENT_PUSHFLOW_INFO_TEST");
       // generatorAll("com.otec.foodsafety", "count",  "t_count");
    }

    private static void generatorAll(String mainPackage, String module, String...tables){
        //EntityGenerator.generator(remote(), "Intelligent_Foodsafety", mainPackage, module,  tables);
        //MapperGenerator.generator(mainPackage, module,  tables);
        //ServiceGenerator.generator(mainPackage, module,  tables);
        //ModuleGenerator.generator(mainPackage, module,  tables);
    }

    private static Connection local(){
        String url = "jdbc:mysql://106.75.119.205:3306/Intelligent_Foodsafety" ;
        String username = "root" ;
        String password = "otecgsgd2016" ;
        Connection con = null;
        try{
            con = DriverManager.getConnection(url , username , password ) ;
        }catch(Exception se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
            return null;
        }
        return con;
    }

    private static Connection remote(){
        String url = "jdbc:mysql://192.168.1.201:3306/Intelligent_Foodsafety" ;
        String username = "Intelligent_Foodsafety" ;
        String password = "Otec_123" ;
        Connection con = null;
        try{
            con = DriverManager.getConnection(url , username , password ) ;
        }catch(Exception se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
            return null;
        }
        return con;
    }
}
