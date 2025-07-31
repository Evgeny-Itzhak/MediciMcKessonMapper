# MediciMcKessonMapper 🧬

## 📦 Overview

This Java console application converts large `.xlsx` Excel files with medical product data into properly structured `.csv` files that can be directly uploaded to Shopify.

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

### Basic Run

💡 First, open a terminal and navigate to the folder where `mediciMcKessonMapper-v1.0.jar` is located.
For instance:
```bash
  cd /Users/icherner/Work/MediciSupplyCo/McKesson/MediciMcKessonMapper/
```

To run the application, specific _Parameters_ are required: 
`-inputFilePath` is __MANDATORY__. The parameters `-outputFilePath`, `-maxRowsPerFile` are optional.



### 🔧 Parameters

- `-inputFilePath` – path to the input `.xlsx` file
- `-outputFilePath` – (optional) path for the resulting `.csv` file
- `-maxRowsPerFile` – (optional) max number of rows per file (e.g., 2000)


---

### ✅ Run with 📄 Input File Path parameter

```bash
  java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/path/to/input.xlsx"
```


---


### ✅ Run with 📐 Limiting Output Size parameter

Split into multiple files with max rows:

```bash
  java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/path/to/input.xlsx" -maxRowsPerFile=2000
```


- Output will be split into: `output_part1.csv`, `output_part2.csv`, `output_part3.csv` and so on, keeping each one under 2,000 rows.

If `maxRowsPerFile` is not specified, all rows will be written to a **single file**.



---


### ✅ Run with 📁 Output File Path parameter

```bash
  java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/path/to/input.xlsx" -outputFilePath="/path/to/output/output.csv"
```


---

### ✅ Run with 🧾 All Parameters

```bash
  java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/path/to/input.xlsx" -outputFilePath="/path/to/output/output.csv" -maxRowsPerFile=2000
```

---

## ⚙️ Configuration

The app supports input/output config via:

### 1. **CLI Arguments** (highest priority)

```bash
  -maxRowsPerFile
  -inputFilePath
  -outputFilePath
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
  output_part3.csv
  ...
  ```

If `-maxRowsPerFile=0` or is not specified, all rows will be written to a **single file**.

---



### ✅ Run with 💡 Most Useful Command

```bash
 java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/path/to/input.xlsx" -maxRowsPerFile=2000
```

---

### 🧪 Real-World Example

If your path is:

```bash
  /Users/icherner/Work/McKesson-eCommerceFormularyWeekly/McKesson eCommerce Formulary Weekly May 26, 2025.xlsx
```

Then run:

```bash
  java -jar mediciMcKessonMapper-v1.0.jar -inputFilePath="/Users/icherner/Work/McKesson-eCommerceFormularyWeekly/McKesson eCommerce Formulary Weekly May 26, 2025.xlsx" -maxRowsPerFile=2000
```

---




## 🗂 Working Directory

All relative paths are resolved from the **working directory**:

```text
System.getProperty("user.dir");
```

This is the directory from which you launch the `.jar` file.

---

## 🪵 Logging

Log4j2 is used for logging. The configuration is as follows:

- Console Output: Displays log messages from level `INFO` and above.
- File Output: All log messages including `DEBUG` are written to `converter__YYYY_MM_DD__hh_mm_ss.log`
- Log Directory Structure:

```text
logs/
└── YYYY_MM/
    └── DD/
```

Each day, logs are stored in a folder corresponding to the current date inside a monthly subdirectory.

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


---

## 📊 CSV Columns and Mapping

| CSV Column                | Mapped From XLSX Column                                                                           |
|---------------------------|---------------------------------------------------------------------------------------------------|
| Handle                    | Retail Description + "MSC-MCK-" + McK Item No + "-" + Manufacturer Number                         |
| Command                   | virtual (not from file) - MERGE                                                                   |
| Title                     | Retail Description                                                                                |
| Body HTML                 | Retail Features & Benefits                                                                        |
| Vendor                    | Brand or Series                                                                                   |
| Type                      | Application                                                                                       |
| Tags Command              | virtual (not from file) - REPLACE                                                                 |
| Tags                      | "__Mckesson-data,__Mckesson-upload-14092022,__Mckesson-August-Sheet-1," + Supply Manager Category |
| Status                    | Active                                                                                            |
| Image Src                 | Primary Image                                                                                     |
| Image Alt Text            | Retail Description                                                                                |
| Custom Collections        | Supply Manager Category                                                                           |
| Variant SKU               | E1 SKU                                                                                            |
| Variant Weight            | Shipping Weight                                                                                   |
| Variant Weight Unit       | Weight UOM                                                                                        |
| Variant Price             |                                                                                                   |
| Cost per item             |                                                                                                   |
| Shipping Width            | Shipping Width                                                                                    |
| Shipping Height           | Shipping Height                                                                                   |
| Shipping Depth            | Shipping Depth                                                                                    |
| Shipping Weight           | Shipping Weight                                                                                   |
| Dimension UOM             | Dimension UOM                                                                                     |
| Variant Barcode           | UPC                                                                                               |
| Variant Image             | Primary Image                                                                                     |
| Variant Inventory Tracker |                                                                                                   |
| Variant Cost              |                                                                                                   |
| Option1 Name              | Size                                                                                              |
| Option1 Value             | E1 SKU                                                                                            |
| Option2 Name              |                                                                                                   |
| Option2 Value             |                                                                                                   |
| Option3 Name              |                                                                                                   |
| Option3 Value             |                                                                                                   |
| Option4 Name              |                                                                                                   |
| Option4 Value             |                                                                                                   |


---

## 🛠 Support

If anything goes wrong, check the `logs/` directory and share the logs with the developer.
