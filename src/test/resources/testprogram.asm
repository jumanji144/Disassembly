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

    mov [data], ecx

    ; jumps and calls
    call func_1a    ; call to func_1a (1 byte) (0x00) (0x00) (0) (0 (in hex)) (0 (in octal)) (0 (in decimal))


func_1a:
  mov ebx, 0x1
  ; jump 10 ahead
  jmp func_1b

func_1b:
  mov ebx, 0x2

data: db 10
