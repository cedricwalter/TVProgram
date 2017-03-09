![tvprogram](tvprogram.png)

# TVProgram

## Introduction
This program is able to get the french TV program and read by a Text to Speech (TTS) engine when pressing on a button. 
This is especially useful for blind or disabled people.

It use MaryTTS (https://github.com/marytts/marytts), an open-source, multilingual Text-to-Speech Synthesis platform written in Java. It was originally developed as a collaborative project of DFKI’s Language Technology Lab and the Institute of Phonetics at Saarland University. It is now maintained by the Multimodal Speech Processing Group in the Cluster of Excellence MMCI and DFKI.
As of version 5.2, MaryTTS supports German, British and American English, French, Italian, Luxembourgish, Russian, Swedish, Telugu, and Turkish; more languages are in preparation. MaryTTS comes with toolkits for quickly adding support for new languages and for building unit selection and HMM-based synthesis voices.

This java program can run on a raspberry pi 2/3. 

## Currently
* TVProgram support fetching any RSS feed, a plugin for the french TV program (https://webnext.fr/programme-tv-rss) is provided
* RSS is cached for more efficiency
* A TVGuide is build and then read by MaryTTS http://mary.dfki.de/
* time like 13:15 will be converted in english to "Quarter past One" or "treize heure et quart"

## TVGuide
* TVGuide now: give you the actual TVProgram on all channels running now +- x minutes
* TVGuideFromTO: configurable guide, for example all TVProgram tonight on all channels

## Configuration
* Free and Premium channel can be filtered out
* Sentences are templates driven (http://freemarker.org/) for easy customization and translation to something else than french
* Voices can be configured, for french "upmc-pierre-hsmm" or "enst-camille-hsmm" is recommended

## Usages

Get tv program started 5 minutes ago or in the next 10 minutes
`java -jar tvprogram-1.0-SNAPSHOT-shaded.jar now 5 10`

Get tv program starting between 20:00 and 22:00
`java -jar tvprogram-1.0-SNAPSHOT-shaded.jar program "20:00" "22:00"`

# Raspberry PI

## Install Raspbian
Raspbian is the Foundation’s official supported operating system. see https://www.raspberrypi.org/downloads/raspbian/

## Install JAVA
Open a terminal and execute the following commands:

```
sudo su -
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
apt-get update
apt-get install oracle-java8-installer
java -version #verify if java was installed correctly
apt-get install oracle-java8-set-default
exit
```

## get sound out

```
pi@raspberrypi:~ $ lsusb
Bus 001 Device 005: ID 2001:330d D-Link Corp.
Bus 001 Device 006: ID 045e:00dd Microsoft Corp. Comfort Curve Keyboard 2000 V1.0
Bus 001 Device 004: ID 046d:0a19 Logitech, Inc.
Bus 001 Device 003: ID 0424:ec00 Standard Microsystems Corp. SMSC9512/9514 Fast Ethernet Adapter
Bus 001 Device 002: ID 0424:9514 Standard Microsystems Corp.
Bus 001 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
```

.asoundrc does take precedence over asound.conf as it loads on user log in. You normally would only use one or the other, not both. Use asound.conf if more than user use the PI

For my Logitech Z205 USB speaker

`/home/pi/.asoundrc`
with
```defaults.ctl.card 1 
defaults.pcm.card 1 
defaults.timer.card 1
```

or

`/etc/asound.conf`
with
```
pcm.!default {
type hw
card 2
}
ctl.!default {
type hw
card 2
}
```

usb sound card ready to use, run alsamixer

`alsamixer`

set the speaker volume , mic and make sure that speaker not muted, test configuration using

`speaker-test`

it will make  pink noise sound at your speaker or use 

`sudo aplay /usr/share/sounds/alsa/Front_Center.wav`

now
sudo vi /usr/share/alsa/alsa.conf

## Get Release and Run

`wget tvprogram-1.0-SNAPSHOT-shaded.jar`

see "Usages"

## Configure


## Recurring
Open a terminal and setup a cron job with the command `crontab -e` (for user specific job) or `sudo crontab -e` (for system wide job)
Go to the end of the file and add the following line

`*/30 * * * * /PATH/TO/tvprogram.sh`

(This is set to 30 minutes interval). For other time intervals

*  *  *  * *  command to execute (like above)
┬ ┬ ┬ ┬ ┬
│ │ │ │ │
│ │ │ │ │
│ │ │ │ └───── day of week (0 - 7) (0 to 6 are Sunday to Saturday, or use names; 7 is Sunday, the same as 0)
│ │ │ └────────── month (1 - 12)
│ │ └─────────────── day of month (1 - 31)
│ └──────────────────── hour (0 - 23)
└───────────────────────── min (0 - 59)