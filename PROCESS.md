### Processing instructions
Every x86 instruction consists of a mnemonic and a set of operands.
The mnemonic is a single word, and the operands are a list of values.
The mnemonic originates from a list which is indexed via the opcode.
#### Operands
Operands are data classes that contain:
- the type of the operand
- a list of OperandObjects that contain the value of each part of the operand for example
the value of the register or the value of the memory address


As an example structure here is the operand of the `add eax, [eax + 0x10]` instruction:
```javascript
Operands: [
        
       Operand1: {
            types: [
                Operand.REGISTER,
            ],
            values: [
                OperandObject: {
                    value: 'eax',
                    type: Operand.REGISTER,
                },
            ]
       },
       Operand2: {
            types: [
                Operand.MEMORY,
            ],
            values: [
                OperandObject: {
                    value: 'eax',
                    type: Operand.REGISTER,
                },
                OperandObject: {
                    value: 0x10,
                    type: Operand.IMMEDIATE,
                },
            ],
       }
        
]
```
#### Operations
The operations are a sting that contains the instructions on how to decode the operands.
each character in the string is a single operation.
for example:
```java
"R" -> 'R' = Read 1 register byte which follows the ModR/M bytes and create 2 operands
```
Example for opcode 01 d8 (add eax, ebx):
```java
  "R":
  0xd8 -> 0b11011000
  0b11: ModR/M (Value is register and not memory reference)
  0b011: Register (ebx)
  0b000: Register (eax)
  Result:
  [Operand1('eax', Operand.REGISTER), Operand2('ebx', Operand.REGISTER)]
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


