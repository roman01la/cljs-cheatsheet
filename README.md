# A ClojureScript Cheatsheet

This project produces the cheatsheet at [clojurescript.ru/cheatsheet]

## Design

TODO: write this section

## Development Setup

### First time setup

Install [Leiningen] and [Node.js]

```sh
# install node_modules
npm install
```

### Development workflow

You may wish to run these commands in separate console tabs / screens.

```sh
# does two things:
# - compiles LESS into CSS whenever a less/*.less file changes
# - builds public/docs.json whenever a docs/*.cljsdoc file changes
npm run start:grunt

# run a local web server on port 9224
# the port is configurable and defaults to 8888 if not provided
npm run start:server

# compile ClojureScript files
npm run start:lein

# build public/index.html
# create a build into the 00-publish directory
npm run build
```

## License

[MIT License]

[clojurescript.ru/cheatsheet]:http://clojurescript.ru/cheatsheet
[cljs.info/cheatsheet]:http://cljs.info/cheatsheet
[Leiningen]:http://leiningen.org
[Node.js]:http://nodejs.org
[MIT License]:https://github.com/oakmac/cljs-cheatsheet/blob/master/LICENSE.md
