package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
public class Conexion {
    String url="jdbc:mysql://localhost:3306/gestiontarea";
    String user="root",pass="";    
    Connection con;
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url,user,pass);
            if(con!=null){
            System.out.println("CONEXION EXITOSA");
            }
        } catch (Exception e) {  
              System.out.println(e);
        }
        return con;
    }
}
