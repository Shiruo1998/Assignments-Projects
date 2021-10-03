#include <LiquidCrystal.h> //申明1602液晶的函数库
#include <Stepper.h>
#include <IRremote.h>

const int RECV_PIN = 2;
IRrecv irrecv(RECV_PIN);
decode_results results;
//decode_results direct;

const int stepsPerRevolution = 200;
Stepper RStepper(stepsPerRevolution, 8,9, 10, 11);
Stepper LStepper(stepsPerRevolution, 22,24,26,28);

const int LEcho=A5;
const int LTrig=A4;
const int CEcho=A3;
const int CTrig=A2;
const int REcho=A1;
const int RTrig=A0;
unsigned long Rdistance;
unsigned long Ldistance;
unsigned long Cdistance;
byte IRstatus=0;
int i;

const int rs = 53;
const int rw = 52;
const int enable = 51;
const int d4 = 50;
const int d5 = 49;
const int d6 = 48;
const int d7 =47;
////LiquidCrystal lcd(3, 4, 7, 8, 11, 12, 13); //4数据口模式连线声明 LiquidCrystal(rs, rw, enable, d4, d5, d6, d7)
LiquidCrystal lcd(rs, rw, enable, d4, d5, d6, d7); //4数据口模式连线声明 LiquidCrystal(rs, rw, enable, d4, d5, d6, d7)

unsigned long duration;
unsigned long startPoint;

long ultrasonic_track = 0xFFA25D;
long remote_control = 0xFF629D;
long ultrasonic_avoid = 0xFFE21D;

long FOR = 0xFF18E7;
long BACK = 0xFF4AB5;
long RIGHT = 0xFF5AA5;
long RIGHT_FORWARD = 0xFF7A85;
long RIGHT_BACK = 0xFF52AD;
long LEFT = 0xFF10EF;
long LEFT_FORWARD = 0xFF30CF;
long LEFT_BACK = 0xFF42BD;
long PAUSE = 0xFF38C7;

void setup()
{
  Serial.begin(9600);
  
  irrecv.enableIRIn();
  irrecv.blink13(true);
  
  lcd.begin(16, 2);      //初始化1602液晶工作模式
  

  
  pinMode(LEcho,INPUT);
  pinMode(LTrig,OUTPUT);
  pinMode(CEcho,INPUT);
  pinMode(CTrig,OUTPUT);
  pinMode(REcho,INPUT);
  pinMode(RTrig,OUTPUT);
}
void forward()
{
    Serial.println("forward");
    for(i=0; i<50;i++){
    RStepper.setSpeed(50);
    LStepper.setSpeed(50);
    RStepper.step(1);
    LStepper.step(-1);
   }
}  
void back(){
  Serial.println("back");
      for(i=0; i<50;i++){
    RStepper.setSpeed(50);
    LStepper.setSpeed(50);
    RStepper.step(-1);
    LStepper.step(1);
    }
}
void pause()
{
  Serial.println("pause");
 // for(i=0; i<50;i++){
    RStepper.setSpeed(0);
    LStepper.setSpeed(0);
    RStepper.step(0);
    LStepper.step(0);
  //  }
  delay(300);
} 
void right(byte flag)
{
    if(flag==1)    //fast
    {
      Serial.println("fast right");
      for(i=0;i<50;i++){
      RStepper.setSpeed(50);
      LStepper.setSpeed(50);
      RStepper.step(-1);
      LStepper.step(-1);
      }
    } 
    else if(flag == 0)         //slow
    {
      Serial.println("slow right");
      for(i=0;i<50;i++){
      RStepper.setSpeed(0);
      LStepper.setSpeed(50);
      RStepper.step(0);   
      LStepper.step(-1); 
      }
    } 
    else {
      Serial.println("back right");
      for(i=0;i<50;i++){
      RStepper.setSpeed(0);
      LStepper.setSpeed(50);
      RStepper.step(0);   
      LStepper.step(1); 
      }
    }
} 
void left(byte flag)
{
    if(flag==1)    //fast
    {
      Serial.println("fast left");
      for(i=0;i<50;i++){
      RStepper.setSpeed(50);
      LStepper.setSpeed(50);
      RStepper.step(1);
      LStepper.step(1);
      }
    } 
    else if(flag == 0)       //slow
    {
      Serial.println("slow left");
      for(i=0;i<50;i++){
      RStepper.setSpeed(50);
      LStepper.setSpeed(0);
      RStepper.step(1);
      LStepper.step(0); 
      }     
    }  
    else{
      Serial.println("black left");
      for(i=0;i<50;i++){
      RStepper.setSpeed(50);
      LStepper.setSpeed(0);
      RStepper.step(-1);
      LStepper.step(0);
      }
    }
} 


