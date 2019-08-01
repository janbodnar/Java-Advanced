*Concurrency*  - tasks run in overlapping time periods. Not necessarily at the same instant. For example, 
multitasking (time-slicing) on a single-core machine.

*Parallelism*  - tasks literally run at the same time, e.g., on a multicore processor.

Process vs Thread

A process is an instance of a program running on a computer. A thread is a dispatchable unit of work
within a process.

Threads (of the same process) run in a shared memory space, while processes run in separate memory spaces.
A thread is a light-weight process, but a process is a heavy-weight process. A process is maintained by operating 
system whereas a thread is maintained by a programmer. On a multiprocessor system, multiple processes can be executed in parallel.
On a uni-processor system, though true parallelism is not achieved, a process scheduling algorithm is applied and the processor is scheduled to execute each process one at a time yielding an illusion of concurrency.

https://i.stack.imgur.com/NVNge.jpg
