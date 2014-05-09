
;.386

data segment ; use16
    var$1 db  - 11000000b + 10000000b
    varDiv dw 0f3h + 011b * 10 ; bla bla bla 
    var?2 dd 12551 + 0 + 2*1
    ident dd 125*2 - 10 + 25/5 + 89
    var@3 dw 765o
    var_4 db -15
    someVar1 db 5 +++ -6
    someVar2 db 5 * 6 - 2
    someVar3 dw ((4 + 2)*5 - 101b + 064h)/31q
data ends 

code segment 
  ;  assume cs : code , ds : data
    
begin:
    ; mov ax,data
    ; mov ds,ax
            
    sti 
    
    and al,es:[bx + si]
    or al,01101110b
    
    div ds:varDiv
    mul dx
    
    
    test edx,fs:var?2
    jae @testLable
    
    mov ds:VAR@3,bx
    or cx,0AB91h
    mov ds:var?2,ecx
    and eax,dword ptr ds:[bx + di]
    
@testLable:    test ds:var_4,al
    jae @endLbl
   
    jmp @testLable
    
    adc al,gs:var_4
    or ecx,086af10c6h
    jmp @endLbl
    ;; 
         
     
@endLbl:    
    
    ; mov ax,4C00h
    ; int 21h
    
code ends 
     end begin