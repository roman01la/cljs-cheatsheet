(ns cljs-cheatsheet-server.core
  (:require-macros
    [hiccups.core :as hiccups])
  (:require
    [clojure.string :refer [blank? join replace]]
    [cljs-cheatsheet.util :refer [docs-href js-log log]]
    [hiccups.runtime :as hiccupsrt]))

;; This file produces:
;; - public/index.html
;; - symbols.json

;; NOTE: this file is pretty messy; it could stand to be cleaned up some

(def fs (js/require "fs"))

(def html-encode js/goog.string.htmlEscape)

(def cljs-core-ns "cljs.core")
(def clj-string-ns "clojure.string")
(def clj-set-ns "clojure.set")

(def symbols
  "Keeps track of the symbols on the cheatsheet that need tooltips.
   Used to produce symbols.json"
  (atom #{}))

;;------------------------------------------------------------------------------
;; Helpers
;;------------------------------------------------------------------------------

(defn- json-stringify [js-thing]
  (js/JSON.stringify js-thing nil 2))

(hiccups/defhtml tt-icon [id]
  [:img.tooltip-icon-0e91b
    {:alt ""
     :data-info-id id
     :src "img/info-circle.svg"}])

(hiccups/defhtml literal [n]
  [:span.literal-c3029 n])

(hiccups/defhtml fn-link
  ([symbol-name]
   (fn-link symbol-name cljs-core-ns))
  ([symbol-name name-space]
   (let [full-name (str name-space "/" symbol-name)
         ;; add this symbol to the docs list
         _ (swap! symbols conj full-name)]
     [:a.fn-a8476
       {:data-full-name full-name
        :href (docs-href symbol-name name-space)}
       (html-encode symbol-name)])))

(hiccups/defhtml inside-fn-link
  ([symbol-name]
   (inside-fn-link symbol-name cljs-core-ns))
  ([symbol-name name-space]
   (let [full-name (str name-space "/" symbol-name)
         ;; add this symbol to the docs list
         _ (swap! symbols conj full-name)]
     [:a.inside-fn-c7607
       {:data-full-name (str name-space "/" symbol-name)
        :href (docs-href symbol-name name-space)}
       (html-encode symbol-name)])))

;;------------------------------------------------------------------------------
;; Sections
;;------------------------------------------------------------------------------

(hiccups/defhtml basics-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Основы"]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Определение" (tt-icon "define")]
          [:td.body-885f4
            (fn-link "def")
            (fn-link "defn")
            (fn-link "defn-")
            (fn-link "let")
            (fn-link "letfn")
            (fn-link "declare")
            (fn-link "ns")]]
        [:tr
          [:td.label-9e0b7 "Ветвление" (tt-icon "branch")]
          [:td.body-885f4
            (fn-link "if")
            (fn-link "if-not")
            (fn-link "when")
            (fn-link "when-not")
            (fn-link "when-let")
            (fn-link "when-first")
            (fn-link "if-let")
            (fn-link "cond")
            (fn-link "condp")
            (fn-link "case")
            (fn-link "when-some")
            (fn-link "if-some")]]
        [:tr
          [:td.label-9e0b7 "Сравнение"]
          [:td.body-885f4
            (fn-link "=")
            (fn-link "not=")
            (fn-link "and")
            (fn-link "or")
            (fn-link "not")
            (fn-link "identical?")
            (fn-link "compare")]]
        [:tr
          [:td.label-9e0b7 "Итерирование"]
          [:td.body-885f4
            (fn-link "map")
            (fn-link "map-indexed")
            (fn-link "reduce")
            (fn-link "for")
            (fn-link "doseq")
            (fn-link "dotimes")
            (fn-link "while")]]
        [:tr
          [:td.label-9e0b7 "Проверки"]
          [:td.body-885f4
            (fn-link "true?")
            (fn-link "false?")
            (fn-link "instance?")
            (fn-link "nil?")
            (fn-link "some?")]]]]])

(hiccups/defhtml functions-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "#( ) Функции" (tt-icon "functions")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            [:div.row-5dec8 "#(...) &rarr; (fn [args] (...))"
              (tt-icon "function-shorthand")]
            (fn-link "fn")
            (fn-link "defn")
            (fn-link "defn-")
            (fn-link "identity")
            (fn-link "constantly")
            (fn-link "comp")
            (fn-link "complement")
            (fn-link "partial")
            (fn-link "juxt")
            (fn-link "memoize")
            (fn-link "fnil")
            (fn-link "every-pred")
            (fn-link "some-fn")]]
        [:tr
          [:td.label-9e0b7 "Вызов"]
          [:td.body-885f4
            (fn-link "apply")
            (fn-link "->")
            (fn-link "->>")
            (fn-link "as->")
            (fn-link "cond->")
            (fn-link "cond->>")
            (fn-link "some->")
            (fn-link "some->>")]]
        [:tr
          [:td.label-9e0b7 "Проверки"]
          [:td.body-885f4
            (fn-link "fn?")
            (fn-link "ifn?")]]]]])

