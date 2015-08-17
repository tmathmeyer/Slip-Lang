#A compiler and interpreter for the Slip Language.

##Language Features:
Slip is a pretty generic breed of lisp. It currently supports basic math expressions, including: (+, -, *, /, >, <, =). It also has a working recursive macro system, and a small standard library.
###Math Examples
````
(+ 1 1)   -> 2
(+ 1 1 1)   -> 3
(- 1 1 1)   -> -1
(* 2 3 4)   -> 24
(/ 72 6 4)   -> 3
(> 3 2 1)   -> true
(> 3 1 2)   -> false
````
###Macro Examples
````
(# (example a b c) (cons a (cons (+ b c) empty)))
(example 2 3 4)   ->  [2, 7]

(# (sumall a ...) (+ a ...))
(sumall 2 3 4 5 6)   -> 20

(# (list a) (cons a empty))
(# (list a b ...) (cons a (list b ...)))
(list 2 3 4 5)   -> [2, 3, 4, 5]

(# (switch (x) (v -> s) ...) (lambda (x) (_switch (v s) ...)))
(# (_switch (v s)) (if (= x v) s #void))
(# (_switch (v s) (v2 s2) ...) (if (= x v) s (_switch (v2 s2) ...)))

((switch (x) (3 -> "three")
             (4 -> "four")
             (5 -> "five")) 4)   -> "four"
((switch (x) (3 -> "three")
             (4 -> "four")
             (5 -> "five")) 6)   -> null
````
###Defining symbols
Definitions are done through the use of the `(#def name val)` function. When executing Slip on a file, definitions will be processed before other commands. Mutial Recursion still needs to be worked out a bit better. There is also syntactic suger provided with sandard builtin macros that allows functions to be defined such as:
````
(#def double (x) (+ x x))
(#def begin (x y) y)

(double 2)   -> 4
(begin 3 4)   -> 4
(begin (print 3) 4)   -> 4 (and prints 3)
````
###Structures
Custom Structures Can also be defined with `( #sdef name (fields ...))` This is the same as defining a function called `is-"name"`, `make-"name"`, and then one function for each field, called `"name"-"field"`
````
(#sdef triple (a b c))
(make-triple 1 2 3)   -> #struct#triple
(is-triple (make-triple 1 2 3))   -> true
(is-triple 2)   -> false
(triple-a (make-triple 1 2 3))   -> 1
````

----
##TODO:
 - finish targeting x86
 - improve (create) documentation
 - a better runtime
 - reduce interpreter code to macros
 - polynomial support?!?
 - string operations
 - file / io operations (eval/loader merge)