void driveMotor_track(byte IRstatus)
{
  duration = millis()-startPoint;

  switch(IRstatus)
  {
    case 0:
      pause();
      if(Cdistance<=5||Ldistance<=5||Rdistance<=5){
   //     lcd.clear();
        lcd.home();
        lcd.print("Too close      ");
        lcd.print("Time: ");
        lcd.print(duration/1000);
        lcd.print("s    ");
      }
      else{
 //       lcd.clear();
        lcd.home();
        lcd.print("Out of range   ");
        lcd.setCursor(0,1);
        lcd.print("Time: ");
        lcd.print(duration/1000);
        lcd.print("s    ");
      }
      break;
    case 1:
      right(1);
    //  lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print(Rdistance);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 2:
      forward();
   //   lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print(Cdistance);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 3:
      right(0);
  //    lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print((Rdistance + Cdistance)/2);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 4:
      left(1);
   //   lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print(Ldistance);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 5:
      forward();
    //  lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print((Rdistance + Ldistance)/2);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 6:
      left(0);
   //   lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print((Ldistance + Cdistance)/2);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;
    case 7:
      forward();
   //   lcd.clear();
      lcd.home();
      lcd.print("Distance: ");
      lcd.print(Cdistance);
      lcd.print("cm  ");
      lcd.setCursor(0,1);
      lcd.print("Time: ");
      lcd.print(duration/1000);
      lcd.print("s    ");
      break;  
  }    
}  

void driveMotor_avoid(byte IRstatus)
{
//  duration = millis()-startPoint;

  switch(IRstatus)
  {
    case 0:
      if(Cdistance<=5||Ldistance<=5||Rdistance<=5)
        back();
      else{
        forward();
      }
      break;
    case 1:
      left(1);
      break;
    case 2:
      back();
      break;
    case 3:
      left(0);
      break;
    case 4:
      right(1);
      break;
    case 5:
      back();
      break;
    case 6:
      right(0);
      break;
    case 7:
      back();
      break;  
  }    
}  

int Distance_test(int Echo, int Trig) // 量出前方距离
{
    digitalWrite(Trig, LOW); // 给触发脚低电平2μs
    delayMicroseconds(2);
    digitalWrite(Trig, HIGH); // 给触发脚高电平10μs，这里至少是10μs
    delayMicroseconds(10);
    digitalWrite(Trig, LOW);               // 持续给触发脚低电
    unsigned long Fdistance = pulseIn(Echo, HIGH); // 读取高电平时间(单位：微秒)
    Fdistance = Fdistance / 58;            //为什么除以58等于厘米，  Y米=（X秒*344）/2
 //   delay(50);
    return Fdistance;
}
void ultrasonic(){
  Cdistance=Distance_test(CEcho, CTrig); 
  Serial.print("center distance: ");
  Serial.println(Cdistance);
  Rdistance=Distance_test(REcho, RTrig);
  Serial.print("Right distance: ");
  Serial.println(Rdistance);
  Ldistance=Distance_test(LEcho, LTrig);
  Serial.print("Left distance: ");
  Serial.println(Ldistance);
    IRstatus=0;  
    if(Ldistance>5 && Ldistance<25)
      IRstatus=(IRstatus+4);              
    if(Cdistance>5 && Cdistance<25)
      IRstatus=(IRstatus+2);        
    if(Rdistance>5 && Rdistance<25)
      IRstatus=(IRstatus+1);  
}
void ultra_sonic_tracking(){
  if(irrecv.decode(&results)){
    irrecv.resume();
    if( results.value == remote_control){
      lcd.home();
      lcd.print("Remote control");
      lcd.setCursor(0,1);
      lcd.print("              ");
      Serial.println("remote_control");
      while(1){
        infraread();
      }
    }
    else if(results.value == ultrasonic_avoid){
      lcd.home();
      lcd.print("ultrasonic avoid");
      lcd.setCursor(0,1);
      lcd.print("              ");
      Serial.println("ultrasonic_avoid");
      while(1){
         ultra_sonic_avoid();   
      }
    }
  }
  ultrasonic();
  driveMotor_track(IRstatus);
}
void ultra_sonic_avoid(){
  if(irrecv.decode(&results)){
    irrecv.resume();
    if( results.value == remote_control){
      lcd.home();
      lcd.print("Remote control");
      lcd.setCursor(0,1);
      lcd.print("              ");
      Serial.println("remote_control");
      while(1){
        infraread();
      }
    }
    else if(results.value == ultrasonic_track){
      Serial.println("ultrasonic_track");
      startPoint = millis();
      while(1){
         ultra_sonic_tracking();   
      }
    }    
  }
  ultrasonic();
  driveMotor_avoid(IRstatus);
}

