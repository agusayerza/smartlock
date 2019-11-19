#include "Stepper_28BYJ_48.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

/**
 * Buttons
 */
int switch_1_pin = 10;   // SD3
int switch_2_pin = 9;    // SD2

/**
 * LED RGB
 */
int led_r = 14; // D5
int led_g = 12;  // D6
int led_b = 13;  // D7

/**
 * Step motor
 */
Stepper_28BYJ_48 stepper(5,4,0,2);  // D1 D2 D3 D4
const int steps_to_open = 400;

/**
* WiFi settings
**/
const char* wlan_ssid             = "Galaxy S8";
const char* wlan_password         = "12345678";

/**
* Server settings
**/
const char host[]               = "192.168.43.243";
const int port                 = 8080;
const char baseURL[]           = "http://192.168.43.243:8080/lock/status/18bfd86f-539e-40e2-a917-64c9ed1d42d9";

// Lock UUID
const char* lock_uuid = "18bfd86f-539e-40e2-a917-64c9ed1d42d9";


// Lock status
boolean isOpen = true;
boolean firstTime = true;
//////////////////////////////////////////////////////////////////////////////
void setup() {
  Serial.begin(115200);
  Serial.println("clean");
  connect_wifi();
  pinMode(switch_1_pin,INPUT_PULLUP);
  pinMode(switch_2_pin,INPUT_PULLUP);
  pinMode (led_r, OUTPUT);
  pinMode (led_g, OUTPUT);
  pinMode (led_b, OUTPUT);
}

void connect_wifi(){
  WiFi.begin(wlan_ssid, wlan_password); //WiFi connection
 
  while (WiFi.status() != WL_CONNECTED) { //Wait for the WiFI connection completion
    led_rgb(255,255,255);
    delay(500);
    Serial.println("Waiting for connection");
  }

  firstTime = true;
}

//////////////////////////////////////////////////////////////////////////////
void loop() {

        if(WiFi.status() == WL_CONNECTED){   //Check WiFi connection status
         
           HTTPClient http;    //Declare object of class HTTPClient
         
           http.begin(baseURL);  //Specify request destination
           http.addHeader("Content-Type", "text/plain");  //Specify content-type header
         
           int httpCode = http.POST("lock");   //Send the request
           String payload = http.getString();                  //Get the response payload
           http.end();  //Close connection
           Serial.print("Got:");
           Serial.print(httpCode);   //Print HTTP return code
           Serial.print(" payload: ");
           Serial.println(payload);    //Print request response payload
           process(payload);
         
         }else{
            Serial.println("Error in WiFi connection");   
            connect_wifi();
         }
         led_rgb(0,0,255);
         delay(200);
}

void process(String payload){
  if(payload.equals("OPEN") && (!isOpen || firstTime)){
    open_lock();
  } else if(payload.equals("CLOSE") && (isOpen || firstTime)){
    close_lock();
  }
}

void led_rgb(int r, int g, int b){
 analogWrite (led_r, r);
 analogWrite (led_g, g);
 analogWrite (led_b, b); 
}

void open_lock(){
  Serial.print("opening...");
  int i = steps_to_open;
  while(i > 0){
    i--;
    stepper.step(-1);
    led_rgb(0,255,0);
    yield();
  }
  isOpen = true;
  firstTime = false;
}

void close_lock(){
  Serial.print("closing...");
  int i = steps_to_open;
  while(i > 0){
    i--;
    stepper.step(1);
    led_rgb(255,0,0);
    yield();
  }
  isOpen = false;
  firstTime = false;
}
