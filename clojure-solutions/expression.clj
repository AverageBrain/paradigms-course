; --- HW10 ---

(def constant constantly)
(defn variable [name] (fn [vars] (vars name)))

(defn create-operation [oper]
  (fn [& args]
    (fn [vars]
      (apply oper (mapv (fn [x] (x vars)) args)))))

(def add (create-operation +))
(def subtract (create-operation -))
(def multiply (create-operation *))

(defn double-divide [& args]
  (if (== 1 (count args))
    (/ 1.0 (first args))
    (reduce #(/ (double %1) (double %2)) args)))

(def divide (create-operation double-divide))

(def negate subtract)

(def exp (create-operation (fn [x] (Math/exp x))))
(defn sumexp [& args] (apply add (mapv exp args)))
(defn softmax [& args] (divide (sumexp (first args)) (apply sumexp args)))

(def OPERATIONS {
                 '+       add,
                 '-       subtract,
                 '*       multiply,
                 '/       divide,
                 'negate  negate,
                 'sumexp  sumexp,
                 'softmax softmax
                 })

(defn parse [expr]
  (cond
    (list? expr) (apply (get OPERATIONS (first expr)) (mapv parse (rest expr))) ; first - operation, other - arguments
    (number? expr) (constant expr)
    :else (variable (str expr))
    ))

(defn parseFunction [input] (parse (read-string input)))

; --- HW11 ---

(definterface Expression
  (^Number evaluate [vars])
  (^String toString [])
  (^Object diff [diffVar]))

(deftype PartOfExpression [eval to-string diff-rules args]
  Expression
  (evaluate [this vars] (eval args vars))
  (toString [this] (to-string args))
  (diff [this diffVar] (diff-rules args diffVar)))

(defn make-expression [evaluate toString diff]
  (fn [args] (PartOfExpression. evaluate toString diff args)))

(def Constant (make-expression
                (fn [args vars] args)
                (fn [args] (str args))
                (fn [args diffVar] (Constant 0))))

(def Variable (make-expression
                (fn [args vars] (vars args))
                (fn [args] (str args))
                (fn [args diffVar] (if (= diffVar args) (Constant 1) (Constant 0)))))

(deftype Operation [sign operation diff-rules args]
  Expression
  (evaluate [this vars] (apply operation (mapv #(.evaluate % vars) args)))
  (toString [this] (str "(" sign " " (clojure.string/join " " (mapv #(.toString %) args)) ")"))
  (diff [this diff-var] (diff-rules args diff-var)))

(defn toString [expr] (.toString expr))
(defn evaluate [expr vars] (.evaluate expr vars))
(defn diff [expr diff-var] (.diff expr diff-var))

(defn make-operation [sign operation diff-rules]
  (fn [& args] (Operation. sign operation diff-rules args)))

(def Add (make-operation "+" + (fn [args diffVar] (apply Add (mapv #(diff % diffVar) args)))))

(def Subtract (make-operation "-" - (fn [args diffVar] (apply Subtract (mapv #(diff % diffVar) args)))))

(def Multiply (make-operation "*" *
                              (fn [args diffVar]
                                (if (== (count args) 1)
                                  (Multiply (diff (first args) diffVar))
                                  (Add
                                    (Multiply (diff (first args) diffVar) (apply Multiply (rest args)))
                                    (Multiply (first args) (diff (apply Multiply (rest args)) diffVar)))
                                  )
                                )))


(def Divide (make-operation "/" double-divide (fn [args diffVar]
                                                (if (== (count args) 1)
                                                  (diff (Divide (Constant 1) (first args))  diffVar)
                                                  (Divide
                                                    (Subtract
                                                      (Multiply (diff (first args) diffVar) (apply Multiply (rest args)))
                                                      (Multiply (first args) (diff (apply Multiply (rest args)) diffVar)))
                                                    (Multiply (apply Multiply (rest args)) (apply Multiply (rest args))))
                                                  ))))

(def Negate (make-operation "negate" #(- %) (fn [o diffVar] (Negate (diff (first o) diffVar)))))

(defn sum-exp [& args]
  (if (== 1 (count args))
    (Math/exp (first args))
    (reduce #(+ %1 (Math/exp %2)) (conj args 0))))


(def Sumexp (make-operation "sumexp"
                            sum-exp
                            (fn [args diffVar]
                              (apply Add (mapv #(Multiply (diff % diffVar) (Sumexp %)) args)))))

(defn soft-max [& args]
  (if (== 1 (count args))
    1
    (/ (sum-exp (first args)) (apply sum-exp args))))

(def Softmax (make-operation "softmax" soft-max
                             (fn [args diffVar] (diff (Divide (Sumexp (first args)) (apply Sumexp args)) diffVar))))

(def OBGECT_OPERATIONS {
                        '+       Add,
                        '-       Subtract,
                        '*       Multiply,
                        '/       Divide,
                        'negate  Negate,
                        'sumexp  Sumexp,
                        'softmax Softmax
                        })

(defn parse_object [expr]
  (cond
    (list? expr) (apply (get OBGECT_OPERATIONS (first expr)) (map parse_object (rest expr))) ; first - operation, other - arguments
    (number? expr) (Constant expr)
    :else (Variable (str expr))
    ))

(defn parseObject [input] (parse_object (read-string input)))

; (def expr (Sumexp (Variable "x") (Variable "x") (Variable "x")))
; (println (evaluate expr {"x" 0}))
; (println (toString (diff expr "y")))

; (println (toString (parseObject "(- (* 2 x) 3)")))