(hiccups/defhtml numbers-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Числа" (tt-icon "numbers")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Литералы"]
          [:td.body-885f4
            (literal "7")
            (literal "3.14")
            (literal "-1.2e3")
            (literal "0x0000ff")]]
        [:tr
          [:td.label-9e0b7 "Арифметика"]
          [:td.body-885f4
            (fn-link "+")
            (fn-link "-")
            (fn-link "*")
            (fn-link "/")
            (fn-link "quot")
            (fn-link "rem")
            (fn-link "mod")
            (fn-link "inc")
            (fn-link "dec")
            (fn-link "max")
            (fn-link "min")]]
        [:tr
          [:td.label-9e0b7 "Сравнение"]
          [:td.body-885f4
            (fn-link "=")
            (fn-link "==")
            (fn-link "not=")
            (fn-link "<")
            (fn-link ">")
            (fn-link "<=")
            (fn-link ">=")
            (fn-link "compare")]]
        [:tr
          [:td.label-9e0b7 "Приведение типов"]
          [:td.body-885f4
            (fn-link "int")]]
        [:tr
          [:td.label-9e0b7 "Проверки"]
          [:td.body-885f4
            (fn-link "zero?")
            (fn-link "pos?")
            (fn-link "neg?")
            (fn-link "even?")
            (fn-link "odd?")
            (fn-link "number?")
            (fn-link "integer?")]]
        [:tr
          [:td.label-9e0b7 "Случайные"]
          [:td.body-885f4
            (fn-link "rand")
            (fn-link "rand-int")]]]]])

(hiccups/defhtml strings-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "\" \" Строки" (tt-icon "strings")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            (literal "\"abc\"")
            (fn-link "str")
            (fn-link "name")]]
        [:tr
          [:td.label-9e0b7 "Операции"]
          [:td.body-885f4
            (literal "(.-length my-str)")
            (fn-link "count")
            (fn-link "get")
            (fn-link "subs")
            (literal "(clojure.string/)")
            (fn-link "join" clj-string-ns)
            (fn-link "escape" clj-string-ns)
            (fn-link "split" clj-string-ns)
            (fn-link "split-lines" clj-string-ns)
            (fn-link "replace" clj-string-ns)
            (fn-link "replace-first" clj-string-ns)
            (fn-link "reverse" clj-string-ns)]]
        [:tr
          [:td.label-9e0b7 "Регулярные выражения"]
          [:td.body-885f4
            [:span.literal-c3029 "#\"" [:span {:style "font-style:italic"} "pattern"] "\""]
            (fn-link "re-find")
            (fn-link "re-seq")
            (fn-link "re-matches")
            (fn-link "re-pattern")
            (literal "(clojure.string/)")
            (fn-link "replace" clj-string-ns)
            (fn-link "replace-first" clj-string-ns)]]
        [:tr
          [:td.label-9e0b7 "Буквы"]
          [:td.body-885f4
            (literal "(clojure.string/)")
            (fn-link "capitalize" clj-string-ns)
            (fn-link "lower-case" clj-string-ns)
            (fn-link "upper-case" clj-string-ns)]]
        [:tr
          [:td.label-9e0b7 "Удаление пробелов"]
          [:td.body-885f4
            (literal "(clojure.string/)")
            (fn-link "trim" clj-string-ns)
            (fn-link "trim-newline" clj-string-ns)
            (fn-link "triml" clj-string-ns)
            (fn-link "trimr" clj-string-ns)]]
        [:tr
          [:td.label-9e0b7 "Проверки"]
          [:td.body-885f4
            (fn-link "char")
            (fn-link "string?")
            (literal "(clojure.string/)")
            (fn-link "blank?" clj-string-ns)]]]]])

(hiccups/defhtml atoms-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Атомы / Состояние" (tt-icon "atoms")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            (fn-link "atom")]]
        [:tr
          [:td.label-9e0b7 "Получение значения"]
          [:td.body-885f4
            [:span.literal-c3029 "@my-atom &rarr; (" (inside-fn-link "deref") " my-atom)"]]]
        [:tr
          [:td.label-9e0b7 "Изменение значения"]
          [:td.body-885f4
            (fn-link "swap!")
            (fn-link "reset!")
            (fn-link "compare-and-set!")]]
        [:tr
          [:td.label-9e0b7 "Наблюдение"]
          [:td.body-885f4
            (fn-link "add-watch")
            (fn-link "remove-watch")]]
        [:tr
          [:td.label-9e0b7 "Валидация"]
          [:td.body-885f4
            (fn-link "set-validator!")
            (fn-link "get-validator")]]]]])

