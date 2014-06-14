#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>
#include "distance.h"
#define TRUE 1
 
#define TRIG 11
#define ECHO 10
 
void setup() {
        wiringPiSetup();
        pinMode(TRIG, OUTPUT);
        pinMode(ECHO, INPUT);
		
        //TRIG pin must start LOW
        digitalWrite(TRIG, LOW);
        digitalWrite(ECHO, LOW);
        delay(30);
}
 
int getCM() {
        //Send trig pulse
        digitalWrite(TRIG, HIGH);
        delayMicroseconds(20);
        digitalWrite(TRIG, LOW);
 
        while(digitalRead(ECHO) == LOW);
 
        //Wait for echo end
        long startTime = micros();
        while(digitalRead(ECHO) == HIGH);
        long travelTime = micros() - startTime;
 
        //Get distance in cm
        int distance = travelTime / 58;
 
        return distance;
}
JNIEXPORT jint JNICALL Java_Sonar_getDistance (JNIEnv *env, jclass obj)
	{
	 
	return getCM();
	}
JNIEXPORT void JNICALL Java_Sonar_setup (JNIEnv *env, jclass obj)
	{
		 setup();
	}

 /*
int main(void) {
        setup();
 
        printf("Distance: %dcm\n", getCM());
 
        return 0;
}
*/
