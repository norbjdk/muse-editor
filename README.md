<p align="center"><h1 align="center">MUSE</h1></p>
<p align="center">
	<em><code>❯ MusicXML Utility for Score Editing </code></em>
</p>
<p align="center">
	<img src="https://img.shields.io/github/license/norbjdk/MUSE?style=default&logo=opensourceinitiative&logoColor=white&color=f53c36" alt="license">
	<img src="https://img.shields.io/github/last-commit/norbjdk/MUSE?style=default&logo=git&logoColor=white&color=f53c36" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/norbjdk/MUSE?style=default&color=f53c36" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/norbjdk/MUSE?style=default&color=f53c36" alt="repo-language-count">
</p>
<p align="center"><!-- default option, no dependency badges. -->
</p>
<p align="center">
	<!-- default option, no dependency badges. -->
</p>
<br>

#  Table of Contents

- [ Overview](#-overview)
- [ Project Structure](#-project-structure)
- [ Getting Started](#-getting-started)
    - [ Prerequisites](#-prerequisites)
    - [ Installation](#-installation)
    - [ Usage](#-usage)
    - [ Testing](#-testing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)

---

#  Overview

#### MUSE is an open source music notation software.
#### Project rely on .musicxml type
#### Technology used for desktop editor: JavaFX
#### Project under MIT License
#### Be aware that is diploma thesis project.

---

# Project Structure

```sh
└── muse-editor/
    ├── README.md
    ├── .gitignore
    ├── mvnw
    ├── pom.xml
    ├── src
    │   └── main
    │       └── java
    │           ├── module-info.java
    │           └── com.norbjdk.museeditor
    │               ├── app   
    │               │   └── MuseEditorApp.java (Application Entry)      
    │               ├── core
    │               ├── model
    │               ├── service
    │               └── ui
    └── README.md
   
```

---
#  Getting Started

##  Prerequisites

Before getting started with muse, ensure your runtime environment meets the following requirements:

- **Programming Language:** Java 25


##  Installation

Install <b>Muse Editor</b> using one of the following methods:

**Build from source:**

1. Clone the muse repository:
```sh
❯ git clone https://github.com/norbjdk/muse-editor/
```

2. Navigate to the project directory:
```sh
❯ cd muse-editor
```

##  Usage
Run muse using the following command:

```sh
❯ mvn javafx:run
```

##  Testing
Run the test suite using the following command:

---

#  License

This project is protected under the [MIT]() License.

---

#  Acknowledgments

- Thinking in Java
- <a href="https://openjfx.io/javadoc/25/">JavaFX Software Development Kit Version 25 API Specification</a>
- <a href="https://github.com/kordamp/ikonli">Ikonli Icon Pack</a>

---