# Operating System Simulator

A Java-based operating system simulator that implements core OS concepts including process management, memory management, device I/O, and inter-process communication.

## Features

### Process Management
- **Multi-level Priority Scheduling**: Three priority levels (Real-time, Interactive, Background)
- **Process Creation and Lifecycle**: Support for creating, running, sleeping, and terminating processes
- **Context Switching**: Cooperative and preemptive process switching
- **Process States**: Running, waiting, sleeping states with proper transitions

### Memory Management
- **Virtual Memory**: Simulated virtual memory with paging support
- **Memory Allocation**: Dynamic memory allocation and deallocation for processes
- **Translation Lookaside Buffer (TLB)**: Hardware simulation with TLB for address translation
- **Page Management**: Virtual-to-physical address mapping

### Device I/O
- **Virtual File System (VFS)**: Abstracted file system interface
- **File Operations**: Create, read, write, seek, and close file operations
- **Random Device**: Pseudo-random number generator device
- **Device Abstraction**: Common interface for all device types

### Inter-Process Communication
- **Message Passing**: Kernel-mediated message passing between processes
- **Process Identification**: PID-based process lookup and communication
- **Synchronization**: Process synchronization through message waiting

## Architecture

### Core Components

- **`OS.java`**: Main operating system interface providing system calls
- **`Kernel.java`**: Core kernel functionality handling system call dispatch
- **`Scheduler.java`**: Multi-level feedback queue scheduler with priority-based process selection
- **`Hardware.java`**: Simulated hardware components including memory and TLB
- **`VFS.java`**: Virtual File System managing device abstraction
- **`Process.java`**: Base process class with thread management
- **`PCB.java`**: Process Control Block for storing process metadata

### Devices

- **`FakeFileSystem.java`**: Simulated file system with file operations
- **`RandomDevice.java`**: Random number generator device
- **`Device.java`**: Interface for all device implementations

### System Calls

The simulator provides the following system calls:

- `createProcess()` - Create new processes with priority levels
- `switchProcess()` - Request process context switch
- `sleep()` - Put process to sleep for specified duration
- `open()` - Open devices (files, random generator, etc.)
- `read()` - Read data from devices
- `write()` - Write data to devices
- `seek()` - Set position in device
- `close()` - Close device handles
- `allocateMemory()` - Allocate virtual memory pages
- `freeMemory()` - Free allocated memory
- `sendMessage()` - Send messages between processes
- `waitForMessage()` - Wait for incoming messages
- `getPID()` - Get current process ID
- `exit()` - Terminate current process

## Usage

### Running the Simulator

```bash
# Compile the Java source files
javac src/*.java

# Run the main simulation
java -cp src Main
```

### Example Programs

The repository includes several example programs demonstrating OS features:

#### Basic Processes
- **`HelloWorld.java`**: Simple process that prints "HelloWorld" repeatedly
- **`GoodbyeWorld.java`**: Companion process for basic multi-processing demo

#### Device I/O Testing
- **`DeviceTest.java`**: Demonstrates file and random device operations
- **`DeviceTest2.java`**: Advanced device testing with multiple file operations

#### Memory Management
- **`PagingTest.java`**: Basic memory allocation and hardware read/write operations
- **`PagingTest2.java`**: Memory allocation and deallocation testing
- **`PagingTest3.java`**: Advanced paging scenarios

#### Inter-Process Communication
- **`Ping.java`** and **`Pong.java`**: Message passing demonstration between processes

#### Process Lifecycle
- **`SleepTest.java`**: Process sleep functionality testing
- **`ExitTest.java`**: Process termination testing

### Creating Custom Processes

To create a new process, extend the `UserlandProcess` class:

```java
public class MyProcess extends UserlandProcess {
    @Override
    public void main() {
        try {
            // Your process logic here
            System.out.println("My custom process is running!");
            
            // Use OS system calls
            int pid = OS.getPID();
            OS.sleep(1000);  // Sleep for 1 second
            
            // Cooperate with scheduler
            cooperate();
            
            OS.exit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### Configuration

The simulator can be run in different modes:

- **Normal Mode**: Runs HelloWorld and GoodbyeWorld processes
- **Test Mode**: Runs memory paging tests (set in `Init.java`)

Modify the `Init.java` constructor parameter to switch between modes:
```java
OS.startup(new Init(false)); // Normal mode
OS.startup(new Init(true));  // Test mode
```

## Educational Value

This simulator is designed for educational purposes to help understand:

1. **Operating System Concepts**: Hands-on experience with OS internals
2. **Process Scheduling**: Multi-level feedback queue implementation
3. **Memory Management**: Virtual memory and paging concepts
4. **Device Drivers**: Abstracted device interface design
5. **System Calls**: OS/user space interaction patterns
6. **Concurrency**: Multi-threading and process synchronization

## Project Structure

```
src/
├── OS.java                 # Main OS interface and system calls
├── Kernel.java            # Core kernel functionality
├── Scheduler.java         # Process scheduler
├── Hardware.java          # Simulated hardware (memory, TLB)
├── VFS.java              # Virtual File System
├── Process.java          # Base process class
├── PCB.java              # Process Control Block
├── UserlandProcess.java  # User process base class
├── Device.java           # Device interface
├── FakeFileSystem.java   # File system implementation
├── RandomDevice.java     # Random number device
├── Main.java             # Entry point
├── Init.java             # Initial process
└── [Test Programs...]    # Various test and example programs
```

## Contributing

When adding new features:

1. Follow the existing code patterns
2. Implement proper error handling
3. Add corresponding test programs
4. Update documentation as needed

## License

This project is for educational purposes. Please check with the repository owner for specific licensing terms.