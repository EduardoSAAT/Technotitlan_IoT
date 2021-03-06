/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import Algoritms.Cad;
import Archivos.Text;
import Dinamic.VectorFloat;
import Graphic.BarGraphic;
import static System.Menu.Arduino;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ing Lalux
 */
public class Monitor extends javax.swing.JFrame {
    int numEXITO=0;
    int numERROR=0;
    int numALERT=0;
    
    
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
            //Cargar General Info//
            CargarGeneralInfo();
            
            //Cargar Bitacora Actual//
            CargarBitacoraActual();
            
            //Cargar Grafico Alertas//
            CargarGrafico();
            
	}else{
            System.out.println("ERROR en Constructor, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    /**
     * Descripcion: Cargar la Bitacora de cualquier dia
     *
     * @param time Fecha para buscan en la bitacora
     */
    public void CargarBitacora(String time){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        
        Text bitacora = new Text(Menu.fileBitacoraName);
        int posTime = bitacora.posLineLike("#FECHA(#"+time+"#)#","#");
    //Comprobar Condiciones Iniciales//
    if(posTime<=0){
        condiciones=false;
        motivo="No existen registros en la bitacora con fecha:"+time;
    }
	//Comenzar Proceso//
        if(condiciones==true){
            //Agregar la Fecha Visualizada
            textFecha.setText(time);
            
            int posFinTime = bitacora.posLineLike("#FIN#FECHA(#"+time+"#)#","#");
            //Agregar todo el contenido de la bitacora
            textBitacora.setText("");
            String line;
            for(int i=posTime; i<=posFinTime; i++){
                line = bitacora.LeerLineaN(i);
                textBitacora.append(line+"\n");
            }
        }else{
            System.out.println("ERROR en CargarBitacora, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso CargarBitacora Terminado con EXITO");
    	}else{
            System.out.println("Proceso CargarBitacora Terminado con FALLO");
    	}
    }
    
    
    
    /**
     * Descripcion: Cargar la Bitacora Actual(dia de Hoy) del Sistema
     *
     */
    public void CargarBitacoraActual(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
		//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            CargarBitacora(Menu.FechaActual);
        }else{
            System.out.println("ERROR en CargarBitacoraActual, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso CargarBitacoraActual Terminado con EXITO");
    	}else{
            System.out.println("Proceso CargarBitacoraActual Terminado con FALLO");
    	}
    }
    
    
    
    
    
    /**
     * Descripcion: Cargar el grafico del sistema
     *
     */
    public void CargarGrafico(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
		//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            Dinamic.VectorFloat vectorData = new VectorFloat(3);
            
            //Agregar datos
            vectorData.addVauleRigth(numEXITO);
            vectorData.addVauleRigth(numALERT);
            vectorData.addVauleRigth(numERROR);
            
            float[] vc = new float[3];
            vc[0] = numEXITO;
            vc[1] = numALERT;
            vc[2] = numERROR;
            
            //Construir grafico
            Graphic.BarGraphic grafico = new BarGraphic(13,3,vc);
            grafico.ImprimirGrafico();
            
            //Mostrar Grafico
            textGrafico.setText("");
            
            for(int i=0; i<grafico.vectorFilas.length; i++){
                textGrafico.append(grafico.vectorFilas[i]+"\n");
            }
            textGrafico.append("EXITO - ALERT - ERROR");
        }else{
            System.out.println("ERROR en CargarGrafico, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso CargarGrafico Terminado con EXITO");
    	}else{
            System.out.println("Proceso CargarGrafico Terminado con FALLO");
    	}
    }
    
    
    
    
    
    /**
     * Descripcion: Cargar la informacion General
     *
     */
    public void CargarGeneralInfo(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
		//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            //Preparando Variables
            Text bitacora = new Text(Menu.fileBitacoraName);
            int posA = bitacora.posLineLike("#FECHA#"+time.AlgoritmsT.getFechaActual()+"#","#");
            int posB = bitacora.posLineLike("#FIN#FECHA#"+time.AlgoritmsT.getFechaActual()+"#","#");
            Text config = new Text(Menu.fileConfigName);
            
            
            String line="";
            numEXITO=0;
            numERROR=0;
            numALERT=0;
            
            
            //Conteo de Eventos
            for(int x=posA; x<=posB; x++){
                line=bitacora.LeerLineaN(x);
                
                //Numero de Modificaciones con Exito
                if(Cad.numOfContains(line,"EXITO",false)>=1){
                    numEXITO=numEXITO+1;
                }
                //Numero de Errores Dectados
                if(Cad.numOfContains(line,"ERROR",false)>=1){
                    numERROR=numERROR+1;
                }
                //Numero de Alertas
                if(Cad.numOfContains(line,"ALERT",false)>=1){
                    numALERT=numALERT+1;
                }
            }
            
            
            
            
            //Mensaje final//
            textGeneral.setText("");
            textGeneral.append("SYSTEMA MONITOR TECHNOTITLAN\n");
            
            textGeneral.append("CONFIGURACION GENERAL"+"\t\t\t\tCONFIG ARDUINO"+"\t\tCONTEO EVENTOS\n");
            textGeneral.append(config.getLineLike("#STATUS_CLOSE#","#")+"\t\t\t\tPUERTO("+Menu.PuertoArduino+")"+"\t\tOperaciones EXITO: "+numEXITO+"\n");
            textGeneral.append(config.getLineLike("#TIME_REPEAT_SEND_OK_MENSAJE#","#")+"\tSERIAL("+Menu.SerialArduino+")"+"\t\tOperaciones ALERT: "+numALERT+"\n");
            textGeneral.append(config.getLineLike("#MAX_TIME_WAIT_OK_REPLY#","#")+"\t\t"+"\t\t\t\t\tOperaciones ERROR: "+numERROR);
            
        }else{
            System.out.println("ERROR en CargarGeneralInfo, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso CargarGeneralInfo Terminado con EXITO");
    	}else{
            System.out.println("Proceso CargarGeneralInfo Terminado con FALLO");
    	}
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
        textBitacora = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textGeneral = new javax.swing.JTextArea();
        botonBACK = new javax.swing.JButton();
        botonNEXT = new javax.swing.JButton();
        textFecha = new javax.swing.JTextField();
        botonGO = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textGrafico = new javax.swing.JTextArea();
        botonHOY = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("TECHNOTITLAN - MONITOR");

        textBitacora.setColumns(20);
        textBitacora.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        textBitacora.setRows(5);
        textBitacora.setTabSize(4);
        jScrollPane1.setViewportView(textBitacora);

        jLabel2.setText("Grafico de Alertas");

        textGeneral.setColumns(20);
        textGeneral.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        textGeneral.setRows(5);
        textGeneral.setTabSize(4);
        textGeneral.setToolTipText("");
        jScrollPane2.setViewportView(textGeneral);

        botonBACK.setText("<--");
        botonBACK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBACKActionPerformed(evt);
            }
        });

        botonNEXT.setText("-->");
        botonNEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNEXTActionPerformed(evt);
            }
        });

        textFecha.setText("jTextField1");

        botonGO.setText("GO");
        botonGO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGOActionPerformed(evt);
            }
        });

        textGrafico.setColumns(20);
        textGrafico.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        textGrafico.setRows(5);
        jScrollPane3.setViewportView(textGrafico);

        botonHOY.setText("HOY");
        botonHOY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonHOYActionPerformed(evt);
            }
        });

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
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel2))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 46, Short.MAX_VALUE)
                                .addComponent(botonHOY)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonBACK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonNEXT)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonGO))
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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(botonBACK)
                        .addComponent(textFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botonNEXT)
                        .addComponent(botonGO)
                        .addComponent(botonHOY))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonHOYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonHOYActionPerformed
        CargarBitacoraActual();
    }//GEN-LAST:event_botonHOYActionPerformed

    private void botonGOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGOActionPerformed
        CargarBitacora(textFecha.getText());
    }//GEN-LAST:event_botonGOActionPerformed

    private void botonNEXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNEXTActionPerformed
        String fechaNOW = textFecha.getText();
        String fechaNew = time.AlgoritmsT.sumarDias(fechaNOW,1);
        
        textFecha.setText(fechaNew);
        CargarBitacora(fechaNew);
    }//GEN-LAST:event_botonNEXTActionPerformed

    private void botonBACKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBACKActionPerformed
        
    }//GEN-LAST:event_botonBACKActionPerformed

    
    
    /**
     * Descripcion: Funcion de Refresh para Actualizar la ventana
     *
     */
    public void Refresh(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
		//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            //Regargar General Info
                CargarGeneralInfo();
                
            //Recargar Bitacora
                CargarBitacoraActual();
            
            //Recargar Grafico
                CargarGrafico();
            
        }else{
            System.out.println("ERROR en Refresh, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso Refresh Terminado con EXITO");
    	}else{
            System.out.println("Proceso Refresh Terminado con FALLO");
    	}
    }
    
    
    
    
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
    private javax.swing.JButton botonBACK;
    private javax.swing.JButton botonGO;
    private javax.swing.JButton botonHOY;
    private javax.swing.JButton botonNEXT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea textBitacora;
    private javax.swing.JTextField textFecha;
    private javax.swing.JTextArea textGeneral;
    private javax.swing.JTextArea textGrafico;
    // End of variables declaration//GEN-END:variables
}
