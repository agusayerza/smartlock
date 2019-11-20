# SmartLock

# Contents
- 1 - [Introduction](#1-introduction)
  - 1.1 - [Apps](#11-apps)
- 2 - [End user usage](#2-end-user-usage)
- 3 - [Compiling](#3-compiling)
  - 3.1 - [Building prerequisites](#31-building-prerequisites)
    - 3.1.1 - [NodeMCU App prerequisites](#311-nodemcu-app-prerequisites)
    - 3.1.2 - [Flutter App](#312-flutter-app-prerequisites)
    - 3.1.3 - [Server](#313-springboot-server-prerequisites)
  - 3.2 - [Compiling and running](#32-compiling-and-running)
    - 3.2.1 - [NodeMCU App](#321-nodemcu-app)
    - 3.2.2 - [Flutter App](#322-flutter-app)
    - 3.2.3 - [Server](#323-springboot-server-app)
- 4 - [Docs](#4docs)
 
---

# 1. Introduction
**SmartLock** is a project that aims to create a NodeMCU SmartLock that querries a server - made in Spring Boot - and that can be controlled through and iPhone as well as Android app, made in flutter. As such is composed by 3 different compiles, dependant on eachother.
## 1.1 Apps
| Application Name         | Location on project | Description                                                                                                                                                 |
|--------------------------|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SmartLock Flutter App    | ~/lock_app/         | Application that supports both Android and iOS, made in Flutter, used with your server account to access and manage your locks.                             |
| SmartLock Spring Server  | ~/server/           | Application containing a Spring Boot server that is used to control access to the locks, as well as opening and closing them. It also word as an auth door. |
| SmartLock Arduino Client | ~/lockClient/       | Arduino-build app that runs on the Locks to query the server and open or close the lock on request.                                                         |

---

# 2. End user usage

To use your SmartLock simply download the app from the AppStore or PlayStore. Register a new account and use the lock, as simple as that.
- 1 Download the App from the AppStore or PlayStore
- 2 Modify /lockClient/lockClient.ino wifi config on lines 27 and 28 to match the desired one and compile it to the ESP8266
- 3 Register a new account if you don't have one
- 4 Login to the new account
- 5 Add the lock by navigating to `Locks -> Add Lock` and entering a Name as well as the lock UUID
- 6 The lock is added an ready to use by navigating to `Locks -> name of the new lock`
 ---
# 3 Compiling

Should you want to use your own version of the compilied apps, this are the steps used to compile them.

## 3.1 Building prerequisites
### 3.1.1 NodeMCU App prerequisites
- Arduino IDE  > https://www.arduino.cc/en/Main/Software
- ESP8266 USB Driver > http://arduino.esp8266.com/stable/package_esp8266com_index.json (add to your Android IDE board manager)
- Stepper_By_J Stepper motor library > https://github.com/thomasfredericks/Stepper_28BYJ_48`

### 3.1.2 Flutter App prerequisites
- Flutter 1.7.8 > https://flutter.dev/docs/get-started/install
- Dart 2.4.0 > https://dart.dev/get-dart

(all other dependencies are covered in pubspec.yaml)

### 3.1.3 Springboot Server prerequisites
- Gradle
- Java JDK 1.8, all other dependencies will be covered by Gradle

## 3.2 Compiling and Running
### 3.2.1 NodeMCU App
Compile through the Arduino IDE and load into a ESP8266.

### 3.2.2 Flutter App
```flutter run``` to run it on an emulator.
```flutter build apk --split-per-abi``` to generate the APKs.

### 3.2.3 Springboot Server App
As simple as ```gradle build```, and then run the generated JAR file

---

# 4 Docs
## 4.1 Server documentation
  Server documentation is available at https://agusayerza.github.io/smartlock/


