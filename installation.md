---
layout: default
---

# Installation

## Installation
To install Lineation, download the latest release and place the JAR file in your server plugins folder and restart your server. This will create the necessary files with the default options in `plugins/Lineation`.  

This plugin also has WorldEdit as a dependency, so please install that too. WorldEdit is used to make selections and set the blocks of the finish line.  

The plugin has been tested in Minecraft 1.16.5 using Java 16 with WorldEdit 7.2.0. It might work on older versions, but I will not be guaranteeing compatability with older versions.  

## Configuration
Most of the configuration is done ingame by using the `/lineation command`. You can however edit `config.yml` to set lines default options, for if you find the provided defaults are not fit.  
Please do not edit `lines.yml` by hand, this will likely break and I will offer no support for this. This plugin overwrites config files when it reloads/stops, so do not edit files with the server running.
