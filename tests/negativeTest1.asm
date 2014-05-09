
 ident dw 0    ; error

data segment 
    ident1 db 0ffffffh ;; error  
    ident2 dw 0fffh
    ident2 db 2 ;; error
    labelConcurent dd 0 
    LABELConCurent db 125 ;;error
    data db 1   ;; error
    segmentConcurent db 0
data ends     
    
    mov ident2,ecx ;; error

segmentConcurent segment 

segmentConcurent ends
   
code segment
    
begin:
    
code:   ;;error
    
    test edx,ds:var?2

    mov     ,al     ;error
    
    mov al, ;error
    
    test    ;error
    test ax ;error
    
    mul ax,bx   ;error
    
labelConcurent:    ;;error
    
    
begin: ;;error
    
    sub labelConcurent,x    ;; error
    
code ends 

code segment 

code ends 

    end  ;; error  