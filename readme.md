![tvprogram](tvprogram.png)

# TVProgram

## Introduction
This program is able to get the french TV program and read by a Text to Speech (TTS) engine when pressing on a button. 
This is especially useful for blind or disable people.

It use MaryTTS (https://github.com/marytts/marytts), an open-source, multilingual Text-to-Speech Synthesis platform written in Java. It was originally developed as a collaborative project of DFKI’s Language Technology Lab and the Institute of Phonetics at Saarland University. It is now maintained by the Multimodal Speech Processing Group in the Cluster of Excellence MMCI and DFKI.
As of version 5.2, MaryTTS supports German, British and American English, French, Italian, Luxembourgish, Russian, Swedish, Telugu, and Turkish; more languages are in preparation. MaryTTS comes with toolkits for quickly adding support for new languages and for building unit selection and HMM-based synthesis voices.

This java program can run on a raspberry pi 2/3. 

## Currently
* TVProgram support fetching any RSS feed, a plugin for the french TV program https://webnext.fr/programme-tv-rss is provided
* RSS is cached for more efficiency
* A TVGuide is build and then read by MaryTTS http://mary.dfki.de/

## Configuration
* Free and Premium channel can be filtered out
* Sentences are templates driven (http://freemarker.org/) for easy customization and translation to something else than french
* Voices can be configured, for french "upmc-pierre-hsmm" or "enst-camille-hsmm" is recommended