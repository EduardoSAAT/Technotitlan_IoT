//DEFINIR VARIABLES DEL SISTEMA//
String ms;

//CONFIG_SYSTEM
  //Resurekcion - System//
    long timeLastRespond;
    long timeActual;
    long diference;
    long timeMaxWait;

  //Sistema de Enfriamiento//
    long timeLastFrio;
    long timeActualFrio;
    long diferenceFrio;
    long timeWaitFrio;
    long timeStillFrio;
    long timeLastStillFrio;
    long timeActualStillFrio;
    long diferenceStillFrio;
    bool VENTILADOR;
    int pinVENTILADOR=1;

  //Sistema de Timbres y Alarmas - Seguridad//
    long timeLastVoice;
    long timeActualVoice;
    long diferenceVoice;
    long timeStillVoice;
    long timeLastStillVoice;
    long timeActualStillVoice;
    long diferenceStillVoice;
    bool BOCINA;
    int pinTIMBRE=2;
    int pinBOCINA=3;
    
  //Sistema Electrico - CONFIG_ELECTRICIDAD//
    bool FUENTE_CFE;
      int pinFUENTE_CFE=4;
    bool UPS_DATA_CENTER;
      int pinUPS_DATA_CENTER=5;
    bool POWER_RACK;
      int pinPOWER_RACK=6;
      
  //Servidores - CONFIG_SERVERS//
    bool PC_CENTRAL;
      int pinPC_CENTRAL=7;
    bool PC_DATA;
      int pinPC_DATA=8;
  
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

  //Sistema de enfriamiento//
    pinMode(pinVENTILADOR,OUTPUT);

  //Sistema de Timbre y Alarmas//
    pinMode(pinTIMBRE,INPUT);
    pinMode(pinBOCINA,OUTPUT);

  //Inicializar Sistema//
  InicializarSistema();
}



void loop() {
  //Evaluar su hay algun mensaje en el Serial
  if(Serial.available()){
    //Actualizar el tiempo de este ultimo mensaje
    timeLastRespond = millis();

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

  //Si no hay ningun mensaje en el Serial
  }else{
    //Verificar PC_CENTRAL//
    if(PC_CENTRAL==false){
      //Solo hacer funciones de Monitoreo
      Monitoreo();
    }else{
      //Evaluar la resurekcion del sistema
      EvaluarResurekcion();
    }
  }
}













//************ FUNCIONES DEL SISTEMA ****************


void InicializarSistema(){
  setDefaultConfig();
}

