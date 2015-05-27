# zlogger
Zlogger isa high-speed logger, allowing to trace a multithreaded application with a speed of not more than 85 ns 
per message of type long including several overhead fields. Typical structure of a single message consists of 
several fields: time, message type, thread name and message. Logger is based on Chronicle-Queue. All messages are 
stored in the binary format. A special terminal application has been developed to read the messages. Processing time
is approximately 3.5 sec / million records.

