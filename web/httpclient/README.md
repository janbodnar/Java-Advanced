*Java HTTP Client* is used to send request HTTP resources over the network. 
It supports HTTP/1.1 and HTTP/2, both synchronous and asynchronous programming models, 
handles request and response bodies as reactive-streams, and follows the familiar builder pattern.
It was added in Java 11.


* HttpClient: It is the main entry point of the API. The client is used to send requests and receive responses. 
It supports sending requests both synchronously and asynchronously by calling its methods *send()* and *sendAsync()*, 
respectively. A *Builder* is provided to create an instance. Once the client is created, the instance is immutable.
*  HttpRequest: It encapsulates an HTTP request, including the target URI, the method (GET, POST, DELETE), headers and other information. 
A request is constructed using a builder, is immutable once created, and can be sent multiple times.
*  HttpRequest.BodyPublisher: If a request has a body (e.g. in POST requests), this is the entity responsible for publishing 
the body content from a given source, for instance, from a string or a file.
*  HttpResponse: It encapsulates an HTTP response, including headers and a message body, if any. This is what the client 
receives after sending an *HttpRequest*.
* HttpResponse.BodyHandler: It is a functional interface that accepts some information about the response (status code and headers), 
and returns a *BodySubscriber*, which itself handles consuming the response body.
* HttpResponse.BodySubscriber: subscribes for the response body and consumes its bytes into some other form (a string, a file, 
or some other storage type).

*BodyPublisher* is a subinterface of *Flow.Publisher*, introduced in Java 9. Similarly, *BodySubscriber* is a subinterface 
of *Flow.Subscriber*. This means that these interfaces are aligned with the reactive streams approach, which is suitable 
for asynchronously sending requests using HTTP/2.

Implementations for common types of body publishers, handlers, and subscribers are pre-defined 
in factory classes *BodyPublishers*, *BodyHandlers*, and *BodySubscribers*. For example, to create a *BodyHandler* 
that processes the response body bytes (via an underlying *BodySubscriber*) as a string, 
the method *BodyHandlers.ofString()* can be used to create such an implementation. 
If the response body needs to be saved in a file, the method *BodyHandlers.ofFile()* can be used.
