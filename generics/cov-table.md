# Covariance & Contravariance table

| Feature | Covariance (Upper Bound) | Contravariance (Lower Bound) |
| :--- | :--- | :--- |
| **Common Name** | "Extends Wildcard" | "Super Wildcard" |
| **Syntax** | `? extends T` | `? super T` |
| **Example** | `List<? extends Number>` | `List<? super Integer>` |
| **What It Means** | A list of *some unknown type* that is `T` or a **subtype** of `T`. | A list of *some unknown type* that is `T` or a **supertype** of `T`. |
| **Type of Bound** | **Upper Bound** (`T` is the *highest* class in the hierarchy you can guarantee). | **Lower Bound** (`T` is the *lowest* class in the hierarchy you can guarantee). |
| **Valid List Types** | `List<Number>`, `List<Integer>`, `List<Double>` | `List<Integer>`, `List<Number>`, `List<Object>` |
| **Can you READ? (Get)** | **Yes.** You are guaranteed to get an object that is at least of type `T`. | **No** (safely). You can only be sure you're getting an `Object`, as you don't know how high up the superclass chain the list's type goes. |
| **Can you WRITE? (Add)** | **No** (except `null`). You don't know the *specific* subtype. A `List<? extends Number>` could be a `List<Integer>`, so you can't add a `Double`. | **Yes.** You can safely add any object of type `T` (or its subtypes). A `List<Integer>`, `List<Number>`, or `List<Object>` can all safely accept an `Integer`. |
| **PECS Mnemonic** | **P**roducer **E**xtends | **C**onsumer **S**uper |
| **Primary Use Case** | When you are **reading** items *from* a generic structure (it *produces* items for you). | When you are **writing** items *to* a generic structure (it *consumes* items from you). |

