// File: controller_wokTop1.cpp
// Date: Summer REU 2021 UTA
// Description: Controller for the wok top
// Author: Alexander Spivey
// Modifications:

// You may need to add webots include files such as
// <webots/DistanceSensor.hpp>, <webots/Motor.hpp>, etc.
// and/or to add some other includes
#include <webots/Keyboard.hpp>
#include <webots/GPS.hpp>
#include <webots/InertialUnit.hpp>
#include <webots/Robot.hpp>
#include <webots/Motor.hpp>
#include <webots/Led.hpp>
#include <webots/Camera.hpp>
#include <string>
#include <iostream>
#define TIME_STEP 128
using namespace webots;
  
int main(int argc, char ** argv)
{
  Robot *robot = new Robot(); //Create a reference to the robot
  Keyboard kb; //Reference to keyboard for user input
  
  Motor *lr; //motor arm 1
  lr = robot->getMotor("linearArm");
  
  Motor *lr2; //motor arm 2
  lr2 = robot->getMotor("linearArm2");
  
  LED *led1; //green led
  led1 = robot->getLED("LEDInterface");
  
  LED *led2; //red led
  led2 = robot->getLED("LEDInterface1");
  
  Camera *cam; //webcam
  cam = robot->getCamera("Webcam");
  cam->enable(TIME_STEP);
  
  Camera *cam1; //pancam
  cam1 = robot->getCamera("Pancam");
  cam1->enable(TIME_STEP);
  
  Speaker *speaker;
  speaker = robot->getSpeaker("Speaker");
  //speaker->playSound(Speaker *left, Speaker *right, const std::string &sound, double volume, double pitch, double balance, bool loop);
  //int width = cam->
  
  kb.enable(TIME_STEP);
  
  double linear = 0;
  double zlinear = 0;
  int mode1 = 1;
  int mode2 = 1;
  
  /*Arm Linear Movement*/
  int W = 87;
  int A = 65;
  int S = 83;
  int D = 68;
  
  /*Arm Diagonal Movement*/
  int Q = 81;
  int E = 69;
  int Z = 90;
  int C = 67;
  
  /*Interface Controls*/
  int G = 71;
  int R = 82;
  int X = 88;
  
  
  while(robot->step(TIME_STEP) != -1) 
  {
   int key = kb.getKey(); // w & s
   //int key2 = kb.getKey(); // a & d
   //int key3 = kb.getKey() //q, e, z, c
   /*Interface Code*/
   if (key == G)
   { //turn light on and off
     if(mode2 == 1)
     {
       led2->set(1); 
       mode2 = 0;
     }
     else
     {
       led2->set(0);
       mode2 = 1;
     }
   }
   else if (key == R)
   {
     if(mode1 == 1)
     {
       led1->set(1); 
       mode1 = 0;
     }
     else
     {
       led1->set(0);
       mode1 = 1;
     }
   }
   else if (key == X)
   {
     std::cout << "BUZZ" << std::endl;
   }
   
   /*Linear and Diagonal Motor Code*/
   if ((key == E && linear < 0.09 && zlinear < 0.09)) 
   { //pressing w & d, needs to go up and outwards
     linear += 0.05;
     zlinear += 0.05;
   }
   else if ((key == Q && linear < 0.09 && zlinear > 0.01))
   { // pressing w & a, needs to go up and inwards
     linear += 0.05;
     zlinear += -0.05;
   }
   else if ((key == C && linear > 0.01 && zlinear < 0.09))
   {//pressing s & d, needs to go down and outwards
     linear += -0.05;
     zlinear += 0.05;
   }
   else if ((key == Z && linear > 0.01 && zlinear > 0.01))
   {//pressing s & a, needs to go down and inwards
     linear += -0.05;
     zlinear += -0.05;
   }
   else if (key == W && linear < 0.09)
   {//pressing w goes up
     linear += 0.05;
   } 
   else if (key== S && linear > 0.01)
   {//pressing s goes down
     linear += -0.05;
   }
   else if (key == D && zlinear < 0.09) 
   {//pressing d goes right
     zlinear += 0.05;
   }
   else if (key == A && zlinear > 0.01)
   {//pressing a goes left
     zlinear += -0.05;
   }
   else 
   {//do nothing
     linear+=0;
     zlinear+=0;
   }
   
   /*Setters*/
   lr->setPosition(linear);
   lr2->setPosition(zlinear);
  }
  
  delete robot;
  return 0;
}