### Processing instructions
Every x86 instruction is first decoded in a mnemonics list
Then with the same index a operation is found in the Operations list
#### Operations 
The operations dictate what bytes need to be decoded and how to interpret them
for example:
```java
"Ra" -> 'R' + 'a' = Read 1 register byte which follows the ModR/M bytes then concaterate
                    them together
```
Example for opcode 01 d8 (add eax, ebx):
```java
  "R":
  0xd8 -> 0b11011000
  0b11: ModR/M (Value is register and not memory reference)
  0b011: Register (ebx)
  0b000: Register (eax)
  "a":
  [eax, ebx] -> "eax, ebx" -> "add eax, ebx"
```
### Labels & Functions
Labels are used to make reading jumps easier.
Every time a jump instruction is found a label to the locations is created.
The same goes for call instructions.
For example:
```asm
  call 0x00401500
  jmp 0x00401000
```
The naming rules can be changed.
For this example I used friendly names.   
Which results in:
```asm
  call function_blackberryjuice
  jmp label_applepie
```
But you can also use the hexadecimal address.
```asm
  call function_00401500
  jmp label_00401000
```

### Data
Data references work in the same way as labels.
But only when address is explicitly given and exists in the program space.
For example:
```asm
  mov eax, [0x00401500]
  mov eax, [0x00401000]
```
Again the naming rules apply here.
```asm
  mov eax, data_blackberryjuice
  mov eax, data_applepie
```
And hex again.
```asm
  mov eax, data_00401500
  mov eax, data_00401000
```


