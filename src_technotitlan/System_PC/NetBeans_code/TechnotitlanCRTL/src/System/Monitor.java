/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import Algoritms.Cad;
import Archivos.Text;
import static System.Menu.Arduino;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ing Lalux
 */
public class Monitor extends javax.swing.JFrame {

    /**
     * Creates new form Monitor
     */
    public Monitor() {
        initComponents();
    }
    
    
    
    
    
     /**
     * Descripcion: Constructor del monitor
     *
     * @return	ERROR o EXITO
     */
    public String Constructor (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            //Cargar Bitacora Actual//
            
            //Cargar Grafico Alertas//
            
            //ActivarListenerMonitor//
            
	}else{
            System.out.println("ERROR en Constructor, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
     /**
     * Descripcion: Activar el Hilo Listener del Monitor
     *
     * @return	EXITO o ERROR
     */
    public String ActivarListenerMonitor (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            
	}else{
            System.out.println("ERROR en ActivarListenerMonitor, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    
    //Definicion del Objeto Listener para Sincronizar el Arduino//
    public static SerialPortEventListener listenerMonitor = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {
            if(Arduino.isMessageAvailable()){
                //Definir la variable del Archivo//
                    Archivos.Text config = new Text("Config.txt");
                
                //Leer la Fecha de Configuracion que nos entrego el Controlador//
                    String mensaje = Arduino.printMessage();
                    
                    
                //comprobar si el mensaje de CONFIG es de FECHA o de DATA o CONTROL//
                if(Cad.numOfContains(mensaje,"FECHA",true)>=1){
                    //Comparar con la Fecha de la ultima configuracion guardada en el Archivo Config.txt//
                        String  line = config.getLineLike("%CONFIG%SYSTEM%","%");

                        String FechaFILE = Cad.subCadCadACadB(line,"FECHA(",")");
                        String HoraFILE = Cad.subCadCadACadB(line,"HORA(",")");

                        String FechaMS = Cad.subCadCadACadB(line,"FECHA(",")");
                        String HoraMS = Cad.subCadCadACadB(line,"HORA(",")");

                    //Seleccionar la configuracion mas reciente//
                        if(time.AlgoritmsT.compareFechas(FechaFILE, FechaMS, "==")){
                            //Comprobar entonces las Horas
                        }else{
                            if(time.AlgoritmsT.compareFechas(FechaFILE, FechaMS, ">")){
                                //Seleccionar Config del FILE, enviarla al Archivo Config.txt//
                            }else{
                                try {
                                    //Seleccionar Config del Arduino - Pedir la configuracion Completa//
                                    Arduino.sendData("GET_CONFIG_DATA");
                                } catch (Exception ex) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                }
                
                //Si el mensaje es de DATA
                if(Cad.numOfContains(mensaje,"CONFIG",true)>=1){
                    //Tomar los Datos de Configuracion y enviarlos al Archivo Config.txt//
                    int posline;
                    String nuevaLinea;
                    //Configuracion del Sistema Electrico
                    if(Cad.numOfContains(mensaje,"CONFIG_ELECTRICIDAD",true)>=1){
                        String fuente_poder = Cad.subCadCadACadB(mensaje, "FUENTE_PODER(", ")");
                            posline = config.posLineLike("%FUENTE%PODER%","%");
                            nuevaLinea="\t\tFUENTE_PODER("+fuente_poder+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                            
                        String power_rack = Cad.subCadCadACadB(mensaje, "POWER_RACK(", ")");
                            posline = config.posLineLike("%POWER%RACK%","%");
                            nuevaLinea="\t\tPOWER_RACK("+fuente_poder+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                        
                    }
                    
                    //Configuracion de Servidores//
                    if(Cad.numOfContains(mensaje,"CONFIG_SERVERS",true)>=1){
                        String pc_central = Cad.subCadCadACadB(mensaje, "CONFIG_SERVERS(", ")");
                            posline = config.posLineLike("%PC%CENTRAL%","%");
                            nuevaLinea="\t\tPC_CENTRAL("+pc_central+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                        String pc_data = Cad.subCadCadACadB(mensaje, "PC_DATA(", ")");
                            posline = config.posLineLike("%PC%DATA%","%");
                            nuevaLinea="\t\tPC_DATA("+pc_central+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                    }
                    
                    //Configuracion de Ilumincacion
                    if(Cad.numOfContains(mensaje,"CONFIG_ILUMINACION",true)>=1){
                        String luz_lab_master = Cad.subCadCadACadB(mensaje, "LUZ_LAB_MASTER(", ")");
                        String luz_lab_taller = Cad.subCadCadACadB(mensaje, "LUZ_LAB_TALLER(", ")");
                        String luz_lab_pasillo_A = Cad.subCadCadACadB(mensaje, "LUZ_LAB_PASILLO_A(", ")");
                        String luz_lab_pasillo_B = Cad.subCadCadACadB(mensaje, "LUZ_LAB_PASILLO_B(", ")");
                        String luz_lab_bano = Cad.subCadCadACadB(mensaje, "LUZ_LAB_BANO(", ")");
                    }
                    
                    
                    //Terminar la cominicacion
                        try {
                            //Arduino.killArduinoConnection();
                        } catch (Exception ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                
                
                //Si el mensaje es de Control
                if(Cad.numOfContains(mensaje,"CONTROL",true)>=1){
                    
                    //Verificar Orden de Terminar Sincronizacion//
                    if(Cad.numOfContains(mensaje,"END SYNCRO",true)>=1){
                        try {
                            Arduino.killArduinoConnection();
                            Menu.textStatusControlador.setText("DESACTIVADO");
                        } catch (Exception ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
        }
    };
    
    
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("TECHNOTITLAN - MONITOR");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Grafico de Alertas");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("<--");

        jButton2.setText("-->");

        jTextField1.setText("jTextField1");

        jButton3.setText("GO");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jButton4.setText("HOY");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 27, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)
                        .addComponent(jButton4))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Monitor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
