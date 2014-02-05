#define datapin 11 // DI
#define latchpin 9// LI
#define enablepin 10 // EI
#define clockpin 13 // CI

#define RED 0
#define GREEN 1
#define BLUE 2

#define NumRows 5
#define NumCols 1

#include <SoftwareSerial.h>  

const int NumLEDs = NumRows * NumCols;
int LEDChannels[NumLEDs][3] = {0};
int SB_CommandMode;
int SB_RedCommand;
int SB_GreenCommand;
int SB_BlueCommand;

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
   // read address
   // read RGB value
   
   // set LedChannels
   // write out
 
 
}



void LedSetup()
{
   pinMode(datapin, OUTPUT);
   pinMode(latchpin, OUTPUT);
   pinMode(enablepin, OUTPUT);
   pinMode(clockpin, OUTPUT);
   SPCR = (1<<SPE)|(1<<MSTR)|(0<<SPR1)|(0<<SPR0);
   digitalWrite(latchpin, LOW);
   digitalWrite(enablepin, LOW);
}

void SB_SendPacket() {
 
    if (SB_CommandMode == B01) {
     SB_RedCommand = 120;
     SB_GreenCommand = 100;
     SB_BlueCommand = 100;
    }
 
    SPDR = SB_CommandMode << 6 | SB_BlueCommand>>4;
    while(!(SPSR & (1<<SPIF)));
    SPDR = SB_BlueCommand<<4 | SB_RedCommand>>6;
    while(!(SPSR & (1<<SPIF)));
    SPDR = SB_RedCommand << 2 | SB_GreenCommand>>8;
    while(!(SPSR & (1<<SPIF)));
    SPDR = SB_GreenCommand;
    while(!(SPSR & (1<<SPIF)));
 
}

void WriteLEDArray() {
 
    SB_CommandMode = B00; // Write to PWM control registers
    for (int h = 0;h<NumLEDs;h++) {
	  SB_RedCommand = LEDChannels[h][0];
	  SB_GreenCommand = LEDChannels[h][1];
	  SB_BlueCommand = LEDChannels[h][2];
	  SB_SendPacket();
          Serial.println("LED " + String(h));
    }
 
    delayMicroseconds(15);
    digitalWrite(latchpin,HIGH); // latch data into registers
    delayMicroseconds(15);
    digitalWrite(latchpin,LOW);
 
    SB_CommandMode = B01; // Write to current control registers
    for (int z = 0; z < NumLEDs; z++) SB_SendPacket();
    delayMicroseconds(15);
    digitalWrite(latchpin,HIGH); // latch data into registers
    delayMicroseconds(15);
    digitalWrite(latchpin,LOW);
 
}
