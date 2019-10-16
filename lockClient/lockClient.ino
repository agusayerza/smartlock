/**
 * SpringStompButtons.ino
 * 
 * This example monitors two buttons attached to an ESP8266 device and sends a message to a STOMP server
 * when they are pressed.
 * 
 * Works best when used with the Spring Websockets+Stomp example code: 
 * 
 * 
 * Connect a normally-open button between ESP D1 and ground, and between ESP D2 and ground.
 * 
 * Author: Duncan McIntyre <duncan@calligram.co.uk>
 * 
 */

#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Servo.h>

/**
* WiFi settings
**/
const char* wlan_ssid             = "Dame internet";
const char* wlan_password         = "balto001";

/**
* Server settings
**/
const char host[]               = "192.168.0.164";
const int port                 = 8080;
const char baseURL[]           = "http://192.168.0.164:8080/lock";

// Lock UUID
const char* lock_uuid = "18bfd86f-539e-40e2-a917-64c9ed1d42d9";


//Servo
Servo servo;

void setup() {
 
  Serial.begin(115200); //Serial connection
  connect_wifi();
  
  servo.attach(4); //D2
  servo.write(90);
 
}

void connect_wifi(){
    WiFi.begin(wlan_ssid, wlan_password); //WiFi connection
 
  while (WiFi.status() != WL_CONNECTED) { //Wait for the WiFI connection completion
 
    delay(500);
    Serial.println("Waiting for connection");
 
  }
}
 
void loop() {
 delay(2500);
 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status
 
   HTTPClient http;    //Declare object of class HTTPClient
 
   http.begin(baseURL);  //Specify request destination
   http.addHeader("Content-Type", "text/plain");  //Specify content-type header
 
   int httpCode = http.POST("Message from ESP8266");   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
   http.end();  //Close connection
   servo.write(0);
 
 }else{
    Serial.println("Error in WiFi connection");   
    connect_wifi();
 }
 
  delay(2500);  //Send a request every 30 seconds
  servo.write(360);
}