(hiccups/defhtml js-interop-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Взаимодействие с JavaScript"]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание объекта"]
          [:td.body-885f4
            (literal "#js {}")
            (fn-link "js-obj")]]
        [:tr
          [:td.label-9e0b7 "Создание массива"]
          [:td.body-885f4
            (literal "#js []")
            (fn-link "array")
            (fn-link "make-array")
            (fn-link "aclone")]]
        [:tr
          [:td.label-9e0b7 "Получение значения свойства"]
          [:td.body-885f4
            [:div.row-5dec8 "(.-innerHTML el)"]
            [:div.row-5dec8 "(" (inside-fn-link "aget") " el \"innerHTML\")"]]]
        [:tr
          [:td.label-9e0b7 "Присваивание значения свойству"]
          [:td.body-885f4
            [:div.row-5dec8 "(" (inside-fn-link "set!") " (.-innerHTML el) \"Hi!\")"]
            [:div.row-5dec8 "(" (inside-fn-link "aset") " el \"innerHTML\" \"Hi!\")"]]]
        [:tr
          [:td.label-9e0b7 "Удаление свойства"]
          [:td.body-885f4
            (fn-link "js-delete")]]
        [:tr
          [:td.label-9e0b7 "Преобразование"]
          [:td.body-885f4
            (fn-link "clj->js")
            (fn-link "js->clj")]]
        [:tr
          [:td.label-9e0b7 "Проверки типов"]
          [:td.body-885f4
            (fn-link "array?")
            (fn-link "fn?")
            (fn-link "number?")
            (fn-link "object?")
            (fn-link "string?")]]
        [:tr
          [:td.label-9e0b7 "Исключения"]
          [:td.body-885f4
            (fn-link "try")
            (fn-link "catch")
            (fn-link "finally")
            (fn-link "throw")]]
        [:tr
          [:td.label-9e0b7 "Внешняя библиотека"]
          [:td.body-885f4
            [:div.row-5dec8 "(js/alert \"Hello world!\")"]
            [:div.row-5dec8 "(js/console.log my-obj)"]
            [:div.row-5dec8 "(.html (js/jQuery \"#myDiv\") \"Hi!\")"]]]]]])

(hiccups/defhtml collections-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Коллекции" (tt-icon "collections")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Базовые операции"]
          [:td.body-885f4
            (fn-link "count")
            (fn-link "empty")
            (fn-link "not-empty")
            (fn-link "into")
            (fn-link "conj")]]
        [:tr
          [:td.label-9e0b7 "Проверки содержимого"]
          [:td.body-885f4
            (fn-link "distinct?")
            (fn-link "empty?")
            (fn-link "every?")
            (fn-link "not-every?")
            (fn-link "some")
            (fn-link "not-any?")]]
        [:tr
          [:td.label-9e0b7 "Проверки возможностей"]
          [:td.body-885f4
            (fn-link "sequential?")
            (fn-link "associative?")
            (fn-link "sorted?")
            (fn-link "counted?")
            (fn-link "reversible?")]]
        [:tr
          [:td.label-9e0b7 "Проверки типов"]
          [:td.body-885f4
            (fn-link "coll?")
            (fn-link "list?")
            (fn-link "vector?")
            (fn-link "set?")
            (fn-link "map?")
            (fn-link "seq?")]]]]])

(hiccups/defhtml lists-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "( ) Списки" (tt-icon "lists")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            (literal "'()")
            (fn-link "list")
            (fn-link "list*")]]
        [:tr
          [:td.label-9e0b7 "Получение значений"]
          [:td.body-885f4
            (fn-link "first")
            (fn-link "nth")
            (fn-link "peek")]]
        [:tr
          [:td.label-9e0b7 "'Изменение'"]
          [:td.body-885f4
            (fn-link "cons")
            (fn-link "conj")
            (fn-link "rest")
            (fn-link "pop")]]]]])

(hiccups/defhtml vectors-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "[ ] Векторы" (tt-icon "vectors")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            (literal "[]")
            (fn-link "vector")
            (fn-link "vec")]]
        [:tr
          [:td.label-9e0b7 "Получение значений"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-vec idx) &rarr; (" (inside-fn-link "nth") " my-vec idx)"
              (tt-icon "vector-as-fn")]
            (fn-link "get")
            (fn-link "peek")]]
        [:tr
          [:td.label-9e0b7 "'Изменение'"]
          [:td.body-885f4
            (fn-link "assoc")
            (fn-link "pop")
            (fn-link "subvec")
            (fn-link "replace")
            (fn-link "conj")
            (fn-link "rseq")]]
        [:tr
          [:td.label-9e0b7 "Итерирование"]
          [:td.body-885f4
            (fn-link "mapv")
            (fn-link "filterv")
            (fn-link "reduce-kv")]]]]])

