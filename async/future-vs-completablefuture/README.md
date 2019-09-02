`CompletableFuture` was introduced to fix the limitations of `Future`. 

`Future` is a placeholder for a result of an operation that hasn't finished yet. 
Once the operation finishes, the `Future` will contain that result.

Limitations of futures:

* they cannot be manually completed
* no further action on a Future's result without blocking
* multiple Futures cannot be chained or combined
* there is no exception handling construct


