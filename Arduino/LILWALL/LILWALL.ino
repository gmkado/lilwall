#define RED 0
#define GREEN 1
#define BLUE 2

#define NumRows 5
#define NumCols 1

#include <SoftwareSerial.h>  
#include "SPI.h"
#include "Adafruit_WS2801.h"

const int NumLEDs = NumRows * NumCols;
int LEDChannels[NumLEDs][3] = {0};

enum btMessageType{
  CHANGE_COLOR
};

int bluetoothTx = 2;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 3;  // RX-I pin of bluetooth mate, Arduino D3
SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

void setup() {
  bluetooth.begin(57600);  // The Bluetooth Mate defaults to 115200bps
  Serial.begin(9600);
  LedSetup();
}
 
void loop() {
 // if anything available from bluesmirf
 if(bluetooth.available())  // If the bluetooth sent any characters
 {
    int msglength = bluetooth.read();
    int message[msglength];
    Serial.println ("length of message = " + String(msglength));
    for(int i = 0; i < msglength; i++)
    {
      message[i] = bluetooth.read();
    }
    btMessageType msgtype = (btMessageType) message[0];
    switch(msgtype)
    {
      case CHANGE_COLOR:
      {
        Serial.println("message type = CHANGE_COLOR");
        Serial.println("row index = " + String(message[1]));
        Serial.println("col index = " + String(message[2]));
        int ledPos = message[1] * NumCols + message[2];
        Serial.println("ledPos = " + String(ledPos));
        Serial.println("red value = " + String(message[3]));
        LEDChannels[ledPos][RED] = message[3];
        Serial.println("green value = " + String(message[4]));
        LEDChannels[ledPos][GREEN] = message[4];
        Serial.println("blue value = " + String(message[5]));
        LEDChannels[ledPos][BLUE] = message[5];
        WriteLEDArray();
        break;
      }
      default:
        Serial.println("Message type unknown");
    }    
  }
 
}

