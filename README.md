# MediciMcKessonMapper

## 📦 Overview

This Java console application converts large `.xlsx` Excel files with medical product data into properly structured `.csv` files with a fixed 29-column format.

---

## ✅ Features

- Reads `.xlsx` files up to 30,000+ rows
- Uses field mapping from Excel to target CSV structure
- Supports virtual/default fields like `Command = MERGE`
- Optional splitting into multiple CSV files
- Configurable via CLI, environment, and YAML
- Logging via Log4j2 to both console and file

---

## 🚀 Usage

### Run from CLI:

```bash
java -jar mediciMcKessonMapper.jar
```

### With parameters:

```bash
java -jar mediciMcKessonMapper.jar -inputFilePath=McKesson-eCommerceFormularyWeekly/input.xlsx -outputFilePath=csv-McKessonMappingResults/output.csv -maxRowsPerFile=2000
```

---

## ⚙️ Configuration

The app supports input/output config via:

### 1. **CLI Arguments** (highest priority)

```bash
-maxRowsPerFile=2000
-inputFilePath=...
-outputFilePath=...
```

### 2. **Environment Variables**

```bash
export INPUT_FILE_PATH=McKesson-eCommerceFormularyWeekly/input.xlsx
export OUTPUT_FILE_PATH=csv-McKessonMappingResults/output.csv
export MAX_ROWS_PER_FILE=2000
```

### 3. **YAML File (`config.yaml`)**

```yaml
inputFilePath: McKesson-eCommerceFormularyWeekly/input.xlsx
outputFilePath: csv-McKessonMappingResults/output.csv
maxRowsPerFile: 2000
```

---

## ✂️ Output File Splitting

By default: **splitting is disabled**

To enable splitting:
- Set `maxRowsPerFile > 0` via CLI, ENV or YAML
- Output will be split into:
  ```
  output_part1.csv
  output_part2.csv
  ...
  ```

If `maxRowsPerFile <= 0`, all rows will be written to a **single file**.

---

## 🗂 Working Directory

All relative paths are resolved from the **working directory**:

```text
System.getProperty("user.dir");
```

This is the directory from which you launch the `.jar` file.

---

## 🪵 Logging

Log4j2 is used for logging:

- Console: shows messages from `INFO` and up
- File: `logs/converter.log` logs everything including `DEBUG`

---

## 🧪 Running Tests

Tests are written with JUnit 5. To run:

```bash
mvn test
```

Covers:
- Field mapping logic
- CSV content generation
- File splitting behavior

---

## 🧱 Build Instructions

```bash
mvn clean package
```

The jar will be created in `target/mediciMcKessonMapper.jar`
