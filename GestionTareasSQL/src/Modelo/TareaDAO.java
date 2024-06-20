package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {
    private Connection conn;
    private Conexion conectar = new Conexion();

    public TareaDAO() {
        conn = conectar.getConnection();
    }

    public List<Tarea> listarTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String query = "SELECT * FROM tareas";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                String fecha = rs.getString("fecha");
                String estado = rs.getString("estado");
                Tarea tarea = new Tarea(id, nombre, tipo, fecha, estado);
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

  public void agregarTarea(Tarea tarea) {
    String query = "INSERT INTO tareas (nombre, tipo, fecha, estado) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, tarea.getNombre());
        pstmt.setString(2, tarea.getTipo());
        pstmt.setString(3, tarea.getFecha());
        pstmt.setString(4, tarea.getEstado());
        
        pstmt.executeUpdate();
        
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                tarea.setId(generatedKeys.getInt(1)); 
            } else {
                throw new SQLException("No se pudo obtener el ID generado automáticamente.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public void actualizarTarea(Tarea tarea) {
        String query = "UPDATE tareas SET nombre = ?, tipo = ?, fecha = ?, estado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tarea.getNombre());
            pstmt.setString(2, tarea.getTipo());
            pstmt.setString(3, tarea.getFecha());
            pstmt.setString(4, tarea.getEstado());
            pstmt.setInt(5, tarea.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarTarea(int id) {
        String query = "DELETE FROM tareas WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
