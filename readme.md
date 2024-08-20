# Selenium Automation Test Framework - WebDriver Auto Downloader
### By James Snee

## Overview

This framework is a utility designed to simplify the process of downloading WebDriver binaries for your automated testing needs. Currently, this tool supports downloading **ChromeDriver** for Google Chrome and **GeckoDriver** for Mozilla Firefox.

## Features

- Automatically downloads and extracts the appropriate version of ChromeDriver or GeckoDriver based on the installed version of Google Chrome, or the version specified in the config for Mozilla Firefox.
- Easy-to-use test method for checking the download worked correctly
- Can use the downloaded WebDriver in tests immediately
- Config properties file added for easy storage of parameters
- Extent Reports added to suite, including Test Listener
- Screenshots on failure added

## Installation

To use this framework, you need to have Java (Version 16 minimum) and Intellij installed on your system.

1. **Clone the Repository:**

   ```sh
   git clone https://github.com/jamessnee1/WebDriver_AutoDownloader.git
2. **Setup**
Please ensure that the parameter `firefoxVersion` is setup in the `config.properties` file:
   ```sh
   # Mozilla Firefox Version
   firefoxVersion=v0.35.0
   ```
3. **Usage:**
To test the autodownloader, run the main method inside the `WebDriverHelper` class. The webdriver will be downloaded to the `src/main/resources/WebDriver` folder.
Alternatively, run the `exampleFirstUITest` inside the tests.UITest class.