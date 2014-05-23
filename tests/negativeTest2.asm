
data segment 
    id1 dw   ;;error 
    id2      ;; error
    id3 dw 0 
    id4 dw id3  ;;error 
    id5 dw 0,2,3,4,5 ;;error
    id6 db 0 + (-1)*14*(-1) + 0
    id7 db -1 * 100 * -1
    mod db -1   ;; error - mod is reserved
    eax dw 0    ;; error
    i1 db 2*(2 + ( / 2 ) )  ;; error
    i2 db (4 + 4    ;; error
    i3 db 2/    ;; error
data ends
    
adc segment ;; error

adc ends    ;; error
    
code segment 
   assume cs : code , ds : data
begin: 

mov:    ;; error
    or al, * 1  ;; error
    or al, 2*() ;; error
    or al, +1 - 5*) ;; error
mod:    ;; error
    
code ends
    end begin,start ;; error