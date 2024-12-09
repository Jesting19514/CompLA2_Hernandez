/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package compla2_hernandez.ventanas;

import compla2_hernandez.procesos.Archivos;
import java.io.File;
import javax.swing.JTextArea;

/**
 *
 * @author team1
 */
public class Ventana extends javax.swing.JFrame {

    /**
     * Creates new form Ventana
     */
    public Ventana() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JFrameArchivo = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtContenido = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSalida = new javax.swing.JTextArea();
        lblArchivo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        mnuAbrir = new javax.swing.JMenuItem();
        Procesar = new javax.swing.JMenu();
        menuSuma = new javax.swing.JMenuItem();
        Triplos = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JFrameArchivo.setVisible(true);

        txtContenido.setColumns(20);
        txtContenido.setRows(5);
        jScrollPane1.setViewportView(txtContenido);
        txtContenido.setEditable(false);

        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        jScrollPane2.setViewportView(txtSalida);
        txtSalida.setEditable(false);

        lblArchivo.setText("Archivo");

        mnuArchivo.setText("Archivo");

        mnuAbrir.setText("Abrir");
        mnuAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAbrirActionPerformed(evt);
            }
        });
        mnuArchivo.add(mnuAbrir);

        jMenuBar1.add(mnuArchivo);

        Procesar.setText("Procesar");

        menuSuma.setText("Separar");
        menuSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSumaActionPerformed(evt);
            }
        });
        Procesar.add(menuSuma);

        Triplos.setText("Triplos");
        Triplos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TriplosActionPerformed(evt);
            }
        });
        Procesar.add(Triplos);

        jMenuItem1.setText("Cuadruplos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        Procesar.add(jMenuItem1);

        jMenuBar1.add(Procesar);

        JFrameArchivo.setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout JFrameArchivoLayout = new javax.swing.GroupLayout(JFrameArchivo.getContentPane());
        JFrameArchivo.getContentPane().setLayout(JFrameArchivoLayout);
        JFrameArchivoLayout.setHorizontalGroup(
            JFrameArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(JFrameArchivoLayout.createSequentialGroup()
                .addComponent(lblArchivo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        JFrameArchivoLayout.setVerticalGroup(
            JFrameArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFrameArchivoLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblArchivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JFrameArchivo, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JFrameArchivo)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAbrirActionPerformed
        File f = Archivos.getFile(this);
        if (f != null) {
            txtContenido.setText(Archivos.getContenido(f.getAbsolutePath()));
            lblArchivo.setText("Ruta del archivo: " + f.getAbsolutePath());
        }


    }//GEN-LAST:event_mnuAbrirActionPerformed

    private void menuSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSumaActionPerformed
        Archivos.asociaList(this);
    }//GEN-LAST:event_menuSumaActionPerformed

    private void TriplosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TriplosActionPerformed
        Archivos.triplos(this);        // TODO add your handling code here:
    }//GEN-LAST:event_TriplosActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Archivos.generarEnsambladorDesdeCuadruplos(this);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    public JTextArea getTxtContenido() {
        return txtContenido;
    }

    public JTextArea getTxtSalida() {
        return txtSalida;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame JFrameArchivo;
    private javax.swing.JMenu Procesar;
    private javax.swing.JMenuItem Triplos;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblArchivo;
    private javax.swing.JMenuItem menuSuma;
    private javax.swing.JMenuItem mnuAbrir;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JTextArea txtContenido;
    private javax.swing.JTextArea txtSalida;
    // End of variables declaration//GEN-END:variables
}
