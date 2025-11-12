# Instructions

## Introcution

At the beginning, provide a detailed, in-depth description of the topic covered. 

## Code Descriptions

When asked to add a description:  

- Include a brief summary directly below the section title
- Follow with a detailed explanation beneath the code block, at least 2-3 sentences,
  more for more complex examples.   
- Limit text to **80 characters per line**, ending each line with **two spaces**  
- Maintain consistency with existing documents in the repository  

##  Example Structure

Examples should build progressively—from simple to advanced concepts.  
Avoid numbering section titles (e.g. *Example 1: Basic types*).  
Use plain titles like *Basic types*, following standard Java naming conventions.  

Use a **dark theme** for all code blocks.  

## Language Features

Utilize modern Java features up to **Java 25**, including:  

- **Compact source files**  
- **Instance main methods**  
- **Implicit imports** (e.g. `List`, `Map`, `IO`)  
- **Type inference with `var`**  
- **Pattern matching and enhanced switch (where relevant)**  

Here’s a Markdown table listing the **implicitly imported types** in Java 25 compact source files,  
based on [JEP 512](https://openjdk.org/jeps/512) and Oracle’s documentation:



---

### 📦 Implicit Imports in Java 25 Compact Source Files

| Package         | Class / Interface     | Description                                      |
|----------------|------------------------|--------------------------------------------------|
| `java.lang`     | `String`              | Textual data                                     |
| `java.lang`     | `System`              | Standard input/output streams                    |
| `java.lang`     | `Math`                | Mathematical functions                           |
| `java.lang`     | `Object`              | Root class of all Java objects                   |
| `java.lang`     | `Exception`           | Base class for exceptions                        |
| `java.lang`     | `IO`                  | Simplified console I/O (new in Java 25)          |
| `java.util`     | `List`                | Ordered collection interface                     |
| `java.util`     | `Map`                 | Key-value mapping interface                      |
| `java.util`     | `Set`                 | Unordered collection of unique elements          |
| `java.util`     | `ArrayList`           | Resizable list implementation                    |
| `java.util`     | `HashMap`             | Hash-based key-value store                       |

---

These types are available without needing explicit `import` statements in compact source files. 


## Code Format

Each example should follow this structure:

```java
void main() {

    // Using List (implicitly imported)
    var fruits = List.of("apple", "banana", "cherry");

    // Using Set (implicitly imported)
    var uniqueFruits = Set.of("apple", "banana", "apple");

    // Using Map (implicitly imported)
    var fruitColors = Map.of(
        "apple", "red",
        "banana", "yellow",
        "cherry", "dark red"
    );

    // Using ArrayList (implicitly imported)
    var mutableList = new ArrayList<String>();
    mutableList.add("grape");
    mutableList.addAll(fruits);

    // Using HashMap (implicitly imported)
    var mutableMap = new HashMap<String, String>();
    mutableMap.put("grape", "purple");
    mutableMap.putAll(fruitColors);

    // Using IO (new in Java 25)
    IO.println("Fruits:");
    for (var fruit : mutableList) {
        IO.println("- " + fruit + " (" + mutableMap.get(fruit) + ")");
    }

    // Using Math and System (from java.lang)
    IO.println("Total fruits: " + Math.max(mutableList.size(), 0));
    System.exit(0); // Clean exit
}
```

Use a simple `main` function as the entry point. Avoid classes and static methods  
unless demonstrating specific language features. Use spaces after Section headings,  
description text and the example, and the `void main() {` method.  

## Terminology

When referencing functions or methods:  

- Use names without parentheses (e.g. *the `main` function*, not *`main()`*)  
- Be precise and consistent in terminology across examples  

