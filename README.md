### Disassembly
This project is a simple, portable, and extensible disassembler for x86 and x64.
it is written in pure java and thus allows for easy integration into any java based project

### Features
These features are only partially implemented and are stated in `goals` further down
* Supports x86 and x64
* Creates function references
* Creates labels for jumps
* Cross-references labels
* Data references with strings view

### Goals
- [ ] Finish the x86 instruction set
- [ ] Add support for 16-bit x86
- [ ] Create cross-references / Function references
- [ ] Create labels for jumps
- [ ] Create data references and string links
- [ ] Support for loading ELF / PE segments directly for better overview

### Graphical User Interface
The disassembler has graphical implementation in the [Recaf](https://github.com/Col-E/Recaf) project.
Using the [Recaf-Disassembler](https://github.com/Nowilltolife/Recaf-Disassembly) fork which implements the features declared.


### Contributing
To contribute feel free to fork the project and make a pull request.
If you have any questions or suggestions feel free to open an issue.
On bug reports please include the disassembly file and the disassembly output. (if possible)

### Resources
Having trouble understanding the architecure or want to develop your own decoder or implementation of the x86 set?
Head over to [Resources](RESOURCES.md) to find the resources i used to develop this program