(hiccups/defhtml sets-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "#{ } Множества" (tt-icon "sets")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            (literal "#{}")
            (fn-link "set")
            (fn-link "hash-set")
            (fn-link "sorted-set")
            (fn-link "sorted-set-by")]]
        [:tr
          [:td.label-9e0b7 "Получение значений"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-set itm) &rarr; (" (inside-fn-link "get") " my-set itm)"
              (tt-icon "set-as-fn")]
            (fn-link "contains?")]]
        [:tr
          [:td.label-9e0b7 "'Изменение'"]
          [:td.body-885f4
            (fn-link "conj")
            (fn-link "disj")]]
        [:tr
          [:td.label-9e0b7 "Операции"]
          [:td.body-885f4
            (literal "(clojure.set/)")
            (fn-link "union" clj-set-ns)
            (fn-link "difference" clj-set-ns)
            (fn-link "intersection" clj-set-ns)
            (fn-link "select" clj-set-ns)]]
        [:tr
          [:td.label-9e0b7 "Проверки"]
          [:td.body-885f4
            (literal "(clojure.set/)")
            (fn-link "subset?" clj-set-ns)
            (fn-link "superset?" clj-set-ns)]]]]])

(hiccups/defhtml maps-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "{ } Словари" (tt-icon "maps")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Создание"]
          [:td.body-885f4
            [:div.row-5dec8 "{:key1 \"a\" :key2 \"b\"}"]
            (fn-link "hash-map")
            (fn-link "array-map")
            (fn-link "zipmap")
            (fn-link "sorted-map")
            (fn-link "sorted-map-by")
            (fn-link "frequencies")
            (fn-link "group-by")]]
        [:tr
          [:td.label-9e0b7 "Получение значений"]
          [:td.body-885f4
            [:div.row-5dec8
              "(:key my-map) &rarr; (" (inside-fn-link "get") " my-map :key)"
              (tt-icon "keywords-as-fn")]
            (fn-link "get-in")
            (fn-link "contains?")
            (fn-link "find")
            (fn-link "keys")
            (fn-link "vals")]]
        [:tr
          [:td.label-9e0b7 "'Изменение'"]
          [:td.body-885f4
            (fn-link "assoc")
            (fn-link "assoc-in")
            (fn-link "dissoc")
            (fn-link "merge")
            (fn-link "merge-with")
            (fn-link "select-keys")
            (fn-link "update-in")]]
        [:tr
          [:td.label-9e0b7 "Записи"]
          [:td.body-885f4
            (fn-link "key")
            (fn-link "val")]]
        [:tr
          [:td.label-9e0b7 "Упорядоченные словари"]
          [:td.body-885f4
            (fn-link "rseq")
            (fn-link "subseq")
            (fn-link "rsubseq")]]]]])

(hiccups/defhtml create-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Создание последовательностей"]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Из коллекции"]
          [:td.body-885f4
            (fn-link "seq")
            (fn-link "vals")
            (fn-link "keys")
            (fn-link "rseq")
            (fn-link "subseq")
            (fn-link "rsubseq")]]
        [:tr
          [:td.label-9e0b7 "Создающие функции"]
          [:td.body-885f4
            (fn-link "lazy-seq")
            (fn-link "repeatedly")
            (fn-link "iterate")]]
        [:tr
          [:td.label-9e0b7 "Из постоянных"]
          [:td.body-885f4
            (fn-link "repeat")
            (fn-link "range")]]
        [:tr
          [:td.label-9e0b7 "Из другого"]
          [:td.body-885f4
            (fn-link "re-seq")
            (fn-link "tree-seq")]]
        [:tr
          [:td.label-9e0b7 "Из последовательностей"]
          [:td.body-885f4
            (fn-link "keep")
            (fn-link "keep-indexed")]]]]])

