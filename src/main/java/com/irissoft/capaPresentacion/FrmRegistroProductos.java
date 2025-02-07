package com.irissoft.capaPresentacion;

import com.irissoft.capaNegocios.NgCategorias;
import com.irissoft.capaNegocios.NgProductos;
import com.irissoft.capaNegocios.NgProductosConCategorias;
import com.irissoft.datos.DtProductos;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class FrmRegistroProductos extends javax.swing.JInternalFrame {
    
        // Constants
    private static final String[] COLUMN_NAMES = {"ID", "CATEGORÍA", "ACCIONES"};
    private static final Color BUTTON_COLOR = new Color(255, 54, 0);
    private static final String REMOVE_BUTTON_TEXT = "Retirar";

    // Variables de instancia
    private DefaultTableModel dtmProductos;
    private final NgProductos ngProductos;
    private final NgCategorias ngCategorias;

    public FrmRegistroProductos() {
        initComponents();
        ngCategorias = new NgCategorias();
        ngProductos = new NgProductos();
        inicializarModeloTabla();
        configurarTabla();
        inicializarListeners();
    }

    // Métodos de configuración
    private void inicializarModeloTabla() {
        dtmProductos = new DefaultTableModel();
        dtmProductos.addColumn("Id Categoria");
        dtmProductos.addColumn("NOMBRE CATEGORIAS");
        dtmProductos.addColumn("ACCIONES");
        tablaCategorias.setModel(dtmProductos);
    }

    private void configurarTabla() {
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);

        // Configurar encabezados
        JTableHeader header = tablaCategorias.getTableHeader();
        for (int i = 0; i < tablaCategorias.getColumnCount(); i++) {
            TableColumn col = tablaCategorias.getColumnModel().getColumn(i);
            DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
            headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            col.setHeaderRenderer(headerRenderer);
            col.setCellRenderer(render);
        }

        // Ocultar columna ID
        TableColumn idColumn = tablaCategorias.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setPreferredWidth(0);

        // Configurar anchos de columnas
        tablaCategorias.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaCategorias.getColumnModel().getColumn(2).setMinWidth(100);
        tablaCategorias.getColumnModel().getColumn(2).setMaxWidth(100);

        // Asignar renderizadores
        tablaCategorias.getColumn("ACCIONES").setCellRenderer(new ButtonRenderer());
        tablaCategorias.getColumn("ACCIONES").setCellEditor(new ButtonEditor(new JCheckBox()));
    }


    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setText("Retirar");
            setBackground(new Color(255, 54, 0));
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column
        ) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private final JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Retirar");
            configurarBoton();
        }

        private void configurarBoton() {
            button.setBackground(new Color(255, 54, 0));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> manejarAccionBoton());
        }

        private void manejarAccionBoton() {
            int row = tablaCategorias.getSelectedRow(); // Obtener la fila seleccionada

            if (row != -1 && row < tablaCategorias.getRowCount()) { // Verificar que la fila es válida
                int confirm = JOptionPane.showConfirmDialog(
                        button,
                        "¿Deseas retirar esta categoría de la lista?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) tablaCategorias.getModel();
                    model.removeRow(row); // Eliminar la fila seleccionada

                    // Si la tabla no tiene filas después de eliminar, no llamamos a fireEditingStopped()
                    if (model.getRowCount() > 0) {
                        int newRow = Math.min(row, model.getRowCount() - 1);
                        tablaCategorias.setRowSelectionInterval(newRow, newRow); // Seleccionar una fila válida
                    }

                    // Verificamos que la tabla aún tenga filas antes de llamar a fireEditingStopped()
                    if (model.getRowCount() > 0) {
                        fireEditingStopped(); // Finalizar edición correctamente
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        button,
                        "No hay ninguna fila seleccionada o la fila es inválida.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column
        ) {
            return button;
        }
    }

    private void actualizarPrecioTotal() {
        SwingUtilities.invokeLater(() -> {
            try {

                String cantidadStr = txtCantidad.getText();
                String precioUnitarioStr = txtPrecioUnitario.getText();

                if (!cantidadStr.isEmpty() && !precioUnitarioStr.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidadStr);
                    BigDecimal precioUnitario = new BigDecimal(precioUnitarioStr);
                    if (cantidad < 0 || precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
                        JOptionPane.showMessageDialog(this, "Los valores deben ser números positivos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        txtPrecioTotal.setText("");
                    } else {
                        BigDecimal precioTotal = precioUnitario.multiply(new BigDecimal(cantidad));
                        txtPrecioTotal.setText(precioTotal.toString());
                    }
                } else {
                    txtPrecioTotal.setText("");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos", "Error de formato", JOptionPane.ERROR_MESSAGE);
                txtCantidad.setText("");
                txtPrecioUnitario.setText("");
            }
        });
    }

    private void inicializarListeners() {
        txtPrecioUnitario.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }
        });
        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPrecioTotal();
            }
        });
    }

    private List<String> obtenerNombresCategoriasDeTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaCategorias.getModel();
        List<String> nombresCategorias = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            nombresCategorias.add(model.getValueAt(i, 1).toString());
        }
        return nombresCategorias;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPrecioUnitario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecioTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCategorias = new javax.swing.JTable();
        btnRegistrarDatos = new javax.swing.JButton();
        btnAgregarCategoria = new javax.swing.JButton();
        txtNombreProducto = new javax.swing.JTextField();
        txtNombreCategoria = new javax.swing.JTextField();

        setClosable(true);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Nombre del producto");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Cantidad");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Precio unitario");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Precio total");

        txtPrecioTotal.setEditable(false);
        txtPrecioTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioTotalActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Nombre de Categoria");

        tablaCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaCategorias);

        btnRegistrarDatos.setBackground(new java.awt.Color(0, 51, 204));
        btnRegistrarDatos.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarDatos.setText("Registrar datos");
        btnRegistrarDatos.setPreferredSize(new java.awt.Dimension(102, 22));
        btnRegistrarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarDatosActionPerformed(evt);
            }
        });

        btnAgregarCategoria.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCategoria.setText("Agregar categoria");
        btnAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnRegistrarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPrecioUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtNombreCategoria))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPrecioTotal)
                                    .addComponent(btnAgregarCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCategoria)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegistrarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPrecioTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioTotalActionPerformed

    private void btnRegistrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarDatosActionPerformed
        try {
            // Solo recolectamos los datos crudos
            String nombreProducto = txtNombreProducto.getText();
            String cantidadStr = txtCantidad.getText();
            String precioUnitarioStr = txtPrecioUnitario.getText();
            List<String> nombresCategorias = obtenerNombresCategoriasDeTabla();

            // Enviamos los datos crudos a la capa de negocio
            NgProductosConCategorias ngProductosConCategorias = new NgProductosConCategorias();
            String resultado = ngProductosConCategorias.registrarNuevoProducto(
                    nombreProducto,
                    cantidadStr,
                    precioUnitarioStr,
                    nombresCategorias
            );

            if (resultado.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Producto registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarDatosActionPerformed

    private void btnAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCategoriaActionPerformed
        String nombreCategoria = txtNombreCategoria.getText().trim();

        if (nombreCategoria.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un nombre de categoría",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generar un ID temporal para la categoría (puedes usar UUID o cualquier otro método)
        String idTemporal = UUID.randomUUID().toString();

        // Agregar la nueva fila al modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) tablaCategorias.getModel();
        model.addRow(new Object[]{
            idTemporal,
            nombreCategoria,
            "Retirar"
        });

        // Limpiar el campo de texto
        txtNombreCategoria.setText("");
        txtNombreCategoria.requestFocus();
    }//GEN-LAST:event_btnAgregarCategoriaActionPerformed

    private void limpiarCampos() {
        // Limpiar campos de texto
        txtNombreProducto.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        txtPrecioTotal.setText("");
        txtNombreCategoria.setText("");

        // Limpiar tabla de categorías
        DefaultTableModel model = (DefaultTableModel) tablaCategorias.getModel();
        model.setRowCount(0);

        // Establecer el foco en el primer campo
        txtNombreProducto.requestFocus();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCategoria;
    private javax.swing.JButton btnRegistrarDatos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCategorias;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtNombreCategoria;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioTotal;
    private javax.swing.JTextField txtPrecioUnitario;
    // End of variables declaration//GEN-END:variables
}
