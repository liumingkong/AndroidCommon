# AndroidCommon

buildscript {
  repositories {
    maven {
      url  "https://dl.bintray.com/liumingkong/maven/"
    }
    mavenCentral()
  }
  dependencies {
    classpath 'com.android.tools.build:gradle:1.0.0'
  }
}

repositories {
  maven {
    url  "https://dl.bintray.com/liumingkong/maven/"
  }
  mavenCentral()
}

dependencies {
  compile 'com.android.black:common:0.0.1'
}
