
 ident dw 0    ; error

data segment 
    id1 db 0ffffffh ;; error  
    id2 dw 0fffh
    id2 db 2 ;; error
    lblC dd 0 
    lblC db 125 ;;error
    data db 1   ;; error
    segC db 0
data ends     
    
    mov ident2,ecx ;; error

segC segment 

segC ends
   
code segment
   ; assume cs : code , ds : data
begin:
    
code:   ;;error
    
    test al,ds:var?2

    mov     ,al     ;error
    
    mov al, ;error
    
    test    ;error
    test ax ;error
    
    mul ax,bx   ;error
    
lblC:    ;;error
    
    
begin: ;;error
    
    sub lblC,x    ;; error
    
code ends 

code segment 

code ends 

    end  ;; error  