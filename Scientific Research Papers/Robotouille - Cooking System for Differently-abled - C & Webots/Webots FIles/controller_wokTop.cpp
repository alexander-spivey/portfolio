#include <webots/Keyboard.hpp>
#include <webots/GPS.hpp>
#include <webots/InertialUnit.hpp>

#define TIME_STEP 64
using namespace webots;

int main(int argc, char ** argv)
{
  Robot *robot = new Robot();
  Keyboard kb;
  
  Motor *lr;
  lr = robot->getMotor("linearArm")
  
  kb.enable(TIME_STEP);
  while(robot->step(TIME_STEP) != -1) {
   int key = kb.getKey();
    
   if(key == 87) 
   {
     linear += 0.005;
   } 
   else if (key == 832) 
   {
     linear += -0.005;
   }
   else
   {
     linear += 0.000;
   }
   lr->setPosition(linear);
}