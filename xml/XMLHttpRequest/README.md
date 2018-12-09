     
In the nginx configuration file, we need to allow CORS

```
location / {
        # Simple requests
        if ($request_method ~* "(GET|POST)") {
           add_header "Access-Control-Allow-Origin"  *;
        }

        # First attempt to serve request as file, then
        # as directory, then fall back to displaying a 404.
        try_files $uri $uri/ =404;
}
```

XMLHttpRequest API provides client functionality for transferring data between 
a client and a server. It allows an easy way to retrieve data from a URL without 
having to do a full page refresh. As a consequence, a web page has to update 
just a part of the page without disrupting what the user is doing. 
XMLHttpRequest is used heavily in AJAX programming. 
