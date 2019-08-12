*A Uniform Resource Locator (URL)*, is a reference to a web resource that specifies 
its location on a computer network and a mechanism for retrieving it. A web resource 
is any data that can be obtained via web, such as HTML documents, PDF files, PNG images, 
JSON data, or plain text.

A generic URL has the following form:

`scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]`

The square brackets indicate that the part is optional. A scheme is a way of addressing resources, 
such as http, ftp, mailto, or file.

The part following two slashes is called the authority part. The authority part contains:

* an optional authentication section of a user name and password, separated by a colon, followed by an 
at symbol (@) 
* a host, which is either a host name of or an IP address
* an optional port number, separated from the host by a colon.

A path is a road to the resource on the host. It may or may not resemble or map exactly to a file 
system path. Query string is used to add some criteria to the request for the resource. It is often 
a sequence of key/value pairs. The final part is an optional fragment, which points to a secondary 
resource, such as a heading. It is separated from the query string by a hash (#). 