boolean check_infraread_input(long L){
  if(L!=ultrasonic_track && L!=remote_control && L!= FOR && L!=BACK && L!=RIGHT && L!=RIGHT_FORWARD && L!=RIGHT_BACK && L!=LEFT && L!=LEFT_FORWARD && L!=LEFT_BACK &&L!=PAUSE)
    return true;
  else return false;
}

void infraread(){
       if(irrecv.decode(&results)){
          irrecv.resume();  
          Serial.println("resume1");

          if(results.value==FOR){
            forward();
            while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                forward();
            }
            infraread();
          }
          else if(results.value==BACK){
            back();
            while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                back();
            }
            infraread();
          }
          
          else if(results.value==RIGHT){
            right(0);
            while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                right(0);
            }
            infraread();
          }
          else if(results.value==RIGHT_FORWARD){ 
            right(1);
            while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                right(1);
            }
            infraread();
          }
          else if(results.value==RIGHT_BACK){
            right(-1);
             while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                right(-1);
            }
            infraread();
          }
        //    right(-1);
          else if(results.value==LEFT){
            left(0);
             while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                left(0);
            }
            infraread();
          }
  //          left(0); 
          else if(results.value==LEFT_FORWARD){
            left(1);
             while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                left(1);
            }
            infraread();
          }
   //         left(1);
          else if(results.value==LEFT_BACK){
            left(-1);
             while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                left(-1);
            }
            infraread();
          }
   //         left(-1);
          else if(results.value==PAUSE){
   //         Serial.println("enter pause");
              while(!irrecv.decode(&results)||(irrecv.decode(&results)&&check_infraread_input(results.value))){
                irrecv.resume();
                pause();
    //            Serial.println("end while");
            }
            infraread(); 
          }
 //           pause();    
          else if( results.value == ultrasonic_track ){
            startPoint = millis();
            while(1){
              ultra_sonic_tracking();
            }
          }
          else if(results.value == ultrasonic_avoid){
  //          Serial.println("ultrasonic_track");
  //          startPoint = millis();
            lcd.home();
            lcd.print("ultrasonic avoid");
            lcd.setCursor(0,1);
            lcd.print("              ");
            while(1){
              ultra_sonic_avoid();
            }
          }
       }
}

void loop()
{
  if(irrecv.decode(&results)){
    irrecv.resume();
    if( results.value == ultrasonic_track ){
      Serial.println("ultrasonic_track");
      startPoint = millis();
      while(1){
        ultra_sonic_tracking();
      }
    }
    else if( results.value == remote_control){
      
      Serial.println("remote_control");
      lcd.home();
      lcd.print("Remote control");
      lcd.setCursor(0,1);
      lcd.print("              ");
      while(1){
 //       Serial.println("enter while(1)");
        infraread();
      }
    }
    else if(results.value == ultrasonic_avoid){
      Serial.println("ultrasonic_avoid");
      lcd.home();
      lcd.print("ultrasonic avoid");
      lcd.setCursor(0,1);
      lcd.print("              ");
      while(1){
 //       Serial.println("enter while(1)");
        ultra_sonic_avoid();
      }
    }
  }
}


