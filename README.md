# anaphorae

A small set of useful anaphoric macros.

# Usage

In your project.clj: `[anaphorae "0.2.0"]`

# Threading (-> and ->>)

These macros are meant to replace the existing `->` and `->>` macros. If you're
feeling lucky you should totally `(use '[anaphorae/threading])`. (Although that
requires `(refer-clojure :except [-> ->>])` to confirm you really want to use
your own.

`->>` is great except every once in a while you want to thread-first in a single
form and thread-last in the rest. (`->` has a similar story) This version of it
will let you put a `%` in any of the forms passed in to indicate where the
previous form will be inserted. In the absense of `%` they will behave like the
macros they replace. (The original macros return a form with the same metadata
as the first form passed in to them, a behavior these macros do not replicate)

# Partial

A not-quite-ready implementation of `partial` with additional support for
adding `%` or `%&` in places the later arguments ought be passed in.