(hiccups/defhtml seq-in-out-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Операции, принимающие и возвращающие последовательности" (tt-icon "sequences")]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Убавление"]
          [:td.body-885f4
            (fn-link "distinct")
            (fn-link "filter")
            (fn-link "remove")
            (fn-link "take-nth")
            (fn-link "for")]]
        [:tr
          [:td.label-9e0b7 "Прибавленее"]
          [:td.body-885f4
            (fn-link "cons")
            (fn-link "conj")
            (fn-link "concat")
            (fn-link "lazy-cat")
            (fn-link "mapcat")
            (fn-link "cycle")
            (fn-link "interleave")
            (fn-link "interpose")]]
        [:tr
          [:td.label-9e0b7 "Получение от конца"]
          [:td.body-885f4
            (fn-link "rest")
            (fn-link "nthrest")
            (fn-link "next")
            (fn-link "fnext")
            (fn-link "nnext")
            (fn-link "drop")
            (fn-link "drop-while")
            (fn-link "take-last")
            (fn-link "for")]]
        [:tr
          [:td.label-9e0b7 "Получение от начала"]
          [:td.body-885f4
            (fn-link "take")
            (fn-link "take-while")
            (fn-link "butlast")
            (fn-link "drop-last")
            (fn-link "for")]]
        [:tr
          [:td.label-9e0b7 "'Изменение'"]
          [:td.body-885f4
            (fn-link "conj")
            (fn-link "concat")
            (fn-link "distinct")
            (fn-link "flatten")
            (fn-link "group-by")
            (fn-link "partition")
            (fn-link "partition-all")
            (fn-link "partition-by")
            (fn-link "split-at")
            (fn-link "split-with")
            (fn-link "filter")
            (fn-link "remove")
            (fn-link "replace")
            (fn-link "shuffle")]]
        [:tr
          [:td.label-9e0b7 "Изменение порядка"]
          [:td.body-885f4
            (fn-link "reverse")
            (fn-link "sort")
            (fn-link "sort-by")
            (fn-link "compare")]]
        [:tr
          [:td.label-9e0b7 "Итерирование"]
          [:td.body-885f4
            (fn-link "map")
            (fn-link "map-indexed")
            (fn-link "mapcat")
            (fn-link "for")
            (fn-link "replace")]]]]])

(hiccups/defhtml use-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Использование последовательснотей"]
    [:table.tbl-902f0
      [:tbody
        [:tr
          [:td.label-9e0b7 "Получение значения"]
          [:td.body-885f4
            (fn-link "first")
            (fn-link "second")
            (fn-link "last")
            (fn-link "rest")
            (fn-link "next")
            (fn-link "ffirst")
            (fn-link "nfirst")
            (fn-link "fnext")
            (fn-link "nnext")
            (fn-link "nth")
            (fn-link "nthnext")
            (fn-link "rand-nth")
            (fn-link "when-first")
            (fn-link "max-key")
            (fn-link "min-key")]]
        [:tr
          [:td.label-9e0b7 "Создание коллекций"]
          [:td.body-885f4
            (fn-link "zipmap")
            (fn-link "into")
            (fn-link "reduce")
            (fn-link "reductions")
            (fn-link "set")
            (fn-link "vec")
            (fn-link "into-array")
            (fn-link "to-array-2d")]]
        [:tr
          [:td.label-9e0b7 "Передеча аргументов в функцию"]
          [:td.body-885f4
            (fn-link "apply")]]
        [:tr
          [:td.label-9e0b7 "Поиск"]
          [:td.body-885f4
            (fn-link "some")
            (fn-link "filter")]]
        [:tr
          [:td.label-9e0b7 "Принудительное выполнение"]
          [:td.body-885f4
            (fn-link "doseq")
            (fn-link "dorun")
            (fn-link "doall")]]
        [:tr
          [:td.label-9e0b7 "Проверка на принудительное выполнение"]
          [:td.body-885f4
            (fn-link "realized?")]]]]])

(hiccups/defhtml bitwise-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Побитовые операции"]
    [:div.solo-section-d5309
      (fn-link "bit-and")
      (fn-link "bit-or")
      (fn-link "bit-xor")
      (fn-link "bit-not")
      (fn-link "bit-flip")
      (fn-link "bit-set")
      (fn-link "bit-shift-right")
      (fn-link "bit-shift-left")
      (fn-link "bit-and-not")
      (fn-link "bit-clear")
      (fn-link "bit-test")
      (fn-link "unsigned-bit-shift-right")]])

;; TODO: create "Export to JavaScript" section
;; include ^:export and goog.exportSymbol functions
;; and a sentence about how it works

;;------------------------------------------------------------------------------
;; Info Tooltips
;;------------------------------------------------------------------------------

;; TODO: break these up into functions

