`Future` is a placeholder for a result of an operation that hasn't finished yet. 
Once the operation finishes, the `Future` will contain that result.

Limitations of futures:

* they cannot be manually completed
* no further action on a Future's result without blocking
* multiple Futures cannot be chained or combined
* there is no exception handling construct

`CompletableFuture` was introduced to fix the limitations of `Future`. 

`CompletableFuture` is a tool for asynchronous programming in Java. Asynchronous programming is writing 
non-blocking code by running a task on a separate thread than the main application thread
and notifying the main thread about its progress, completion, or failure.

Callbacks can be attached to the `CompletableFuture` using `thenApply()`, `thenAccept()` and `thenRun()` 
methods.

The `CompletableFuture.get()` method is blocking. It waits until the `Future` is completed and returns 
the result after its completion.

The `runAsync(Runnable runnable)` returns a new `CompletableFuture` that is asynchronously completed by 
a task running in the `ForkJoinPool.commonPool()` after it runs the given action.
(It execute tasks in a thread obtained from the global `ForkJoinPool.commonPool()`) 
