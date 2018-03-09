/*
   AnalogInputUSB
   Read analog input from analog pin 0
   and send data to USB
*/
#include <Adafruit_NeoPixel.h>
#define NUMPIXELS      150


int ledPin = 13;
int led1 = 9;
int led2 = 10;
int led3 = 11;
//int offset = -48;
int offset = 0;

int outPin = 6;

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, outPin, NEO_GRB + NEO_KHZ800);

byte arr[500];
int incomingByte = 0;

int byteCount = 0;

typedef enum State {
  READY_TO_RECEIVE,
  RECEIVED
};

State state;

void setup() {

pinMode(outPin, OUTPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);

      pixels.begin(); // This initializes the NeoPixel library.


  Serial.begin(57600);
  Serial.write('!');



  state = READY_TO_RECEIVE;

  for (int i = 0; i < sizeof(arr); i++) {
    arr[i] =0+offset;
  }
}

void loop() {

  switch (state) {

    case READY_TO_RECEIVE:

      while (Serial.available() > 0) {
        incomingByte = Serial.read();
        //        Serial.println("INCOMING: ");

        if (incomingByte == (byte)'<') {
          for (int i = 0; i < sizeof(arr); i++) {
            arr[i] = 0+offset;
          }
          byteCount = 0;
          //          Serial.println("COUNT0");
        } else if (incomingByte == (byte)'>') {
          state = RECEIVED;
          //          Serial.print("\nsize: ");
          //          Serial.println(byteCount, DEC);
//          for (int i = 0; i < byteCount; i++) {
//            Serial.print(i, DEC);
//            Serial.println(arr[i], DEC);
//          }

        }
        else {
          arr[byteCount] = incomingByte+offset;
          byteCount++;
        }


      }


      break;
    case RECEIVED:

      for (int i = 0; i < sizeof(arr); i++) {
       pixels.setPixelColor(i, pixels.Color(arr[i*3],arr[(i*3)+1],arr[(i*3)+2])); // Moderately bright green color.
      }
    pixels.show(); // This sends the updated pixel color to the hardware.


//      analogWrite(led1, arr[0]);
//      analogWrite(led2, arr[1]);
//      analogWrite(led3, arr[2]);


      state = READY_TO_RECEIVE;
      Serial.write('!');

      break;

  }


}
