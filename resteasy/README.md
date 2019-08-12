JAX-RS specification is a Java API for RESTful Web Services over the HTTP protocol. 
RESTEasy is a portable implementation of these specifications which can run in any 
Servlet container. 
RESTEasy also comes with additional features on top of plain JAX-RS functionalities. 

*A RESTFul application* follows the REST architectural style, which is used for designing networked applications. 
RESTful applications generate HTTP requests performing CRUD (Create/Read/Update/Delete) operations on resources. 
RESTFul applications typically return data in JSON or XML format. 

In our examples, we use Resteasy with Undertow server. We include Weld. 
