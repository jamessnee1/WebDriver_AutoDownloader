# WebDriver Auto Downloader
### By James Snee

## Overview

**WebDriver Auto Downloader** is a utility designed to simplify the process of downloading WebDriver binaries for your automated testing needs. Currently, this tool supports downloading **ChromeDriver** for Google Chrome.

## Features

- Automatically downloads the appropriate version of ChromeDriver based on the installed version of Google Chrome.
- Easy-to-use test method for checking the download worked correctly
- Config properties file added for easy storage of parameters
- Extent Reports added to suite, including Test Listener

## Installation

To use the WebDriver Auto Downloader, you need to have Java and Intellij installed on your system.

1. **Clone the Repository:**

   ```sh
   git clone https://github.com/jamessnee1/WebDriver_AutoDownloader.git

2. **Usage:**
To test the autodownloader, run the main method inside the `WebDriverHelper` class. The chromedriver will be downloaded to the `src/main/resources/WebDriver` folder.
Alternatively, run the `firstTest` inside the tests.UITest class.