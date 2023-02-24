package Utils;


import Model.Service.Log.Log;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;
@SuppressWarnings("all")
public class JDBCUtil {

//    private static  String url;
//    private static  String user;
//    private static  String password;
//    private static  String driver;
    private static DataSource ds;
    /**
     * 文件的读取，只需要读取一次即可拿到这些值。使用静态代码块
     */
    static{
        //读取资源文件，获取值。
        try {
            //1. 创建Properties集合类。
            Properties pro = new Properties();

            //获取src路径下的文件的方式--->ClassLoader 类加载器
//            ClassLoader classLoader = JDBCUtil.class.getClassLoader();
//            URL res  = classLoader.getResource("jdbc.properties");
//            String path = res.getPath();
//            path = path.replaceAll("%e6%a1%8c%e9%9d%a2","桌面");
            //因为放在桌面上，编码会出现乱码

            //            //2. 加载文件
//            //或使用绝对路径
//            pro.load(new FileReader(path));
//
//            //3. 获取数据，赋值
//            url = pro.getProperty("url");
//            user = pro.getProperty("username");
//            password = pro.getProperty("password");
//            driver = pro.getProperty("driverClassName");
//            //4. 注册驱动
//            Class.forName(driver);

            //数据库连接池
            pro.load(JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            //2.获取DataSource
            ds = DruidDataSourceFactory.createDataSource(pro);

        }catch (Exception e) {
            e.printStackTrace();
            Log.LOGER().error("获取数据库连接池错误");
        }
    }

    /**
     * 私有化避免错误创建实例化对象
     */
    private JDBCUtil() {
    }

    /**
     * 获取连接
     * @return 连接对象
     */
    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, password);
        return ds.getConnection();
    }

    /**
     * 获取数据库连接池
     * @return
     */
    public static DataSource getDataSource(){
        return ds;
    }

    /**
     * 抽取数据库查询功能，返回结果集
     */
    public static ResultSet query(Connection con, PreparedStatement st, ResultSet rs, String sql, Object[] params) throws SQLException {
        //避免sql注入问题
        st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
        }
        rs = st.executeQuery();
        return rs;
    }

    /**
     * 抽取数据库增删改
     * @param con
     * @param sql
     * @param params
     * @param rs
     * @param st
     * @return
     * @throws SQLException
     */
    public static int update(Connection con, String sql, Object[] params, ResultSet rs, PreparedStatement st) throws SQLException {
        //解除sql语句注入
        st = con.prepareStatement(sql);

        //通过数组的不断循环进行注入
        for (int i = 0; i < params.length; i++) {
            st.setObject(i + 1, params[i]);
        }
        return st.executeUpdate();
    }

    /**
     * 释放资源
     * @param stmt
     * @param conn
     */
    public static void close(Statement stmt,Connection conn){
        //简化代码
        close(null,stmt,conn);
    }
    /**
     * 释放资源，重载形式
     * @param stmt
     * @param conn
     */
    public static void close(ResultSet rs,Statement stmt, Connection conn){
        if( rs != null){
            try {
                rs.close();
                rs = null;
                //防止对数据库操作对象的误操作
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if( stmt != null){
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if( conn != null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

