(ns om-tutorial.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

; Gets player symbol for boolean
(defn get-symbol [n] (if n "X" "O"))

; Generates a function that will update a slot
; of the field, based on the current player.
; Then the current player is set to the next one.
(defn update-field [slot] (fn [state] (assoc state :field (assoc (:field state) slot (get-symbol (:xIsNext state)))
                                                   :xIsNext (not (:xIsNext state)))))

; Will reset each slot
; of the field by initilizing it with an empty string.
; Then the current player is set to X.
(defn reset-field [state] (assoc state
                            :field (vec (map (fn [_] "") (range 9)))
                            :xIsNext true))

(defn read
  [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

(defmulti mutate om/dispatch)

(defmethod mutate 'field/reset
  [{:keys [state] :as env} key params]
     {:action #(swap! state reset-field)})

(defmethod mutate 'field/update
     [{:keys [state]} _ {:keys [id]}]
      {:action #(swap! state (update-field id))})

; Evaluates a winner
; Returns X or O on win, or "" (empty string), if the game is still on.
; Recursively checks every possible win line.
(defn check-win-lines [field lines]
  (if (> (count lines) 0)
    (if (and
          (not= (nth field (nth (first lines) 0)) "")
          (=
            (nth field (nth (first lines) 0))
            (nth field (nth (first lines) 1))
            (nth field (nth (first lines) 2))))
      (nth field (nth (first lines) 0))
      (check-win-lines field (rest lines)))
    ""))

(defn winner [field]
  (check-win-lines field [
                [0, 1, 2],
                [3, 4, 5],
                [6, 7, 8],
                [0, 3, 6],
                [1, 4, 7],
                [2, 5, 8],
                [0, 4, 8],
                [2, 4, 6],
                ]))

(def app-state (atom (reset-field {})))

; One slot of the field.
; It gets its id (0-8) and a copy of the full field as props.
(defui Square
  static om/IQuery
  (query [this] '[:field])
  Object
  (render [this]
    (let [{:keys [id field xIsNext] :as props} (om/props this)]
    (dom/button
      #js
          {
             :className "square"
             :onClick (fn [e]
                        (if (and (= (nth field id) "") (= (winner field) ""))
                          (om/transact! reconciler `[(field/update {:id ~id})])))
           }
      (nth field id)))))
(def square (om/factory Square {:keyfn :id})) ; Use props "id" as react-id

; Main component
; Gets app-state injected via props
(defui Board
    static om/IQuery
    (query [this] '[:field :xIsNext])
  Object
  (render [this]
    (let [{:keys [field xIsNext] :as props} (om/props this)]
      (dom/div nil
               (dom/div #js {:className "board-row"}
                      (square (assoc props :id 0))
                      (square (assoc props :id 1))
                      (square (assoc props :id 2)))
               (dom/div #js {:className "board-row"}
                      (square (assoc props :id 3))
                      (square (assoc props :id 4))
                      (square (assoc props :id 5)))
               (dom/div #js {:className "board-row"}
                      (square (assoc props :id 6))
                      (square (assoc props :id 7))
                      (square (assoc props :id 8)))
               (if (= (winner field) "")
                 (dom/p nil (str "Next: " (get-symbol xIsNext)))
                 (dom/p nil (str "Gewinner: " (winner field))))
               (dom/button #js { :onClick (fn [e] (om/transact! reconciler `[(field/reset)]))
                      } "Reset")
               ))))

  (def reconciler
    (om/reconciler
      {:state  app-state
       :parser (om/parser {:read read :mutate mutate})}))


; Configure root component. The reconciler will watch
; for changes of the app state and will inject the
; app-state as props to the Board component.
(om/add-root! reconciler Board (gdom/getElement "app"))
