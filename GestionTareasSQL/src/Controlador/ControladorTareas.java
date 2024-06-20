package Controlador;

import Modelo.Tarea;
import Modelo.TareaDAO;
import Vista.TareasVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorTareas {
    private TareasVista vista;
    private TareaDAO tareaDAO;
    private DefaultTableModel tableModel;

    public ControladorTareas(TareasVista vista, TareaDAO tareaDAO) {
        this.vista = vista;
        this.tareaDAO = tareaDAO;
        this.tableModel = (DefaultTableModel) vista.getTableTareas().getModel();
        initListeners();
        actualizarTabla();
    }

    private void initListeners() {
        vista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarTarea();
            }
        });

        vista.getBtnNuevaTarea().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        vista.getBtnEditar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarTarea();
            }
        });

        vista.getBtnUpdate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTarea();
            }
        });

        vista.getBtnDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTarea();
            }
        });
    }

    private void guardarTarea() {
        String nombre = vista.getTxtNombreTarea().getText();
        String tipo = vista.getComboBoxTipoTarea().getSelectedItem().toString();
        String fecha = vista.getTxtFecha().getText();
        String estado = vista.getComboBoxEstado().getSelectedItem().toString();
        
        Tarea tarea = new Tarea(0, nombre, tipo, fecha, estado);
        tareaDAO.agregarTarea(tarea);
        actualizarTabla();
        limpiarCampos();
    }

    private void editarTarea() {
        int selectedRow = vista.getTableTareas().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Tarea tarea = tareaDAO.listarTareas().stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElse(null);
            if (tarea != null) {
                vista.getTxtNombreTarea().setText(tarea.getNombre());
                vista.getComboBoxTipoTarea().setSelectedItem(tarea.getTipo());
                vista.getTxtFecha().setText(tarea.getFecha());
                vista.getComboBoxEstado().setSelectedItem(tarea.getEstado());
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró la tarea seleccionada.");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Selecciona una tarea para editar.");
        }
    }

    private void actualizarTarea() {
        int selectedRow = vista.getTableTareas().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nombre = vista.getTxtNombreTarea().getText();
            String tipo = vista.getComboBoxTipoTarea().getSelectedItem().toString();
            String fecha = vista.getTxtFecha().getText();
            String estado = vista.getComboBoxEstado().getSelectedItem().toString();
            
            Tarea tarea = new Tarea(id, nombre, tipo, fecha, estado);
            tareaDAO.actualizarTarea(tarea);
            actualizarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Selecciona una tarea para actualizar.");
        }
    }

    private void eliminarTarea() {
        int selectedRow = vista.getTableTareas().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            tareaDAO.eliminarTarea(id);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Selecciona una tarea para eliminar.");
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0);
        List<Tarea> tareas = tareaDAO.listarTareas();
        for (Tarea tarea : tareas) {
            tableModel.addRow(new Object[]{tarea.getId(), tarea.getNombre(), tarea.getTipo(), tarea.getFecha(), tarea.getEstado()});
        }
    }

    private void limpiarCampos() { 
        vista.getTxtNombreTarea().setText("");
        vista.getComboBoxTipoTarea().setSelectedIndex(0);
        vista.getTxtFecha().setText("D / M / Y");
        vista.getComboBoxEstado().setSelectedIndex(0); // Asegúrate de limpiar también el combo de estado si es necesario
    }
}
