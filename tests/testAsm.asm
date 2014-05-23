
; .386

code segment ;use16
   ; assume cs : code , ds : data
    
begin:

    jmp @end           
    sti 
@sLbl:

    mov ds : var$1, cl
    and edx, gs : ident
    adc dl, fs : id7
    and al,es:[bx + si]
    or al,01101110b
    
    div ds:varD
    div word ptr ds:id9 
    div byte ptr gs:id8
    div dword ptr es:id6
    
    jmp @def
    lol1 db 12
    lol2 dw 1225
    lol3 dd 1225112
@def:
       
    mov cs:lol1, dl
    mov cs:lol2, cx
    mov cs:lol3, ecx
    
    div byte ptr gs:[edx + esi]
    div word ptr es:[ebx + ecx]
    
@loop: jmp @loop

    div dword ptr es:[ebx + esi]
    div word ptr fs:[bx + di]

    div dword ptr es:[ebx][ecx]
    
    and ecx,dword ptr es:[esp][ecx]
    adc bx,word ptr gs:[esi + edx]
    
    mov byte ptr fs:[ecx + edi],al

    mul dx
    mul cx 
    mul ax
    mul al
    mul di
    mul ecx
    mul eax
    mul ebx
    
    test fs:var?2 ,edx   
    jae @sLbl    
    
    mov ds:VAR@3,bx 
    or cx,0AB91h
    mov ds:var?2,ecx
    and eax,dword ptr ds:[bx + di]

@tLbl:  
    test ds:var_4,al
    jae @eLbl     
   
    jmp @tLbl  
    
    adc al,gs:var_4 
   
    adc eax, fs:[eax + edi]
    adc al, ds : [edi + esi]
    
    and bx , ds : [bx + di]
    and eax , gs : [bx + si]
    
    mov  ss : [bx + si] , dl 
    mov  cs : [edi + esi], bx
    
    test gs : [ecx + eax ] , al 
    test es : [ bp + di ] , eax
    
    or al,1
    or ax,256
    or eax,0fffffh   
    or ecx,086af10c6h  
    jmp @eLbl 
    
         
    or cl,-20
    or cx,0fffh
    or ecx,0ac000h  
     
@eLbl:    
   
    or bx,22
    or ebx,-3


    
    ;Or reg,imm
    or dh , 12 * 01101b 
    or cx , (12 - 0A7h + (4 - 6/3)*11)*20
    or di , -(12 + 5)
    or ebx , (20 mod 0111b mod 5) - 13
    
    sti 
   
    ;Mov mem, reg
    mov es:sVar1 , ah
    mov byte ptr gs:[edx + esi],cl
    mov gs:sVar2 , dl
    mov dword ptr ds:sVar3,esp

@end:
    
code ends 

data segment ; use16
    var$1 db  - 11000000b + 10000000b
    varD dw 0f3h + 011b * 10  
    var?2 dd 12551 + 0 + 2*1    
    ident dd 125*2 - 10 + 25/5 + 89 
    var@3 dw 765o   
    var_4 db -15
    sVar1 db 5 +++ -6 
    sVar2 db 5 * 6 - 2   ;; 0Ah 
    sVar3 dw ((4 + 2)*5 - 101b + 064h)/31q  ;; should be 05h
    id6 db 0 + (-1)*14*(-1) + 0  ;; should be 0Eh
    id7 db -1 * 64 * -1  ;; should be 40h
    id8 dw 8 mod 2   ;; should be 0000h
    id9 dd ((9 mod 2 + 0fh) mod 17 ) mod ( -1 + 4 * 3 - ( 4 / 2) ) ;; should be 0007h 
data ends 

    end begin
          
     
