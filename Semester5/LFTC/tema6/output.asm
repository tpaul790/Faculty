extern printf, scanf, exit

section .data
    format_in db "%d", 0
    format_out db "%d", 10, 0

section .text
global main
main:
section .data
    a dd 0
section .text
section .data
    b dd 0
section .text
section .data
    c dd 0
section .text
    ; cin >> a
    push a
    push format_in
    call scanf
    add esp, 8
    ; cin >> b
    push b
    push format_in
    call scanf
    add esp, 8
    push 1
    push 2
    push dword [a]
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    pop ebx
    pop eax
    add eax, ebx
    push eax
    push dword [b]
    push 3
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    pop ebx
    pop eax
    sub eax, ebx
    push eax
    ; c = ...
    pop eax
    mov [c], eax
    push dword [c]
    ; cout << expr
    pop eax
    push eax
    push format_out
    call printf
    add esp, 8

    ; return 0
    push 0
    call exit
