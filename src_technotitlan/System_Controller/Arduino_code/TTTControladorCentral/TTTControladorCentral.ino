//DEFINIR VARIABLES DEL SISTEMA//
String ms;

//CONFIG_SYSTEM
  //Metadata//
    String Fecha;
    String Hora;
    
  //Sistema Electrico - CONFIG_ELECTRICIDAD//
    bool FUENTE_CFE;
      int pinFUENTE_CFE=1;
    bool UPS_DATA_CENTER;
      int pinUPS_DATA_CENTER=2;
    bool POWER_RACK;
      int pinPOWER_RACK=3;
      
  //Servidores - CONFIG_SERVERS//
    bool PC_CENTRAL;
      int pinPC_CENTRAL=4;
    bool PC_DATA;
      int pinPC_DATA=5;
  
  //Sistema de Ilumniacion - CONFIG_ILUMINACION//
    bool LUZ_LAB_MASTER;
      int pinLUZ_LAB_MASTER;
    bool LUZ_LAB_TALLER;
      int pinLUZ_LAB_TALLER;
    bool LUZ_LAB_PASILLO_A;
      int pinLUZ_LAB_PASILLO_A;
    bool LUZ_LAB_PASILLO_B;
      int pinLUZ_LAB_PASILLO_B;
    bool LUZ_LAB_BANO;
      int pinLUZ_LAB_BANO;



//*************** CUERPO DEL MASTER_THREAD ******************

void setup() {
  //Configuracion del Sistema//
  Serial.begin(9600);

  //CONFIG_SYSTEM//
  //Sistema Electrico - CONFIG_ELECTRICIDAD//
    pinMode(pinFUENTE_CFE,INPUT);
    pinMode(pinUPS_DATA_CENTER,OUTPUT);
    pinMode(pinPOWER_RACK,OUTPUT);
      
  //Servidores - CONFIG_SERVERS//
    pinMode(pinPC_CENTRAL,OUTPUT);
    pinMode(pinPC_DATA,OUTPUT);

  //Inicializar Sistema//
  InicializarSistema();
}



void loop() {
  //Evaluar si la conexion serial funciona
  if(Serial.available()){
    //Si la conexion funciona hacer funciones de Monitoreo y Reconfig

    //Leer el mensaje del monitor Serial
    ms = Serial.readStringUntil("\n");
    //Comprobar que tipo de mensaje es Recibido
    if(ms=="OK\n"){
      //Solo hacer funciones de Monitoreo  
      Monitoreo();

      //Responder al OK- Anunciar la precencia del Controlador Central
      Serial.println("OK-Recibido");
    }else{
      //Si es un mensaje de reconfiguracion//
      if(ms.indexOf("CONFIG")>=0){
        Reconfig(ms);
      }
    }

  //Si la conexion no funciona, verificar si es correcto y hay que resucitar el sistema
  }else{
    //Verificar PC_CENTRAL//
    if(PC_CENTRAL==false){
      //Solo hacer funciones de Monitoreo
      Monitoreo();
    }else{
      //Intentar Encender el PC_CENTRAL//
      ResurekcionPC_MASTER();
    }
  }
}













//************ FUNCIONES DEL SISTEMA ****************


void InicializarSistema(){
  setDefaultConfig();
}

void setDefaultConfig(){
  //Colocar la configuracion por defecto al Sistema
  //Metadata//
    String Fecha="1/1/2020";
    String Hora="00:01";
    
  //Sistema Electrico - CONFIG_ELECTRICIDAD//
    bool FUENTE_PODER=false;
    bool POWER_RACK=false;
    
  //Servidores - CONFIG_SERVERS//
    bool PC_CENTRAL=false;
    bool PC_DATA=false;
  
  //Sistema de Ilumniacion - CONFIG_ILUMINACION//
    bool LUZ_LAB_MASTER=false;
    bool LUZ_LAB_TALLER=false;
    bool LUZ_LAB_PASILLO_A=false;
    bool LUZ_LAB_PASILLO_B=false;
    bool LUZ_LAB_BANO=false;
}


