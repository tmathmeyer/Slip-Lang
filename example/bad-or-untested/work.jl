(#def ml (cons 3 (cons 4 empty)))

(first ml)

(#def suml (x)
    (if (empty? x) 0
        (+ (first x) (suml (rest x)))))

(suml ml)