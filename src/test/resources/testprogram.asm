[bits 64]
_main:

    mov eax, 0x12345678
    mov ebx, 0x9abcdef0
    rol eax, 1
    add eax, ebx
    mov ecx, 0x9abcdef0
    add eax, [ecx]

    mov rax, 0x123456789abcdef0
    mov ebx, 0x9abcdef0
    rol rax, 1
    add rax, rbx
    mov ecx, 0x9abcdef0

    ; jumps and calls
    call func_1a


func_1a:
  jmp +0x1
