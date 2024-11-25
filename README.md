# Compiling and Running Java Files in the BankSystem Project

This guide explains how to compile and run the Java files in the **BankSystem** project using the terminal.

## Prerequisites
- Ensure you have the Java Development Kit (JDK) installed. You can check by running:
  ```bash
  java -version
  javac -version
  ```
- Confirm that your working directory contains the `.java` source files.

## Steps to Compile and Run

### 1. Compile the Java Files

To compile all `.java` files in the current directory and organize the `.class` files based on their package structure, run:
```bash
javac -d . *.java
```
- `-d .`: Specifies the output directory for compiled files. In this case, the current directory (`.`) is used.
- `*.java`: Compiles all `.java` files in the current directory.

### 2. Run the Program

Once compiled, use the `java` command to run the program. Ensure you specify the fully qualified class name (including the package if applicable).

For this project, to run the `Main` class located in the `BankSystem` package, use:
```bash
java BankSystem.Main
```

### Example Workflow
```bash
@Mynth16 ➜ /workspaces/BankSystem (main) $ javac -d . *.java
@Mynth16 ➜ /workspaces/BankSystem (main) $ java BankSystem.Main
```

### Notes
1. **Directory Structure After Compilation**:
   The compiled `.class` files will be organized based on the `package` declarations in your source files. For example:
   ```
   BankSystem/
   ├── BankAccount.class
   ├── Main.class
   ├── SavingsAccount.class
   ├── CurrentAccount.class
   ```

2. **Default Package**:
   If your classes are not in a package, compile with:
   ```bash
   javac *.java
   ```
   Then, run the program using:
   ```bash
   java Main
   ```

3. **Error Handling**:
   - If you encounter `ClassNotFoundException`, ensure you are in the correct directory and specify the fully qualified class name.
   - If there are package-related errors, confirm that your directory structure matches the `package` declarations in your `.java` files.

### Cleaning Up
To remove all compiled `.class` files and start fresh, run:
```bash
rm -r BankSystem/*.class
```

Now you are ready to compile and run your Java program! If you encounter issues, double-check your package declarations and file organization.

