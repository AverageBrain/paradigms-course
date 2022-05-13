(defn vector-every-item-struct [struct] (fn [arg] (and (vector? arg) (every? struct arg))) )

(def is-vector? (vector-every-item-struct number?))

(defn size-equals [args] (apply == (mapv count args)))

(def is-matrix? (vector-every-item-struct is-vector?))

(defn create-base-operation [oper checker] (fn [& args]
                   {:pre [(and (every? checker args) (size-equals args))]
                    :post [(checker %)]}
                   (apply mapv oper args)))

(def v+ (create-base-operation + is-vector?))
(def v- (create-base-operation - is-vector?))
(def v* (create-base-operation * is-vector?))
(def vd (create-base-operation / is-vector?))

(defn scalar [& args] (apply + (apply v* args)))

(defn vect2 [a b] (mapv (partial scalar b) [[0 (- (nth a 2)) (nth a 1)]
                                            [(nth a 2) 0 (- (nth a 0))]
                                            [(- (nth a 1)) (nth a 0) 0]])
)

(defn vect [& a] {:pre [(and (every? is-vector? a) (size-equals a))]
                  :post [(is-vector? %)]}
                  (reduce vect2 a))

(defn v*s [v & s] {:pre [(and (is-vector? v) (every? number? s))]
                   :post [(is-vector? %)]} (mapv (partial * (apply * s)) v))

(def m+ (create-base-operation v+ is-matrix?))
(def m- (create-base-operation v- is-matrix?))
(def m* (create-base-operation v* is-matrix?))
(def md (create-base-operation vd is-matrix?))

(defn m*s [m & s] {:pre [(and (is-matrix? m) (every? number? s))]
                   :post [(is-matrix? %)]}
  (mapv (fn [str] (v*s str (apply * s))) m))

(defn m*v [m v] {:pre [(and (is-matrix? m) (is-vector? v))]
                 :post [(is-vector? %)]}
                (mapv (partial scalar v) m))

(defn transpose [m] {:pre [(is-matrix? m)]
                     :post [(if (empty? (first m))
                              (and (is-vector? %) (empty %))
                              (and
                              (is-matrix? %)
                              (== (count m) (count (first %)))
                              (== (count (first m)) (count %))))]}
  (apply mapv vector m))

(defn m*m2 [m1 m2] {:pre [(and (is-matrix? m1) (is-matrix? m2) (== (count (first m1)) (count m2)))]}
  (mapv (partial m*v (transpose m2)) m1))

(defn m*m [& m] (reduce m*m2 m))

(defn is-tensor? [arg]
  (or
    (number? arg)
    (is-vector? arg)
    (and (every? is-tensor? arg) (apply == (mapv count arg)))
  ))

(def is-vector-vector (vector-every-item-struct vector?))

(defn tensors-size-equals? [t1 t2]
  (or
    (and (number? t1) (number? t2))
    (and (is-vector? t1) (is-vector? t2) (== (count t1) (count t2)))
    (and
      (and
        (is-vector-vector t1)
        (is-vector-vector t2)
        (== (count t1) (count t2))
      )
      (every? true? (mapv tensors-size-equals? t1 t2))
    )))

(defn is-suffix? [t1 t2]
   (or (tensors-size-equals? t1 t2)
      (and (vector? t1) (is-suffix? (first t1) t2))))

(defn broadcast [t1 t2] {:pre  [(is-suffix? t1 t2)]
                        :post [(tensors-size-equals? t1 %)]}
  (if (tensors-size-equals? t1 t2) t2 (mapv (fn [layer] (broadcast layer t2)) t1)))

(defn apply-function-to-broadcast [f]
  (fn [& args]
    (if (number? (first args)) (apply f args)
      (apply mapv (apply-function-to-broadcast f) args))))

(defn tensor-function-2args [f] (fn [t1 t2] {:pre [(and (is-tensor? t1) (is-tensor? t2))]
                                             :post [(is-tensor? %)]}
  ((apply-function-to-broadcast f)
    (if (is-suffix? t1 t2) (identity t1) (broadcast t2 t1))
    (if (is-suffix? t2 t1) (identity t2) (broadcast t1 t2)))))

(defn create-operation [oper neutral] (fn [& args]
  (if (== 1 (count args))
    ((tensor-function-2args oper) neutral (first args))
    (reduce (tensor-function-2args oper) args))))

(def hb+ (create-operation + 0))
(def hb- (create-operation - 0))
(def hb* (create-operation * 1))
(def hbd (create-operation / 1))
