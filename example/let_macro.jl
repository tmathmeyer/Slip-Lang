(#redef (letr ((iname iexpr) (name expr) ...) evalme)
        ((lambda (iname) (letr ((name expr) ...) evalme)) iexpr))

(#redef (letr ((iname iexpr)) evalme)
        ((lambda (iname) evalme) iexpr))

(letr ((x 5) (y 6) (z 7))
  (+ x y z))
