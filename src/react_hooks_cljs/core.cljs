(ns react-hooks-cljs.core
  (:require ["react" :as React]))

(def use-state! (.-useState React))
(def use-effect! (.-useEffect React))
(def use-context! (.-useContext React))
(def use-reducer! (.-useReducer React))
(def use-callback! (.-useCallback React))
(def use-memo! (.-useMemo React))
(def use-ref! (.-useRef React))
(defn current<- [ref]
  "Extracts the current value of the ref."
  (.-current ref))
(def use-imperative-handle! (.-useImperativeHandle React))
(def use-layout-effect! (.-useLayoutEffect React))
(def use-debug-value! (.-useDebugValue React))


(deftype StateAtom [^:mutable state meta validator set-state!]
  IAtom

  IEquiv
  (-equiv [o other] (identical? o other))

  IDeref
  (-deref [this]
    state)

  IReset
  (-reset! [a new-value]
    (when-not (nil? validator)
      (assert (validator new-value) "Validator rejected reference state"))
    (let [old-value state]
      (set-state! new-value)
      new-value))

  ISwap
  (-swap! [a f]          (-reset! a (f state)))
  (-swap! [a f x]        (-reset! a (f state x)))
  (-swap! [a f x y]      (-reset! a (f state x y)))
  (-swap! [a f x y more] (-reset! a (apply f state x y more)))

  IWithMeta
  (-with-meta [_ new-meta] (StateAtom. state new-meta validator set-state!))

  IMeta
  (-meta [_] meta)

  IPrintWithWriter
  (-pr-writer [a w opts] (pr-atom a w opts "Atom:"))

  IHash
  (-hash [this] (goog/getUid this)))


(defn state-atom
  "Creates a state atom in a component.

  Use only inside rendering functions!

  Note: unlike actual atoms, state atoms cannot be watched. This is
  because the update is handled by set-state, and this means the
  actual update happens later than the setting of the new value.

  In practise, this should be no problem: one should only use the
  value of state atoms when rendering, and by then, any updates will
  have occurred."
  [x & {:keys [meta validator]}]
  (let [[state set-state!] (use-state! x)]
    (StateAtom. state meta validator set-state!)))
