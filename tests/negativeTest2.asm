
data segment 
    ident1 dw   ;;error 
    ident2 
    ident3 dw 0 ;;error
    ident4 dw ident3   
    ident5 dw 0,2,3,4,5 ;;error
data ends
    
    
code segment 

begin: 


code ends
    end begin,start ;; error