(hiccups/defhtml truthy-table []
  [:table.tbl-3160a
    [:thead
      [:tr
        [:th.tbl-hdr-e0564 "Имя"]
        [:th.tbl-hdr-e0564 "Код"]
        [:th.tbl-hdr-e0564 "Булевое значение"]]]
    [:tbody
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Пустая строка"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "\"\""]]
        [:td.cell-e6fd2 [:code "true"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Ноль"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "0"]]
        [:td.cell-e6fd2 [:code "true"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Не число"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "js/NaN"]]
        [:td.cell-e6fd2 [:code "true"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Пустой вектор"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "[]"]]
        [:td.cell-e6fd2 [:code "true"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Пустой массив"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "(array)"]]
        [:td.cell-e6fd2 [:code "true"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "False"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "false"]]
        [:td.cell-e6fd2 [:code "false"]]]
      [:tr
        [:td.cell-e6fd2.right-border-c1b54 "Nil"]
        [:td.cell-e6fd2.right-border-c1b54 [:code "nil"]]
        [:td.cell-e6fd2 [:code "false"]]]]])

(hiccups/defhtml function-shorthand-table []
  [:table.exmpl-tbl-42d9f
    [:thead
      [:tr
        [:th.tbl-hdr-e0564 "Сокращение"]
        [:th.tbl-hdr-e0564 "Раскрывается в"]]]
    [:tbody
      [:tr
        [:td.code-72fa0.right-border-c1b54 "#(str \"Hello \" %)"]
        [:td.code-72fa0 [:pre "(fn [n]\n  (str \"Hello \" n))"]]]
      [:tr
        [:td.code-72fa0.right-border-c1b54 "#(my-fn %1 %2 %3)"]
        [:td.code-72fa0 [:pre "(fn [a b c]\n  (my-fn a b c))"]]]
      [:tr
        [:td.code-72fa0.right-border-c1b54 "#(* % (apply + %&amp;))"]
        [:td.code-72fa0 [:pre {:style "font-size:10px"}
                          "(fn [x &amp; the-rest]\n"
                          "  (* x (apply + the-rest)))"]]]]])

(hiccups/defhtml basics-tooltips []

  [:div#tooltip-define.tooltip-53dde {:style "display:none"}
    [:p "В ClojureScript все данные неизменяемы по умолчанию. Это значит, "
        "что значение символа не может быть изменено после его определения."]]

  [:div#tooltip-branch.tooltip-53dde {:style "display:none"}
    [:p "В операторах ветвления все значения приводятся к " [:code "true"]
        " за исключением " [:code "false"] " и " [:code "nil"] "."]
    [:p "В JavaScript правила приведения типов намного сложнее."]
    (truthy-table)]

  [:div#tooltip-numbers.tooltip-53dde {:style "display:none"}
    [:p "Все числа в ClojureScript представлены в формате числа двойной точности "
        "с плавающей запятой стандарта IEEE 754. Так же, как и в JavaScript."]]

  [:div#tooltip-atoms.tooltip-53dde {:style "display:none"}
    [:p
      "Атомы предоставляют возможность управления состоянием в ClojureScript."]
    [:p
      "В отличие от JavaScript, в ClojureScript все данные неизменяемы по умолчанию. "
      "Это значит, что вы не можете изменить значение чего-то после определения."]
    [:p
      "Атомы делают возможным изменение значения. Также они разделяют то, "
      "как значение изменяется и считывается, что делает состояние более понятным."]
    [:p
      "Наблюдающие функции выполняются, когда значение изменяется."
      "Это идеальное поведение, когда данные являются отображением UI."]]

  [:div#tooltip-functions.tooltip-53dde {:style "display:none"}
    [:p
      "Функции в ClojureScript и в JavaScript схожи."]
    [:p
      "Базовая библиотека имеет множество полезных функций высшего порядка. "
      "Также в ClojureScript есть удобное сокращение для создания анонимных функций."]]

  [:div#tooltip-function-shorthand.tooltip-53dde {:style "display:none"}
    [:p
      "Короткая запись анонимных функций " [:code "#()"] " очень удобна для создания "
      "небольших функций."]
    [:p
      "Анонимные функции "[:code "#()"] " не могут быть вложенными, и считается правильным писать "
      "их как можно короче."]
    (function-shorthand-table)]

  [:div#tooltip-strings.tooltip-53dde {:style "display:none"}
    [:p "Строки в ClojureScript такие же, как в JavaScript, и у них есть все методы "
        "и свойства, которые есть у строк в JavaScript."]
    [:p "Строки в ClojureScript должны быть заключены в двойные кавычки."]
    [:p "Пространство имен " [:code "clojure.string"] " содержит много полезных "
        "функций для работы со строками."]])

(hiccups/defhtml collections-tooltips []
  [:div#tooltip-collections.tooltip-53dde {:style "display:none"}
    [:p
      "В ClojureScript есть четыре типа коллекций: списки, векторы, множества и "
      "словари. "
      "Каждый из этих типов данных имеет свои сильные стороны, и все они активно "
      "используются в большинстве программ."]
    [:p
      "Все коллекции являются неизменяемыми и постоянными, что означает, что "
      "при изменении они сохраняют свою предыдущую версию. "
      "Создание \"измененной\" версии любой коллекции является эффективной "
      "операцией."]
    [:p
      "У всех коллекций есть представление в виде литерала:"]
    [:table.tbl-3160a
      [:thead
        [:tr
          [:th.tbl-hdr-e0564 "Коллекция"]
          [:th.tbl-hdr-e0564 "Литерал"]]]
      [:tbody
        [:tr
          [:td.cell-e6fd2.right-border-c1b54 "Список"]
          [:td.cell-e6fd2 [:code "()"]]]
        [:tr
          [:td.cell-e6fd2.right-border-c1b54 "Вектор"]
          [:td.cell-e6fd2 [:code "[]"]]]
        [:tr
          [:td.cell-e6fd2.right-border-c1b54 "Множество"]
          [:td.cell-e6fd2 [:code "#{}"]]]
        [:tr
          [:td.cell-e6fd2.right-border-c1b54 "Словарь"]
          [:td.cell-e6fd2 [:code "{}"]]]]]]

  [:div#tooltip-lists.tooltip-53dde {:style "display:none"}
    [:p
      "Списки — это последовательности значений, так же, как и векторы."]
    [:p
      "Большинство литералов списков представляют собой вызов функции."]
    [:p
      [:code "(a b c)"] " список из трех значений, и в то же время это означает "
      "\"вызов функции " [:code "a"] " с двумя аргументами: " [:code "b"]
      " и " [:code "c"] "\""]]

  [:div#tooltip-vectors.tooltip-53dde {:style "display:none"}
    [:p
      "Векторы представляют собой наборы значений, которые индексированы "
      "последовательными целыми числами."]
    [:p
      "Хоть они и похожи, но массивы в JavaScript не то же самое, "
      "что векторы в ClojureScript. "
      [:code "(.indexOf my-vec)"] " не сработает на векторе."]]

  [:div#tooltip-vector-as-fn.tooltip-53dde {:style "display:none"}
    [:p
      "Вектор может быть использован как функция, для получения его значений."]]

  [:div#tooltip-sets.tooltip-53dde {:style "display:none"}
    [:p "Множества — это коллекции уникальных значений, так же, как и в "
      "математике."]]

  [:div#tooltip-set-as-fn.tooltip-53dde {:style "display:none"}
    [:p
      "Множество может быть использован как функция, для получения его значений."]]

  [:div#tooltip-maps.tooltip-53dde {:style "display:none"}
    [:p
      "Словарь — это коллекция пар ключ/значение. "
      "Получение значения по ключу из коллекции работает очень быстро."]
    [:p
      "В JavaScript, объекты обычно используются как словари, в которых "
      "ключами могут быть только строки. "
      "В ClojureScript ключом в словаре может быть любое значение, "
      "хотя принято использовать специальный тип данных Keyword (ключевое слово)."]]

  [:div#tooltip-keywords-as-fn.tooltip-53dde {:style "display:none"}
    [:p
      "Ключевые слова могут быть использованы как функции для получения значений из словаря. "
      "Поэтому они часто используются как ключи в словарях."]])

(hiccups/defhtml sequences-tooltips []
  [:div#tooltip-sequences.tooltip-53dde {:style "display:none"}
    [:p
      "Многие базовые операции определены на последовательностях. "
      "Последовательность представляет собой интерфейс списка."]
    [:p
      "Любая последовательность является коллекцией, а любая коллекция может быть "
      "преобразована в последовательность с помощью фцнкции " [:code "seq"] ". "
      "На самом деле, это то, что происходит с коллекцией, когда она попадает "
      "в функцию последовательности."]
    [:p
      "Большинство функций последовательностей ленивы, что означает, что они "
      "обрабатывают их элементы по мере необходимости. "
      "Это дает возможность создавать бесконечные последовательности."]
    [:p
      "Вы можете заставить последовательность вычислить все свои элементы, используя функцию "
      [:code "doall"] ". Это удобно, когда вы хотите узнать результат выполнения "
      "функции с побочными эффектами на всех элементах последовательности."]])

(hiccups/defhtml info-tooltips []
  [:section
    (basics-tooltips)
    (collections-tooltips)
    (sequences-tooltips)])

;;------------------------------------------------------------------------------
;; Header and Footer
;;------------------------------------------------------------------------------

(hiccups/defhtml header []
  [:header
    [:h1
      [:img {:src "img/cljs-ring.svg" :alt "Логотип ClojureScript"}]
      "Справочник по ClojureScript"]
    [:input#searchInput {:type "text" :placeholder "Поиск"}]])

(def clojure-cheatsheet-href "http://clojure.org/cheatsheet")
(def clojure-tooltip-cheatsheet-href "http://jafingerhut.github.io/cheatsheet/clojuredocs/cheatsheet-tiptip-cdocs-summary.html")
(def clojurescript-github-href "https://github.com/clojure/clojurescript")
(def repo-href "https://github.com/clojurescript-ru/cljs-cheatsheet/")
(def access-href "https://github.com/oakmac/cljs-cheatsheet/issues/6#issuecomment-199989399")
(def license-href "https://github.com/clojurescript-ru/cljs-cheatsheet/blob/master/LICENSE.md")

;; include this? "Please copy, improve, and share this work."
;; TODO: improve the markup here
(hiccups/defhtml footer []
  [:footer
    [:div.links-446e0
      [:label.quiet-5d4e8 "ссылки: "]
      [:a.ftr-link-e980e {:href clojure-cheatsheet-href} "Справочник по Clojure"]
      ", "
      [:a.ftr-link-e980e {:href clojure-tooltip-cheatsheet-href} "еще одна"]
      ", "
      [:a.ftr-link-e980e {:href clojurescript-github-href} "исходники ClojureScript"]]
    [:div.links-446e0
      [:label.quiet-5d4e8 "исходники сайта: "]
      [:a.ftr-link-e980e {:href repo-href} "github.com/clojurescript-ru/cljs-cheatsheet"]]
    [:div.links-446e0
      [:label.quiet-5d4e8 "оригинальный контент использован с разрешения автора: "]
      [:a.ftr-link-e980e {:href access-href} "oakmac"]]
    [:div.links-446e0
      [:label.quiet-5d4e8 "лицензия: "]
      [:a.ftr-link-e980e {:href license-href} "MIT"]]])

;;------------------------------------------------------------------------------
;; Head and Script Includes
;;------------------------------------------------------------------------------

(def page-title "Справочник по ClojureScript")

(hiccups/defhtml head []
  [:head
    [:meta {:charset "utf-8"}]
    [:title page-title]
    [:meta {:name "description" :content "Справочник по ClojureScript"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "apple-touch-icon" :href "apple-touch-icon.png"}]
    [:link {:rel "stylesheet" :href "css/main.min.css"}]])

(hiccups/defhtml script-tags []
  [:script {:src "js/cheatsheet.min.js"}])

;;------------------------------------------------------------------------------
;; Body
;;------------------------------------------------------------------------------

(hiccups/defhtml body []
  [:section.major-category
    [:h2 "Основы"]
    [:div.three-col-container
      [:div.column
        (basics-section)
        (functions-section)]
      [:div.column
        (numbers-section)
        (strings-section)]
      [:div.column
        (atoms-section)
        (js-interop-section)]]
    [:div.two-col-container
      [:div.column
        (basics-section)
        (numbers-section)
        (js-interop-section)]
      [:div.column
        (functions-section)
        (strings-section)
        (atoms-section)]]]

  [:section.major-category
    [:h2 "Коллекции"]
    [:div.three-col-container
      [:div.column
        (collections-section)
        (lists-section)]
      [:div.column
        (vectors-section)
        (sets-section)]
      [:div.column
        (maps-section)]]
    [:div.two-col-container
      [:div.column
        (collections-section)
        (lists-section)
        (maps-section)]
      [:div.column
        (vectors-section)
        (sets-section)]]]

  [:section.major-category
    [:h2 "Последовательности"]
    [:div.three-col-container
      [:div.column (seq-in-out-section)]
      [:div.column (use-seq-section)]
      [:div.column (create-seq-section)]]
    [:div.two-col-container
      [:div.column (seq-in-out-section)]
      [:div.column
        (use-seq-section)
        (create-seq-section)]]]

  [:section.major-category
    [:h2 "Разное"]
    [:div.three-col-container
      [:div.column (bitwise-section)]]
    [:div.two-col-container
      [:div.column (bitwise-section)]]])

(defn cheatsheet-page []
  (str "<!DOCTYPE html>"
       "<html lang=\"ru\">"
       (head)
       "<body>"
       (header)
       (body)
       (footer)
       (info-tooltips)
       (script-tags)
       "</body>"
       "</html>"))

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn- write-cheatsheet-html! []
  (.writeFileSync fs "public/index.html" (cheatsheet-page)))

(defn- write-symbols-json! []
  (.writeFileSync fs "symbols.json" (-> @symbols sort clj->js json-stringify)))

(write-cheatsheet-html!)
(write-symbols-json!)

;; needed for :nodejs cljs build
(def always-nil (constantly nil))
(set! *main-cli-fn* always-nil)
