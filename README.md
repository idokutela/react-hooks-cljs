# react-hooks-cljs
A very light wrapper on react hooks.

In addition to wrapping the various hook functions, it provides a
"state atom" that can be created inside of the functions that define
components. State atoms are like atoms, but should only be used to
hold component state. They cannot be watched.

## Example

A very basic example:

```cljs
;;; A simple counter
(defn counter
  []
  (let [count (state-atom init)]
    (.createElement React
	 "button"
	 #js {:onClick #(swap! count inc)}
	 #js [(str "Clicked: " @count)])))

(defn popup
  "A really annoying component!"
  []
  (use-effect! #(js/alert "Rendered!"))
  "I'm going to annoy the hell out of you by popping up an alert.")
```

## API

### Hooks

The react-hooks api is exported as is, with camel case becoming kebab
case, and exclamation marks added to reflect that they add impurity.

For example, the following are equivalent:

```js
function Counter() {
  const [count setCount] = useState(0);
  const incr = () => setCount(count + 1);
  return <Button onClick={incr}>Clicks: {count}</Button>;
}
```

```cljs
(defn Counter
  []
  (let [[count set-count!] (use-state! 0)]
    (.createElement React
	  "button"
	  #js {:onClick #(set-count! (inc count))}
	  #js [(str "Clicks: " count)])))
```

The following are available: `use-state!`, `use-effect!`,
`use-context!`, `use-reducer!`, `use-callback!`, `use-memo!`,
`use-ref!`, `use-imperative-handle!`, `use-layout-effect!`,
`use-debug-value!`. For information as to how they work, see [the
react docs](https://reactjs.org/docs/hooks-reference.html#usecontext).

In addition, the convenience method `current<-` is provided, that
extracts the current value of a ref. For example:

```cljs
(defn annoying-focus-button
 []
 (let [cmp (use-ref! nil)]
  (use-effect! (.focus (current<- ref)))
  (.createElement React "button" #js {} #js ["I steal focus when I render"])))
```

### State atoms

To make dealing with state easier, `react-hooks-cljs` provides
`state-atom`. This is just almost like an atom, except, under the
hood, uses the `useState` hook to take care of its value. One can use
it in a component to keep state conveniently.

One derefs them inside rendering functions to get their value, and
uses `swap!` and `reset!` to set their value.

One caveat: unlike actual atoms, `StateAtom`s *cannot be
watched*.

Look [here](#Example) for an example.

## License

MIT
