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
#include <WebSocketsClient.h>
#include "StompClient.h"



/**
* WiFi settings
**/
const char* wlan_ssid             = "Dame internet";
const char* wlan_password         = "balto001";

/**
* Stomp server settings
**/
bool useWSS                       = true;
const char* ws_host               = "192.168.0.164";
const int ws_port                 = 8080;
const char* ws_baseurl            = "/websocket/"; // don't forget leading and trailing "/" !!!

// Lock UUID
const char* lock_uuid = "18bfd86f-539e-40e2-a917-64c9ed1d42d9";

// Confusingly, on a NodeMCU board these correspond to D1 and D2
#define BUTTON1 4
#define BUTTON2 5

// VARIABLES

WebSocketsClient webSocket;

Stomp::StompClient stomper(webSocket, ws_host, ws_port, ws_baseurl, true);

void setup() {

  // Enable pullups on the GPIO pins the buttons are attached to
//  pinMode(BUTTON1, INPUT_PULLUP);
//  pinMode(BUTTON2, INPUT_PULLUP);

  // setup serial
  Serial.begin(115200);
  // flush it - ESP Serial seems to start with rubbish
  Serial.println();

  // connect to WiFi
  Serial.println("Logging into WLAN: " + String(wlan_ssid));
  Serial.print(" ...");
  WiFi.begin(wlan_ssid, wlan_password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println(" success.");
  Serial.print("IP: "); Serial.println(WiFi.localIP());

  stomper.onConnect(subscribe);
  stomper.onError(error);

  // Start the StompClient
  if (useWSS) {
    stomper.beginSSL();
  } else {
    stomper.begin();
  }
  pong();
  
}

// Once the Stomp connection has been made, subscribe to a topic
void subscribe(Stomp::StompCommand cmd) {
  Serial.println("Connected to STOMP broker");
  
  stomper.subscribe("/commands/ping.lock_uuid", Stomp::CLIENT, handlePingMessage);
  stomper.subscribe("/commands/open.lock_uuid", Stomp::CLIENT, handleOpenMessage);
  stomper.subscribe("/commands/close.lock_uuid", Stomp::CLIENT, handleCloseMessage);
  pong();
  
}

void error(const Stomp::StompCommand cmd) {
  Serial.println("ERROR: " + cmd.body);
}

Stomp::Stomp_Ack_t handlePingMessage(const Stomp::StompCommand cmd) {
  Serial.println(cmd.body);
  pong();
  return Stomp::CONTINUE;
}

Stomp::Stomp_Ack_t handleOpenMessage(const Stomp::StompCommand cmd) {
  Serial.println("Got a message on Open!");
  Serial.println(cmd.body);

  return Stomp::CONTINUE;
}

Stomp::Stomp_Ack_t handleCloseMessage(const Stomp::StompCommand cmd) {
  Serial.println("Closing!");
  Serial.println(cmd.body);

  return Stomp::CONTINUE;
}

void pong(){
  Serial.println("ponging");
  stomper.sendMessage("/esp/pong.lock_uuid", "pong");
}

//void clickB1() {
//  stomper.sendMessage("/esp/buttons", "{\\\"button\\\": 0}");
//}

void loop() {
  webSocket.loop();
}
