# TicTacOm

React's tutorial rewritten in Om Next

## Running

### Leiningen

If you have Leiningen installed, use the following command to run the app:

```
lein run -m clojure.main script/figwheel.clj
```

### IntelliJ IDEA

If you use IntelliJ IDEA and the Cursive Plugin, you can create an according Run Configuration using the Leiningen support:

1. Add New Configuration: Leiningen
2. Add `lein run -m clojure.main script/figwheel.clj` to arguments
3. Run the configuration


You can now play the Tic Tac Toe game at http://localhost:3449
