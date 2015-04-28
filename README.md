# Samsung-S4-ImageView-Test
To figure out why an ImageView will not load a picture from the camera or gallery, but the same code works on other Android devices.

When testing this project on a Samsung Galaxy S4 running Android 4.2.2 the ImageView turns blank when being set by a URI.

##S4

S4 Before                  |  S4 After
:-------------------------:|:-------------------------:
![S4 Before](http://i.imgur.com/oNWVpJY.png "S4 Before")  |  ![S4 After](http://i.imgur.com/HiiU2j1.png "S4 After")

However when tested using GenyMotion 2.4.0 on a device running 4.4.4, the code works:

## GenyMotion 2.4.0

GenyMotion 2.4.0/4.4.4 Before                  |  GenyMotion 2.4.0/4.4.4 After
:-------------------------:|:-------------------------:
![GenyMotion 2.4/0/4.4.4 Before](http://i.imgur.com/EJJQlqG.png "S4 Before")  |  ![GenyMotion 2.4.0/4.4.4 After](http://i.imgur.com/KDUUjgl.png "S4 After")


