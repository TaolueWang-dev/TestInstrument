# README

## Project Overview

This project uses the **Soot** framework to analyze and instrument Java bytecode. The `MainDriver` class uses Soot to load Java class files, inserts a `GotoCounterTransformer` that tracks and records the number of times `goto` statements are executed during runtime.

## Features

- **Class Path Setup**: Specifies the path to the Java class files to be analyzed.
- **Soot Class Path Setup**: Adds the standard Java library (`rt.jar`) and the specified class path to Soot's classpath.
- **Instrumentation**: The `GotoCounterTransformer` class inserts code before every `goto` statement to increment a counter, and the count is printed when the program exits.

## Usage

### Prerequisites

1. **JDK Installation**: Ensure that you have JDK installed and the `JAVA_HOME` environment variable is set correctly.
2. **Soot Framework**: You need to include the Soot framework in your project. You can do this by:
    - It has already be saved in input/
    - Including "sootclasses-trunk-jar-with-dependencies.jar" to the project structure and library.

3. **Project Structure**:
   ```
   ├── input/
   │   └── MyCounter.class
   │   └── <Java file that you want to instrument>
   ├── lib/
   │   └── soot.jar
   ├── src/
   │   └── MainDriver.java
   │   └── GotoCounterTransformer.java 
   │   └── InvokeStaticInstrumenter.java
   └── sootOutput
       └── MainDriver.java
       └── MyCounter.class
       └── <after instrument classes>
   
   ```

### Environment Variable Configuration

Ensure that the `JAVA_HOME` environment variable is set to your JDK installation path, for example:

```bash
export JAVA_HOME=/path/to/your/jdk
```

### Running the Program
1. **Prepare the file**:
   First complile the Java file  that you want to calculate the number of goto statements and put to the "input" directory.

2. **Run the Main Program**:

   Just run the `MainDriver` class and put the name of class you want to count after it:

   ```bash
   java MainDriver testGoto
   ```

   The instrumented file would be saved in sootOutput.

   Example Output
   ```bash
   Soot started on Sun Feb 16 15:07:09 CST 2025
   Found Goto statement: goto [?= return]
   Found Goto statement: goto [?= (branch)]
   Soot finished on Sun Feb 16 15:07:09 CST 2025
   Soot has run for 0 min. 0 sec.
   ``` 

3. **View the Output**:

   Use command line to run the instrumented bytecode file:

   ```bash
   java 'youClassName' -cp 'youClassNameDirectory'
   ```

   Example output
   ```bash
   counter : 4
   ```

## Code Explanation

1. **Class Path Setup**:
   In `MainDriver`, the `classPath` variable specifies the directory where the Java classes are located. `rtJar` points to the Java standard library, which is retrieved via `System.getenv("JAVA_HOME")`.

   ```java
   String classPath = "input";
   String rtJar = System.getenv("JAVA_HOME") + "/jre/lib/rt.jar";
   Options.v().set_soot_classpath(classPath + ":" + rtJar);
   ```

2. **Soot Instrumentation**:
   The code retrieves the `jtp` pack from `PackManager.v().getPack("jtp")`, then adds the `GotoCounterTransformer` for instrumentation during the `jtp` phase.

   ```java
   Pack jtp = PackManager.v().getPack("jtp");
   jtp.add(new Transform("jtp.instrumenter", new GotoCounterTransformer()));
   ```



3. **Running Soot**:
   Finally, the `soot.Main.main(args)` method is invoked to initiate Soot’s analysis and instrumentation process.

   ```java
   soot.Main.main(args);
   ```

### `GotoCounterTransformer` Class

The `GotoCounterTransformer` class traverses the method body, checks each statement, and if it’s a `goto` statement, it inserts a line of code to increment a static counter. The counter’s value is printed when the program exits.

Here has two instumrnt, the other also can use 'InvokeStaticInstrumenter' to count the number of 'InvokeStatic' statements. The usage the same as the GotoCounterTransformer.

