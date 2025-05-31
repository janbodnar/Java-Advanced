# hashCode/equals contract

The **hashCode/equals contract** in Java is a set of rules that governs the  
behavior of the `hashCode` and `equals` methods, as defined in the `Object`  
class, to ensure consistent and correct behavior in hash-based collections like  
`HashSet` and `HashMap`. These methods must work together cohesively, as they  
are used to determine object equality and hash-based storage. The contract  
combines requirements for both methods, primarily from the `Object.hashCode` and  
`Object.equals` documentation.  
  
### HashCode/Equals Contract Rules  
  
1. **Consistency**:  
   - The `hashCode` method must consistently return the same integer for an  
     object during a single execution of a Java application, provided no fields  
     used in `hashCode` or `equals` are modified.  
   - The `equals` method must also be consistent, returning the same result for  
     the same pair of objects as long as their relevant fields remain unchanged.  
   - **Implication**: Both methods should produce predictable results to ensure  
     reliable behavior in collections. For example, an object’s hash code  
     shouldn’t change unless its state (relevant to `equals`) changes.  
  
2. **Equality Implies Same Hash Code**:  
   - If two objects are equal according to the `equals` method, they must return  
     the same hash code via `hashCode`.  
   - **Implication**: If `a.equals(b)` returns `true`, then `a.hashCode()` must  
     equal `b.hashCode()`. This ensures that equal objects are placed in the  
     same hash bucket in collections like `HashSet` or `HashMap`, preventing  
     issues like duplicates or lookup failures.  
  
3. **Non-Equal Objects May Have Same Hash Code**:  
   - If two objects are not equal according to `equals`, they are not required  
     to have different hash codes, but ideally, they should to minimize hash  
     collisions.  
   - **Implication**: While distinct objects can share the same hash code (a  
     collision), a well-designed `hashCode` method distributes hash codes evenly  
     to optimize performance in hash-based collections.  
  
4. **Reflexivity, Symmetry, and Transitivity of `equals`**:  
   - **Reflexivity**: For any non-null object `x`, `x.equals(x)` must return  
     `true`.  
   - **Symmetry**: For any non-null objects `x` and `y`, if `x.equals(y)`  
     returns `true`, then `y.equals(x)` must also return `true`.  
   - **Transitivity**: For any non-null objects `x`, `y`, and `z`, if  
     `x.equals(y)` returns `true` and `y.equals(z)` returns `true`, then  
     `x.equals(z)` must return `true`.  
   - **Implication**: These properties ensure `equals` behaves like a proper  
     equivalence relation, which is critical for consistent behavior in  
     collections.  
  
5. **Null Handling in `equals`**:  
   - For any non-null object `x`, `x.equals(null)` must return `false`.  
   - **Implication**: This prevents `NullPointerException`s and ensures `equals`  
     handles null comparisons correctly.  
  
### Additional Notes  
  
- **Consistency Between `hashCode` and `equals`**: The fields used in `equals`  
  to determine equality should be the same as those used in `hashCode`. If  
  `equals` compares certain fields, `hashCode` must compute its value based on  
  those same fields to maintain the contract.  
- **Immutability**: Fields used in `hashCode` and `equals` should ideally be  
  immutable to prevent changes after an object is added to a hash-based  
  collection. Modifying these fields (e.g., `name` in the provided `Thing`  
  class) can break the contract, causing issues like duplicates or lookup  
  failures.  
- **Default Implementations**:  
  - `Object.equals` checks for reference equality (`==`), which is insufficient  
    for content-based comparison.  
  - `Object.hashCode` returns a value based on the object’s memory address,  
    which is inconsistent with a custom `equals` based on fields.  
  - Custom implementations (like in the `Thing` class) override these to base  
    equality and hash codes on object content (e.g., `name`).  
  
## Example from Provided Code  
In the `Thing` class:  
  
```java  
@Override  
public int hashCode() {  
    return name.hashCode();  
}  
@Override  
public boolean equals(Object obj) {  
  
    if (this == obj) return true;  
    if (!(obj instanceof Thing)) return false;  
    Thing other = (Thing) obj;  
    return Objects.equals(this.name, other.name);  
}  
```  
- **Consistency**: `hashCode` returns `name.hashCode()`, and `equals` compares  
  `name` fields, both consistent as long as `name` is unchanged.  
- **Equality and Hash Code**: If two `Thing` objects have the same `name`,  
  `equals` returns `true`, and `hashCode` returns the same value, satisfying the  
  contract.  
