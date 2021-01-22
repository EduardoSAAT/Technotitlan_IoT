/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;
import Algoritms.Cad;
import Archivos.Text;
import Graphic.ErrorCatcher;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import panamahitek.Arduino.PanamaHitek_Arduino;

/**
 *
 * @author Ing Lalux
 */
public class Menu extends javax.swing.JFrame {
    //Variables Globales para esta Clase//
    public static String fileConfigName = "Config.txt";
    public static String fileBitacoraName = "Bitacora.txt";
    public static String FechaActual = time.AlgoritmsT.getFechaActual();
    
    //Variables del Arduino//
    public static panamahitek.Arduino.PanamaHitek_Arduino Arduino;
    public static String PuertoArduino="";
    public static String SerialArduino="";
    
    //Variables para el Monitor//
    public static Monitor monitor;
    
    //Variables para el Manager//
    public static Manager manager;
    

    /**
     * Creates new form NewJFrame
     */
    public Menu() {
        initComponents();
        
        
        
        //Iniciar el Sistema//
        String status = "";
        
        status = CheckSystem();
        if(Cad.numOfContains(status,"ERROR",true)>=1){
            textStatusGeneral.setText(status);
        }else{
            status = Sincronizar();
            if(Cad.numOfContains(status,"ERROR",true)>=1){
                textStatusGeneral.setText(status);
            }else{
                //IniciarSystem//
                status = IniciarSystem();
                if(Cad.numOfContains(status,"ERROR",true)>=1){
                    textStatusControlador.setText("ACTIVO");
                    textStatusGeneral.setText("EXITO al Iniciar Sistema");
                }
            }
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        textStatusControlador = new javax.swing.JTextField();
        textStatusGeneral = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("TECHNOTITLAN SYSTEMS - CONTROL CENTRAL OPERATIONS");

        jButton1.setText("Manager");

        jButton2.setText("Monitor");

        jLabel2.setText("Status Controlador Central");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textStatusControlador, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                    .addComponent(textStatusGeneral, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel2)
                    .addComponent(textStatusControlador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textStatusGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    
    
    
     /**
     * Descripcion: Iniciar las ventanas Automaticamente
     *
     * @return	String Error de algo o Exito
     */
    public static String IniciarSystem (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
    
	//Comenzar Proceso//
        if(condiciones==true){
            //IniciarMonitor//
                IniciarMonitor();
            //IniciarManager//
                
	}else{
            System.out.println("ERROR en IniciarSystem, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
     /**
     * Descripcion: Iniciar el Monitor del Sistema
     *
     * @return	String Error de algo o Exito
     */
    public static String IniciarMonitor(){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
    
	//Comenzar Proceso//
        if(condiciones==true){
            //IniciarMonitor//
                monitor = new Monitor();
                String mensaje = monitor.Constructor();
                
                if(Cad.numOfContains(mensaje,"ERROR",true)>=1){
                    salida = "Error en Iniciar Monitor\n\t"+mensaje;
                }else{
                    monitor.setVisible(true);
                }
	}else{
            System.out.println("ERROR en IniciarMonitor, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    
    
    
     /**
     * Descripcion: Comprobar el Archivo de Configuracion
     *
     * @return True, Archivo Existe y esta correcto, False, fallo
     */
    public static String CheckFileConfig (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            if(Text.FileExist(fileConfigName)){
                Text archivo = new Text(fileConfigName);
                
                //Comprobar la estructura del Archivo
                if(archivo.posLineLike("%CONFIG%SYSTEM%","%")==-1){
                    salida="ERROR archivo Config, no Tiene Definida ninguna configuracion del system";
                }
                
                if(archivo.posLineLike("%CONFIG%ARDUINO%","%")==-1){
                    salida="ERROR archivo Config, no Tiene Definida ninguna configuracion del arduino";
                }
            }else{
                salida="ERROR, Archivo:"+fileConfigName+" No encontrado";
            }
	}else{
            System.out.println("ERROR en CheckFileConfig, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    /**
     * Descripcion: Comprobar el Archivo de Bitacoras
     *
     * @return True, Archivo Existe y esta correcto, False, fallo
     */
    public static String CheckFileBitacora (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="ERROR...";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            if(Text.FileExist(fileBitacoraName)){
                salida="EXITO";
            }
	}else{
            System.out.println("ERROR en CheckFileBitacora, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    /**
     * Descripcion: Comprobar el sistema para poder iniciar
     *
     * @return True, Archivo Existe y esta correcto, False, fallo
     */
    public static String CheckSystem (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO en CheckSystem";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            String status = CheckFileBitacora();
            
            if(Cad.numOfContains(status,"ERROR",true)>=1){
                salida="ERROR en Archivo de Bitacora "+"\n\t"+status;
            }
            
            
            status = CheckFileConfig();
            if(Cad.numOfContains(status,"ERROR",true)>=1){
                salida="ERROR en Archivo de Configuracion "+"\n\t"+status;
            }
	}else{
            System.out.println("ERROR en CheckSystem, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    
     /**
     * Descripcion: Comprobar si el Controlador Principal es Accesible
     *
     * @return String con EXITO o ERROR
     */
    public static String CheckControladorStatus(){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            
	}else{
            System.out.println("ERROR en CheckControladorStatus, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
     /**
     * Descripcion: Actualizar la configuracion del Sistema
     *
     * @return String con EXITO o ERROR
     */
    public static String SeleccionaConfig(){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
    
	//Comenzar Proceso//
        if(condiciones==true){
            //Creacion de Variable para manejo del Archivo de CONFIG.txt
            Archivos.Text config = new Text("Config.txt");
            
            //Seleccion del Modo de Configuracion//
            String modo="DEFAULT";
                //Si el sistema no se cerro corectamente
                String line = config.getLineLike("#STATUS_CLOSE#","#");
                if(Cad.numOfContains(line,"INCORRECTO",true)>=1){
                    //Entonces escoger el modo por DEFAULT
                    modo="DEFAULT";
                }else{
                    //Entonces escoger el modo LAST_STATUS
                    modo="LAST_STATUS";
                }
            
            
            //Setear el Tipo de configuracion en el apartado LAST_STATUS//
            if(Cad.Equals(modo,"DEFAULT",true)){
                int posX= config.posLineLike("#CONFIG#SYSTEM#DEFAULT#","#");
                int posY= config.posLineLike("#FIN#CONFIG##DEFAULT#","#");
                
                int posA= config.posLineLike("#CONFIG#SYSTEM#LAST#STATUS#","#");
                int posB= config.posLineLike("#FIN#CONFIG##LAST#STATUS#","#");
                
                
                //Sistema Electrico
                    //Para el UPS_DATA_CENTER
                        String lineNew = config.getLineLikeBetween("#UPS_DATA_CENTER#","#", posX, posY);
                        int lineOLD = config.posLineLikeBetween("#UPS_DATA_CENTER#","#", posA, posB);
                        config.RemplaceLineN(lineOLD, lineNew);
                        
                    //Para el POWER_RACK
                        lineNew = config.getLineLikeBetween("#POWER_RACK#","#", posX, posY);
                        lineOLD = config.posLineLikeBetween("#POWER_RACK#","#", posA, posB);
                        config.RemplaceLineN(lineOLD, lineNew);
                    
                //Data Center
                    //Para el PC_CENTRAL
                        lineNew = config.getLineLikeBetween("#PC_CENTRAL#","#", posX, posY);
                        lineOLD = config.posLineLikeBetween("#PC_CENTRAL#","#", posA, posB);
                        config.RemplaceLineN(lineOLD, lineNew);
                    
                    //Para el PC_DATA
                        lineNew = config.getLineLikeBetween("#PC_DATA#","#", posX, posY);
                        lineOLD = config.posLineLikeBetween("#PC_DATA#","#", posA, posB);
                        config.RemplaceLineN(lineOLD, lineNew);
                
                //Sistema de Iluminacion
                    
                
                
                //Mandar Mensaje a Bitacora
                addBitacora("EXITO","Se ha establecido la configuracion: "+modo);
            }else{
                //Simplemente dejar el LAST como estaba
            }
            
            
            //EnviarConfig al controlador//
            
	}else{
            System.out.println("ERROR en ActualizarConfig, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    /**
     * Descripcion: Enviar la configuracion de LAST_STATUS al Controlador CENTRAL
     *
     */
    public void EnviarConfig(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
		//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            try {
                //Definir las variables del Archivo config.txt
                Text config = new Text(fileConfigName);
                
                //Enviar todos los elementos de la configuracion
                String mensaje;
                int posA= config.posLineLike("#CONFIG#SYSTEM#LAST#STATUS#","#");
                int posB= config.posLineLike("#FIN#CONFIG##LAST#STATUS#","#");
                
                //Sistema electrico
                    //Para el UPS_DATA_CENTER
                        mensaje = config.getLineLikeBetween("#UPS_DATA_CENTER#","#", posA, posB);
                        mensaje = "CONFIG/"+mensaje;
                        Arduino.sendData(mensaje);
                    
                    //Para el POWER_RACK
                        mensaje = config.getLineLikeBetween("#POWER_RACK#","#", posA, posB);
                        mensaje = "CONFIG/"+mensaje;
                        Arduino.sendData(mensaje);
                
                //Sistema Data Center
                    //Para PC_CENTRAL
                        mensaje = config.getLineLikeBetween("#PC_CENTRAL#","#", posA, posB);
                        mensaje = "CONFIG/"+mensaje;
                        Arduino.sendData(mensaje);
                    
                    //Para PC_DATA
                        mensaje = config.getLineLikeBetween("#PC_DATA#","#", posA, posB);
                        mensaje = "CONFIG/"+mensaje;
                        Arduino.sendData(mensaje);
                
                //Sistema de Ilumincacion
                
                
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("ERROR en EnviarConfig, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso EnviarConfig Terminado con EXITO");
    	}else{
            System.out.println("Proceso EnviarConfig Terminado con FALLO");
    	}
    }
    
    
    
    
    
     /**
     * Descripcion: Sincronizar el Sistema con el Controlador Principal
     * 
     *
     * @return String con EXITO o ERROR
     */
    public static String Sincronizar (){
    //Variables Locales e Inicializacion//
        boolean condiciones=true;
	String motivo="Indeterminado";
        String salida="EXITO";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            MakeConection();
            
            String status = SeleccionaConfig();
            if(Cad.numOfContains(status,"ERROR",true)>=1){
                salida = "ERROR al Sincronizar"+"\n\t"+status;
            }
	}else{
            System.out.println("ERROR en Sincronizar, motivo: "+motivo+", valor regresado: "+salida);
	}
    //Terminar Proceso//
        return salida;
    }
    
    
    
    
    
    /**
     * Descripcion: Procesos para hacer la conexion con el Serial de Arduino
     *
     */
    public static void MakeConection(){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            boolean Conection = false;
            
            
            //Intentar realizar la conexion, cada 1Mins
            while(Conection == false){
                try {
                    //Definir la Variable para Manejar el Arduino
                    panamahitek.Arduino.PanamaHitek_Arduino Arduino = new PanamaHitek_Arduino();
                    Arduino.arduinoRXTX(PuertoArduino,Cad.aEntero(SerialArduino,9600),Listener);
                    
                    Conection = true;
                    
                    //Agregar conexion realizada a bitacora//
                    addBitacora("EXITO","Se ha establecido conexion con el Controlador Central");
                } catch (Exception ex) {
                    textStatusControlador.setText("DESACTIVADO");
                    textStatusGeneral.setText("Error al Conectar con Arduino.... Reintentando en 1Mins");
                    
                    //Si pasa 1Hr y no se puede levantar el sistema enviar una notificacion de Alerta
                        //EnviarAlerta();
                }
                
                try {
                    //Esperar 1Mins para Reconexion
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            System.out.println("ERROR en MakeConection, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso MakeConection Terminado con EXITO");
    	}else{
            System.out.println("Proceso MakeConection Terminado con FALLO");
    	}
    }
    
    
    
    /**
     * Descripcion: Agregar un mensaje a la bitacora
     *
     * @param alerta Tipo de Alerta que se envia
     * @param mensaje El mensaje a mostrar
     */
    public static void addBitacora(String alerta, String mensaje){
    //Variables Locales e Inicializacion//
    boolean condiciones=true;
	String motivo="Indeterminado";
    //Comprobar Condiciones Iniciales//
	//no hay condiciones Iniciales
	//Comenzar Proceso//
        if(condiciones==true){
            //Variables del Archivo//
            Text bitacora = new Text(fileBitacoraName);
            String line=""; int posLine=0;
            
            //Comprobar si el registro que se va agregar cae en un bloque existente o hay que crear un nuevo bloque de timepo
            String fechaActual = time.AlgoritmsT.getFechaActual();
            int posA=bitacora.posLineLike("#FECHA#"+fechaActual+"#","#");
            if(posA>=1){
                //Entonces solo agregar el registro en esa posicion
                int posB=bitacora.posLineLike("#FIN#FECHA#"+fechaActual+"#","#");
                
                String texto = alerta+"  ("+time.AlgoritmsT.getTimeActual()+") - "+mensaje;
                bitacora.InsertLineN(posB,texto);
            }else{
                //Entonces crear el nuevo bloque de registros//
                bitacora.AgregarLine("FECHA("+fechaActual+")");
                String texto = alerta+"  ("+time.AlgoritmsT.getTimeActual()+") - "+mensaje;
                bitacora.AgregarLine(texto);
                bitacora.AgregarLine("FIN_FECHA("+fechaActual+")");
            }
        }else{
            System.out.println("ERROR en addBitacora, motivo: "+motivo);
	}
    //Terminar Proceso//
    	if(condiciones==true){
            System.out.println("Proceso addBitacora Terminado con EXITO");
    	}else{
            System.out.println("Proceso addBitacora Terminado con FALLO");
    	}
    }
    
    
    
    
    
    
    //Definicion del Objeto Listener para Sincronizar el Arduino//
    public static SerialPortEventListener Listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {
            
            //OK al Controlador Central, que todo esta bien y que avance con el monitoreo
                try {
                    Arduino.sendData("OK");
                    
                    //Mandar la instruccion de OK para continuar con el monitoreo 5 veces cada segundo
                    Thread.sleep(200);
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            //ESCUCHAR, Si el Controlador Central manda un mensaje//
            if(Arduino.isMessageAvailable()){
                //Definir la variable del Archivo//
                    Archivos.Text config = new Text("Config.txt");
                    int posA= config.posLineLike("#CONFIG#SYSTEM#LAST#STATUS#","#");
                    int posB= config.posLineLike("#FIN#CONFIG##LAST#STATUS#","#");
                
                //Leer el mensaje de Configuracion que nos entrego el Controlador//
                    String mensaje = Arduino.printMessage();
                
                    
                    
                //Si el mensaje es de CONFIG
                if(Cad.numOfContains(mensaje,"CONFIG",true)>=1){
                    //Tomar los Datos de Configuracion y enviarlos al Archivo Config.txt//
                    int posline;
                    String nuevaLinea;
                    
                    
                    //Configuracion del Sistema Electrico
                        String UPS_DATA_CENTER = Cad.subCadCadACadB(mensaje, "UPS_DATA_CENTER(", ")");
                            posline = config.posLineLikeBetween("%UPS_DATA_CENTER%","%",posA,posB);
                            nuevaLinea="\t\tUPS_DATA_CENTER("+UPS_DATA_CENTER+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                        
                            
                        String POWER_RACK = Cad.subCadCadACadB(mensaje, "POWER_RACK(", ")");
                            posline = config.posLineLikeBetween("%POWER_RACK%","%",posA,posB);
                            nuevaLinea="\t\tPOWER_RACK("+POWER_RACK+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                            
                        
                    //Configuracion de Servidores//
                        String PC_CENTRAL = Cad.subCadCadACadB(mensaje, "PC_CENTRAL(", ")");
                            posline = config.posLineLikeBetween("%PC_CENTRAL%","%",posA,posB);
                            nuevaLinea="\t\tPC_CENTRAL("+PC_CENTRAL+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                            
                            
                        String PC_DATA = Cad.subCadCadACadB(mensaje, "PC_DATA(", ")");
                            posline = config.posLineLikeBetween("%PC_DATA%","%",posA,posB);
                            nuevaLinea="\t\tPC_DATA("+PC_DATA+")";
                            config.RemplaceLineN(posline, nuevaLinea);
                            
                            
                    //Configuracion de Ilumincacion
                        
                        
                        
                    //Despues de Tomar el mensaje
                        //Agregar accion a la Bitacora//
                            addBitacora("EXITO", mensaje);
                            
                        //Pedir Refresh al Manager//
                            
                        //Pedir Refresh al Monitor//
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
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private static javax.swing.JTextField textStatusControlador;
    private static javax.swing.JTextField textStatusGeneral;
    // End of variables declaration//GEN-END:variables
}
