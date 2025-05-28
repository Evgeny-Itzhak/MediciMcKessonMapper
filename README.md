# mediciMcKessonMapper

## Overview

This is a Java console application designed to convert large `.xlsx` Excel files (up to 30,000 rows) with medical product data into properly structured `.csv` files with a fixed 29-column format.

It supports flexible configuration and reads from multiple input sources such as CLI arguments, environment variables, and a YAML configuration file. It also logs processing status both to console and to a file.

---

## Features

- ✅ Reads `.xlsx` Excel files with up to 30k rows
- ✅ Outputs `.csv` files with mapped and renamed 29 columns
- ✅ Supports virtual fields such as `Command = MERGE`
- ✅ Automatically creates missing output folders
- ✅ Logs to console and to `logs/converter.log` via Log4j2
- ✅ Uses Lombok for boilerplate-free model classes

---

## Usage

After building the project, run the application:

```bash
java -jar mediciMcKessonMapper.jar
```

Or with CLI parameters:

```bash
java -jar mediciMcKessonMapper.jar -inputFilePath=McKesson-eCommerceFormularyWeekly/input.xlsx -outputFilePath=csv-McKessonMappingResults/output.csv
```

---

## ⚙️ How Configuration Works

The application supports **multiple ways** to specify input/output file paths:

### ✅ 1. CLI Arguments (Highest Priority)

```bash
java -jar mediciMcKessonMapper.jar -inputFilePath=path/to/input.xlsx -outputFilePath=path/to/output.csv
```

### ✅ 2. Environment Variables

Set these variables:

- `INPUT_FILE_PATH`
- `OUTPUT_FILE_PATH`

**In IntelliJ IDEA:**
Go to `Run > Edit Configurations > Environment Variables` and add:

```
INPUT_FILE_PATH=McKesson-eCommerceFormularyWeekly/input.xlsx
OUTPUT_FILE_PATH=csv-McKessonMappingResults/output.csv
```

### ✅ 3. YAML Configuration (`src/main/resources/config.yaml`)

```yaml
inputFilePath: McKesson-eCommerceFormularyWeekly/input.xlsx
outputFilePath: csv-McKessonMappingResults/output.csv
```

> YAML will only be used if CLI and ENV are not provided.

### ✅ 4. Fallback Defaults

If none of the above sources are provided:
- Input: `McKesson-eCommerceFormularyWeekly/input.xlsx`
- Output: `csv-McKessonMappingResults/output.csv`

The output folder is created automatically if missing.

---

## 🔄 Configuration Priority

```
CLI args > Environment Variables > config.yaml > Default paths
```

---

## 📂 Working Directory

All relative paths are resolved from the **working directory**:
```java
System.getProperty("user.dir");
```

This is the directory from which you launch the `.jar` file.

---

## 🪵 Logging

Log4j2 is used to log both to console and to file:

- Console shows messages from `INFO` level and up
- File `logs/converter.log` logs everything including `DEBUG`

---

## Dependencies

- Apache POI
- Lombok
- Log4j2
- SnakeYAML

---

## Build Instructions

Build using Maven:

```bash
mvn clean package
```

Resulting `.jar` file will be:

```
target/mediciMcKessonMapper.jar
```

Run as shown above.