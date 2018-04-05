
#define CPU_RESTART_ADDR (uint32_t *)0xE000ED0C
#define CPU_RESTART_VAL 0x5FA0004
#define CPU_RESTART (*CPU_RESTART_ADDR = CPU_RESTART_VAL);

#include <OctoWS2811.h>

//#define NUMPIXELS      150
const int ledsperpin = 30;
DMAMEM int displayMem[ledsperpin * 6];
int drawingMem[ledsperpin * 6];
extern const uint8_t gamma8[];

int receivedArr[450];



//byte arr[500];
int incomingByte = 0;

int byteCount = 0;

typedef enum State {
  READY_TO_RECEIVE,
  RECEIVED
};

State state;




const int config = WS2811_GRB | WS2811_800kHz;

OctoWS2811 leds(ledsperpin, displayMem, drawingMem, config);

int offset = 0;


void setup() {


  leds.begin(); // This initializes the NeoPixel library.
  leds.show();

  Serial.begin(57600);
  Serial.write('!');


  state = READY_TO_RECEIVE;

//  for (int i = 0; i < sizeof(receivedArr); i++) {
//    receivedArr[i] = 0 + offset;
//  }
}

void loop() {

  switch (state) {

    case READY_TO_RECEIVE:

      while (Serial.available() > 0) {
        incomingByte = Serial.read();
        if (incomingByte == (byte)'<') {
          for (int i = 0; i < 450; i++) {
            receivedArr[i] = 0 + offset;
          }
          byteCount = 0;
          //          Serial.println("COUNT0");
        } else if (incomingByte == (byte)'>') {
          state = RECEIVED;
        }
        else {
          receivedArr[byteCount] = incomingByte + offset;
          byteCount++;
        }
      }

      break;
    case RECEIVED:

      for (int i = 0, j = -1; i < 450; i += 3, j++) {
      leds.setPixel(j, pgm_read_byte(&gamma8[receivedArr[i] / 2]), pgm_read_byte(&gamma8[receivedArr[i + 2] / 2]), pgm_read_byte(&gamma8[receivedArr[i + 1] / 2]));
      //      j++;
    }
      leds.show(); // This sends the updated pixel color to the hardware.

      state = READY_TO_RECEIVE;
      Serial.write('!');

      break;

  }



}


  const uint8_t PROGMEM gamma8[] = {
    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  1,  1,
    1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,
    2,  3,  3,  3,  3,  3,  3,  3,  4,  4,  4,  4,  4,  5,  5,  5,
    5,  6,  6,  6,  6,  7,  7,  7,  7,  8,  8,  8,  9,  9,  9, 10,
    10, 10, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 15, 15, 16, 16,
    17, 17, 18, 18, 19, 19, 20, 20, 21, 21, 22, 22, 23, 24, 24, 25,
    25, 26, 27, 27, 28, 29, 29, 30, 31, 32, 32, 33, 34, 35, 35, 36,
    37, 38, 39, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 50,
    51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 66, 67, 68,
    69, 70, 72, 73, 74, 75, 77, 78, 79, 81, 82, 83, 85, 86, 87, 89,
    90, 92, 93, 95, 96, 98, 99, 101, 102, 104, 105, 107, 109, 110, 112, 114,
    115, 117, 119, 120, 122, 124, 126, 127, 129, 131, 133, 135, 137, 138, 140, 142,
    144, 146, 148, 150, 152, 154, 156, 158, 160, 162, 164, 167, 169, 171, 173, 175,
    177, 180, 182, 184, 186, 189, 191, 193, 196, 198, 200, 203, 205, 208, 210, 213,
    215, 218, 220, 223, 225, 228, 231, 233, 236, 239, 241, 244, 247, 249, 252, 255
  };