void ResurekcionPC_MASTER(){
  //Corroborar el sistema electrico Principal//
  if(CheckElectricSystemEstability()==true){
    
    //Corroborar el Sistema Electrico Secundario//
    if(UPS_DATA_CENTER==true){
      //Solo Intentar levantar la PC_CENTRAL
      setConfig_PC_CENTRAL(true);
    }else{
      //Encender UPS_DATA_CENTER primero
      digitalWrite(pinUPS_DATA_CENTER,HIGH);

      //Intentar levantar la PC_CENTRAL
      setConfig_PC_CENTRAL(true);
    }
    
  }else{
    //Si no funciona el sistema electrico, solo esperar
  }
}




//************ FUNCIONES DE MONITOREO ****************

void Monitoreo(){
  //Sistema electrico

  //Sistema de Timbres

  //Sistema de Iluminacion

  //Sistema de Enfriamiento
}


//Evaluar si el Sistema Electrico Funciona con Estabilidad//
bool CheckElectricSystemEstability(){ 
  bool sondeo1=false;
  bool sondeo2=false;
  bool sondeo3=false;
  
  if(CheckElectricSystem()==true){
    sondeo1=true;
  }else{
    sondeo1=false;  
  }
  
  //Esperar 5 Minutos para la siguiente evaluacion
  delay(300000);
  if(CheckElectricSystem()==true){
    sondeo2=true;
  }else{
    sondeo2=false;  
  }

  //Esperar 5 Minutos para la siguiente evaluacion
  delay(300000);
  if(CheckElectricSystem()==true){
    sondeo3=true;
  }else{
    sondeo3=false;  
  }

  //Evaluar el Resultado de los Sondeos//
  if(sondeo1==sondeo2==sondeo3==true){
    return true; //La red Electrica es confiable
  }else{
    return false; //La red Electrica no es confiable
  }
}


//Evaluar el Sistema Electrico Principal en el Momento CFE//
bool CheckElectricSystem(){ 
  if(digitalRead(pinFUENTE_CFE) == HIGH){
    return true;
  }else{
    return false;  
  }
}



//************ FUNCIONES DE CONFIGURACION ****************

void Reconfig(String data){
  //Sistema electrico
  if(data.indexOf("(ELECTRIC)")>=0){
    //Config UPS_DATA_CENTER
      if(data.indexOf("UPS_DATA_CENTER")>=0){
        
      }
    //Config POWER_RACK
      if(data.indexOf("POWER_RACK")>=0){
        
      }
  }else{
    //Sistema de DataCenter
    if(data.indexOf("(DATA_CENTER)")>=0){
      //Config PC_CENTRAL
        if(data.indexOf("PC_CENTRAL")>=0){
          if(data.indexOf("(ON)")>=0){
            setConfig_PC_CENTRAL(true);
          }else{
            setConfig_PC_CENTRAL(false);
          }
        }
      //Config PC_DATA
        if(data.indexOf("PC_DATA")>=0){
          
        }
    }else{
      //Sistema de Iluminacion
      if(data.indexOf("(ILUMINACION)")>=0){
        //Config.....
      }  
    }
  }
}



void setConfig_PC_CENTRAL(bool state){
  if(state==true){
    //Mandar la instruccion de Encedido
    if(PC_CENTRAL==true){
      //No hay necesidad, de reencender, ya esta encendido
    }else{
      //Entonces encender//
      PC_CENTRAL==true;
      digitalWrite(pinPC_CENTRAL, HIGH);
      delay(800);
      digitalWrite(pinPC_CENTRAL, LOW);

      //Esperar 10mins, en lo que el PC enciende e Inicia los Sistemas para continuar
      delay(6000);

      //Mandar la instruccion de Endendido
      Serial.println("SET_CONFIG/PC_CENTRAL(ON)");
    }
  }else{
    //Mandar la instruccion de Apagado
    PC_CENTRAL==false;
    Serial.println("SET_CONFIG/PC_CENTRAL(OFF)");  
  }
}
