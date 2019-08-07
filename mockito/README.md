**SUT** 

System under test (SUT) refers to a system that is being tested for correct operation. 


**Stub**

A stub method is a method that just returns a simple but valid 
(though not necessarily correct) result.

They are typically made when building the infrastructure and we don't 
want to spend time on every method needed right now. Instead we create stubs 
so everything compiles and our IDE's auto-complete knows about the methods 
we plan to use.

Stubs are using in mocking as well. We supply stub methods instead of the normal 
dependency through dependency injection which return fixed results and then ensure 
that the code does the right thing with them. 
This isolates testing to the code we are trying to test and means we do not need 
to spin up a database just to run those tests.
