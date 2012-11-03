# Taimos daemon-framework

Litte framework to create Java programs that behave like Linux daemons.

# Getting started

There are some small steps to follow to create a Linux daemon with Java:

1. Implement your subclass of de.taimos.daemon.DaemonLifecycleAdapter
2. Call the DaemonStarter from your main method
3. Customize your init-script

If you want to run the program in _development mode_ make sure you start it with ``-DdevelopmentMode=true``.

In development mode the program's behavior will differ in some small points:

- Logging uses console instead of syslog and logfile
- No signal handling is used


## DaemonLifecycleAdapter

### boolean doStart()

This method is called to start your program. It must be non blocking and must return true on success.
If this method returns false the DaemonFramework stops execution of the daemon.

### boolean doStop()

This method is called on OS signal to stop your program. It must be non blocking and must return true on success.
If this method returns false the DaemonFramework stops execution of the daemon.

### started()

This method is called when the daemon completed startup successfully.

### stopped()

This method is called when the daemon completed shutdown successfully. Immediatly after this method _System.exit(0)_ is called.

### stopping()

This method is called when the daemon received shutdown signal.

### aborting()

This method is called when the daemon aborts execution. Immediatly after this method _System.exit(1)_ is called.

### signalUSR2()

This method is called when the daemon received the OS signal __USR2__. You can do what you want within this method.

### exception(LifecyclePhase phase, Throwable exception)

This method is called when an exception occurs in DaemonStarter. It provides the current phase and the thrown exception.

### Map<String, String> loadProperties()

This method is called to obtain the daemon properties. All properties provided in thsi map will be available via _System.getProperty_ later on.
The properties found in _de.taimos.daemon.DaemonProperties_ are used by the daemon framework itself and should be filled.

## Call DaemonStarter

In your _main_ method just call startDaemon to run the daemon framework. You have to provide a service name and an instance of your subclass of DaemonLifecycleAdapter;

```
DaemonStarter.startDaemon("my-service-name", new MyLifecycleAdapter());
```

## Customize init-script

You just have to create a copy of _initscript_ from the projects root folder and customize it. For basic usage it is enough to change the variables on top of the script

### progname

The name of the daemon

### pidfile

The name and location of the PID file. It defaults to /var/run/<daemonname>

### RUNDIR

The location of the daemon program. It defaults to /opt/<daemonname>

### RUNUSER

The user under which the daemon is run. The init script then calls java with ``su -c <command> RUNUSER``

### JAR_FILE

The name of the jarfile as it is found in $RUNDIR. The script calls java -jar <jarname>.

### JAVA_OPTS

Some Java options like memory size etc.











