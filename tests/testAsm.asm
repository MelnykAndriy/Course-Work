
; .386

data segment ;; use16
    var$1 db  - 11000000b + 10000000b
    varDiv dw 0f3h + 011b * 10  
    var?2 dd 12551 + 0 + 2*1    
    ident dd 125*2 - 10 + 25/5 + 89 
    var@3 dw 765o   
    var_4 db -15    
    someVar1 db 5 +++ -6    
    someVar2 db 5 * 6 - 2   ;; 0Ah 
    someVar3 dw ((4 + 2)*5 - 101b + 064h)/31q  ;; should be 05h
    ident6 db 0 + (-1)*14*(-1) + 0  ;; should be 0Eh
    ident7 db -1 * 64 * -1  ;; should be 40h
    ident8 dw 8 mod 2   ;; should be 0000h
    ident9 dd ((9 mod 2 + 0fh) mod 17 ) mod ( -1 + 4 * 3 - ( 4 / 2) ) ;; should be 0007h 
data ends 

code segment ;; use16
   ; assume cs : code , ds : data
    
begin:
    ; mov ax,data
    ; mov ds,ax
                
    sti 
; @someLbl:
    ; and al,es:[bx + si]   ;; additional operand size detection
    or al,01101110b
    
    div ds:varDiv
    div word ptr ds:ident9 
    div byte ptr gs:ident8
    div dword ptr es:ident6
    div byte ptr gs:[edx + esi]
    div word ptr es:[ebx + ecx]
    div dword ptr es:[ebx + ecx]
    mul dx
    
    
    test fs:var?2 ,edx   
    ; jae @someLbl    
    
    mov ds:VAR@3,bx 
    or cx,0AB91h
    mov ds:var?2,ecx
    and eax,dword ptr ds:[bx + di]
    
; @testLable:  
    test ds:var_4,al
    ; jae @endLbl     
   
    ; jmp @testLable  
    
    adc al,gs:var_4 
   
    
    
    or al,1
    or ax,256
    or eax,0fffffh   
    or ecx,086af10c6h  
    ; jmp @endLbl 
    
         
    or cl,-20
    or cx,0fffh
    or ecx,0ac000h  
     
; @endLbl:    
   
    or bx,22
    or ebx,-3

    ; mov ax,4C00h
    ; int 21h   
    
code ends 
    end begin
          
     
