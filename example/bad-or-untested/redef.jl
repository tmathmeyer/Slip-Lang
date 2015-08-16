(#redef (switch         (num -> fn)           ...)
        (lambda (x)     (cond ((= x num) fn) ...)))

(((switch (1 -> (lambda (x) (print x)))
         (2 -> (lambda (x) (* x x)))
         (3 -> (lambda (x) (+ x x)))) 2) 20)
        
(+ 3 4 5)