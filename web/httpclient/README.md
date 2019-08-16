*Java HTTP Client* is used to send request HTTP resources over the network. 
It supports HTTP/1.1 and HTTP/2, both synchronous and asynchronous programming models, 
handles request and response bodies as reactive-streams, and follows the familiar builder pattern.
An HttpClient provides configuration information, and resource sharing, for all requests sent through it. 
It was added in Java 11.

Requests can be sent either synchronously or asynchronously:

* send(HttpRequest, BodyHandler) blocks until the request has been sent and the response has been received.
* sendAsync(HttpRequest, BodyHandler) sends the request and receives the response asynchronously. 

The *sendAsync()* method returns immediately with a *CompletableFuture<HttpResponse>*. The *CompletableFuture* 
completes when the response becomes available. The returned *CompletableFuture* can be combined in different ways 
to declare dependencies among several asynchronous tasks.

* **HttpClient**: It is the main entry point of the API. The client is used to send requests and receive responses. 
It supports sending requests both synchronously and asynchronously by calling its methods *send()* and *sendAsync()*, 
respectively. A *Builder* is provided to create an instance. Once the client is created, the instance is immutable.
*  **HttpRequest**: It encapsulates an HTTP request, including the target URI, the method (GET, POST, DELETE), headers and other information. A request is constructed using a HttpRequest builder. It is immutable once created and can be sent multiple times.
*  **HttpRequest.BodyPublisher**: It converts high-level Java objects into a flow of byte buffers suitable for sending 
as a request body froom a given source, such as a string or a file.
*  **HttpResponse**: It encapsulates an HTTP response, including headers and a message body, if any. The client it receives after sending an *HttpRequest*.
* **HttpResponse.BodyHandler**: It handles response bodies. It is a functional interface that allows inspection of the response 
code and headers, before the actual response body is received, and returns a *BodySubscriber*, which itself handles consuming the response body.
* **HttpResponse.BodySubscriber**: It consumes response body bytes and converts them into a higher-level Java types.


*BodyPublisher* is a subinterface of *Flow.Publisher* and *BodySubscriber* is a subinterface of *Flow.Subscriber*. 
These interfaces are adjusted with the reactive streams approach, which is suitable for sending asynchronous 
requests using HTTP/2.

*BodyPublishers*, *BodyHandlers*, and *BodySubscribers* are factory classes which provide implementations for 
common types of body publishers, handlers, and subscribers. For instance, the *BodyHandlers.ofString()* can be 
used to create a *BodyHandler* that processes the response body bytes (via an underlying *BodySubscriber*) as a string.
The *BodyHandlers.ofFile()* can be used to save a response body into a file.


https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