void setDefaultConfig(){
  //Colocar la configuracion por defecto al Sistema

  //Resurekcion - System//
    timeMaxWait = 120000;
    timeActual = millis();
    diference = 0;
    timeLastRespond = timeActual;

  //Sistema de enfriamiento//
    VENTILADOR = false;
    timeLastFrio = timeActual;
    timeActualFrio = timeActual;
    timeWaitFrio=3600000;
    timeStillFrio=120000;
    diferenceFrio = 0;
    timeLastStillFrio=timeActual;
    timeActualStillFrio=timeActual;
    diferenceStillFrio=0;

  //Sistema de Timbres y Alarmas//
    timeLastVoice=timeActual;
    timeActualVoice=timeActual;
    diferenceVoice=0;
    timeStillVoice=15000;
    BOCINA=false;
   
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


void EvaluarResurekcion(){
    timeActual = millis();
    diference = timeLastRespond-timeActual;

    if(diference>timeMaxWait){
      //Mandar Resurekcion//
      ResurekcionPC_MASTER();
    }
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
  CheckElectricSystem();

  //Sistema de Timbres
  CheckTimbreSystem();
  CheckBOCINA();
  
  
  //Sistema de Enfriamiento
  CheckEnfriamientoSystem();
  
  //Sistema de Iluminacion
}



//Evaluar la estabilidad del sistema electrico de la CFE//
bool CheckElectricSystemEstability(){ 
  bool sondeo1=false;
  bool sondeo2=false;
  bool sondeo3=false;
  
  if(CheckElectricCFE()==true){
    sondeo1=true;
  }else{
    sondeo1=false;  
  }
  
  //Esperar 5 Minutos para la siguiente evaluacion
  delay(300000);
  if(CheckElectricCFE()==true){
    sondeo2=true;
  }else{
    sondeo2=false;  
  }

  //Esperar 5 Minutos para la siguiente evaluacion
  delay(300000);
  if(CheckElectricCFE()==true){
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
bool CheckElectricCFE(){ 
  if(digitalRead(pinFUENTE_CFE) == HIGH){
    return true;
  }else{
    return false;  
  }
}


//Evaluar el sistema electricpo y tomar desiciones
void CheckElectricSystem(){
  if(CheckElectricCFE==false){
    //Si no funciona - mandar a apagar todo
    setConfig_PC_DATA(false);
    setConfig_PC_CENTRAL(false);
  }
}


//Evaluar el sistema de enfriamiento
void CheckEnfriamientoSystem(){
  //Si el ventilador esta Activado - comprobar tiempo de Activacion
  if(VENTILADOR){
    //Obtener la diferencia de tiempo para manter
    timeActualStillFrio=millis();
    diferenceStillFrio=timeLastStillFrio-timeActualStillFrio;

    //Si ya paso el tiempo de enfriamiento terminar frio//
    if(diferenceStillFrio>timeStillFrio){
      digitalWrite(pinVENTILADOR,LOW);
      VENTILADOR = true;
      timeLastFrio=timeActualStillFrio;
    }
  }else{
    //Si no esta activado - comprobar tiempo para ser Activado
    //Obtener la diferencia de tiempo
    timeActualFrio = millis();
    diferenceFrio = timeLastFrio - timeActualFrio;
  
    //Si ya paso el tiempo maximo activar enfriamiento
    if(diferenceFrio>timeWaitFrio){
      digitalWrite(pinVENTILADOR,HIGH);
      VENTILADOR = true;
    }
  }
}


void CheckTimbreSystem(){
  //Si se Activa el timbre mandar Alerta
  if(digitalRead(pinTIMBRE) == HIGH){
    //Activar la bocina
    setConfig_BOCINA(true);

    //Mandar mensaje al PC_CENTRAL
    Serial.println("ALERT - Timbre Activado");
  }
}



//Evaluar sistema de vocina para dejarlo encendo o mandarlo a apagar
void CheckBOCINA(){
  //Si la vocina esta activada - comprobar tiempo de Activacion
  if(BOCINA){
    //Obtener la diferencia de tiempo para manter
    timeActualStillVoice=millis();
    diferenceStillVoice=timeLastStillVoice-timeActualStillVoice;

    //Si ya paso el tiempo de Sonido, apagar//
    if(diferenceStillVoice>timeStillVoice){
      setConfig_BOCINA(false);
    }
  }
}


//************ FUNCIONES DE CONFIGURACION ****************

void Reconfig(String data){
  //Sistema electrico
  if(data.indexOf("(ELECTRIC)")>=0){
    
    //Config UPS_DATA_CENTER
      if(data.indexOf("UPS_DATA_CENTER")>=0){
        if(data.indexOf("(ON)")>=0){
          setConfigUPS_DATA_CENTER(true);
        }else{
          setConfigUPS_DATA_CENTER(false);
        }
      }
      
    //Config POWER_RACK
      if(data.indexOf("POWER_RACK")>=0){
        if(data.indexOf("(ON)")>=0){
          setConfigPOWER_RACK(true);
        }else{
          setConfigPOWER_RACK(false);
        }
      }
  }else{
    
    //Sistema de PC_CENTRAL
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
          if(data.indexOf("(ON)")>=0){
            setConfig_PC_DATA(true);
          }else{
            setConfig_PC_DATA(false);
          }
        }
        
    }else{
      
      //Sistema de Iluminacion
      if(data.indexOf("(ILUMINACION)")>=0){
        //Config.....
      }  
      
    }
  }
}






void setConfigUPS_DATA_CENTER(bool state){
    if(state==true){
      digitalWrite(pinUPS_DATA_CENTER, HIGH);
      UPS_DATA_CENTER = true;
      Serial.println("SET_CONFIG/UPS_DATA_CENTER(ON)");
    }else{
      //Comprobar previamente que los sistemas que dependen de este, esten apagados
      if((PC_CENTRAL == false) && (PC_DATA == false)){
        //Mandar a apagar el UPS
        digitalWrite(pinUPS_DATA_CENTER, LOW);
        UPS_DATA_CENTER = false;
        Serial.println("SET_CONFIG/UPS_DATA_CENTER(OFF)");
      }else{
        //Mandar mensaje de alerta, imposible apagar
        if(PC_DATA == true){
          Serial.println("ALERTA - Imposible apagar UPS_DATA_CENTER: Sistema dependiente ACTIVO: PC_DATA"); 
        }
        if(PC_CENTRAL == true){
          Serial.println("ALERTA - Imposible apagar UPS_DATA_CENTER: Sistema dependiente ACTIVO: PC_CENTRAL"); 
        }
      }
    }
}


void setConfigPOWER_RACK(bool state){
    //Comprobar previamente que los sistemas que dependen de este, esten apagados
    if(state==true){
      digitalWrite(pinPOWER_RACK, HIGH);
      POWER_RACK = true;
      Serial.println("SET_CONFIG/POWER_RACK(ON)");
    }else{
      //Mandar a apagar el UPS
      digitalWrite(pinPOWER_RACK, LOW);
      POWER_RACK = false;
      Serial.println("SET_CONFIG/POWER_RACK(OFF)");
    }
}


void setConfig_PC_CENTRAL(bool state){
  if(state==true){
    //Mandar la instruccion de Encedido
    if(PC_CENTRAL==true){
      //No hay necesidad, de reencender, ya esta encendido
      Serial.println("SET_CONFIG-PC_CENTRAL(ON)PRUEBA");
    }else{
      //Entonces encender//
      PC_CENTRAL=true;
      digitalWrite(pinPC_CENTRAL, HIGH);
      delay(800);
      digitalWrite(pinPC_CENTRAL, LOW);

      //Esperar a leer algun mensaje
      Serial.readStringUntil("\n");
      Serial.println("SET_CONFIG/PC_CENTRAL(ON)"); 
    }
  }else{
    //Mandar la instruccion de Apagado
    PC_CENTRAL=false;
    Serial.println("SET_CONFIG/PC_CENTRAL(OFF)");  
  }
}



void setConfig_PC_DATA(bool state){
  if(state==true){
    //Mandar la instruccion de Encedido
    if(PC_DATA==true){
      //No hay necesidad, de reencender, ya esta encendido
      Serial.println("SET_CONFIG/PC_DATA(ON)");
    }else{
      //Entonces encender//
      PC_DATA=true;
      digitalWrite(pinPC_DATA, HIGH);
      delay(800);
      digitalWrite(pinPC_DATA, LOW);

      //Mandar la instruccion de Endendido
        Serial.println("SET_CONFIG/PC_DATA(ON)");
    }
  }else{
    //Mandar la instruccion de Apagado
    PC_DATA=false;
    Serial.println("SET_CONFIG/PC_DATA(OFF)");  
  }
}


//Activar o Desactivar VOCINA DEL TIMBRE
void setConfig_BOCINA(bool state){
  if(state==true){
    digitalWrite(pinBOCINA,HIGH);
    BOCINA=true;
  }else{
    digitalWrite(pinBOCINA,LOW);
    BOCINA=false;
  }
}