- **Reflexivity, Symmetry, Transitivity**: The `equals` method satisfies these:  
  - Reflexive: `t1.equals(t1)` is `true`.  
  - Symmetric: If `t1.equals(t2)`, then `t2.equals(t1)`.  
  - Transitive: If `t1.equals(t2)` and `t2.equals(t3)`, then `t1.equals(t3)`.  
  - Null handling: `t1.equals(null)` returns `false` due to the `instanceof`  
    check.  
- **Issue**: The `name` field is mutable (not `final`). If changed after adding  
  a `Thing` to a `HashSet` (e.g., via commented-out `t1.name = "Thing 1";`), the  
  hash code changes, but the object remains in its original hash bucket. This  
  violates consistency, potentially causing duplicates or lookup failures.  
  
### Why the Contract Matters  
  
Violating the hashCode/equals contract can lead to serious issues in hash-based  
collections:  
  
- **Inconsistent Hash Codes**: If equal objects have different hash codes, they  
  may be placed in different buckets, allowing duplicates in a `HashSet` or  
  preventing `HashMap` lookups.  
- **Mutating Fields**: Changing fields used in `hashCode` or `equals` after  
  adding an object to a collection (as in the `Thing` example) breaks  
  consistency, potentially causing the object to be unreachable or added  
  multiple times.  
- **Incorrect Equality**: An `equals` method that doesn’t satisfy reflexivity,  
  symmetry, or transitivity can lead to unpredictable behavior in collections or  
  other logic relying on equality checks.  
  


```java
import java.util.HashSet;
import java.util.Set;

class Thing {

    public String name = "thing";

    public Thing(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (!(obj instanceof Thing))
            return false;
        Thing other = (Thing) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}

void main() {

    Set<Thing> vals = new HashSet<>();

    var t1 = new Thing("thing 1");
    var t2 = new Thing("thing 2");
    var t3 = new Thing("thing 3");
    var t4 = new Thing("thing 4");
    var t5 = new Thing("thing 5");

    vals.add(t1);
    vals.add(t2);
    vals.add(t3);
    vals.add(t4);
    vals.add(t5);

    // t1.name = "Thing 1";
    // t2.name = "Thing 2";
    // t3.name = "Thing 3";

    vals.add(t1);
    vals.add(t3);
    vals.add(t5);

    System.out.println(vals);
}
```


### Issues in the Provided Code

1. **Mutable Field Used in `hashCode` and `equals`**:  
   - The `name` field, used in `hashCode` and `equals`, is mutable (not  
     `final`). If modified after a `Thing`  object is added to the `HashSet` (as  
     in the commented-out lines `t1.name = "Thing 1";`, etc.), it changes the  
     object’s hash code and equality behavior.  
   - **Impact**: This breaks the `HashSet` contract, as the object remains in  
     its original hash bucket (based on the old `name`). Subsequent operations  
     like `vals.add(t1)` may add the same object again or fail to find it,  
     leading to duplicates or lookup issues.  
   - **Example**: If `t1.name` changes from `"thing 1"` to `"Thing 1"`, `t1`’s  
     new hash code won’t match its bucket, potentially allowing `t1` to be added  
     again as a "new" object.  
  
2. **Commented-Out Code Exposes Risk**:  
   - The commented-out lines (`t1.name = "Thing 1";`, etc.) highlight the risk  
     of mutating `name` after adding objects to the `HashSet`. While currently  
     inactive, they indicate a potential design flaw, as the code allows such  
     modifications.  
   - **Impact**: If uncommented, these changes would corrupt the `HashSet`’s  
     integrity, causing unpredictable behavior like duplicate entries or failure  
     to find existing objects.  
  
3. **Lack of Immutability Enforcement**:  
   - The `name` field is `public` and not `final`, making it easily modifiable  
     from outside the class. This violates best practices for objects used in  
     hash-based collections like `HashSet`, which require consistent `hashCode`  
     and `equals` results.  
   - **Impact**: External code can modify `name` at any time, risking the same  
     issues as above (e.g., duplicates or lookup failures).  
  
### Summary  
The primary issue is the mutability of the `name` field, which is used in  
`hashCode` and `equals`. Modifying it after adding a `Thing` to a `HashSet` (as  
suggested by the commented-out code) can break the set’s integrity, leading to  
duplicates or lookup failures.    
This is a design flaw, as objects in hash-based collections should be immutable  
with respect to fields affecting `hashCode` and `equals`.  
