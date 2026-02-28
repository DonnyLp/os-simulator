# Operating System Simulator

A Java-based OS simulator for learning core operating system concepts.

## Features

- **Process Scheduling**: Multi-level priority queues (Real-time, Interactive, Background)
- **Virtual Memory**: Paging with TLB simulation
- **Device I/O**: Virtual File System with file and random device support
- **IPC**: Kernel-mediated message passing between processes

## Build & Run

```bash
javac src/*.java
java -cp src Main
```

## Creating a Process

Extend `UserlandProcess` and implement `main()`:

```java
public class MyProcess extends UserlandProcess {
    @Override
    public void main() {
        System.out.println("Hello from MyProcess!");
        OS.exit();
    }
}
```

## Project Structure

```
src/
├── OS.java               # System call interface
├── Kernel.java           # Kernel and system call dispatch
├── Scheduler.java        # Multi-level process scheduler
├── Hardware.java         # Memory and TLB simulation
├── VFS.java              # Virtual File System
├── PCB.java              # Process Control Block
├── UserlandProcess.java  # User process base class
├── FakeFileSystem.java   # File system device
├── RandomDevice.java     # Random number device
└── [Test Programs...]    # Example and test processes
```
