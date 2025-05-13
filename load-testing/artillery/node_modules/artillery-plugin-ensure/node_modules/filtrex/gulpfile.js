// Compile parser
const { parser } = require('./src/generateParser');
const parserSourceCode = parser.generate({ moduleType: 'esm' })

const Vinyl = require('vinyl')

// Create file from string
// via https://stackoverflow.com/a/23398200/1137334
function srcFromString(path, str) {
    var src = require('stream').Readable({ objectMode: true })
    src._read = function () {
      this.push(new Vinyl({
        cwd: '',
        base: SRC,
        path: path,
        contents: Buffer.from(str, 'utf-8')
      }))
      this.push(null)
    }
    return src
}


const pipe = require('util').promisify(require('stream').pipeline)
const { src, dest } = require('gulp')
const beautify = require('gulp-beautify')
const uglify = require('gulp-uglify-es').default
const rename = require('gulp-rename')
const del = require('del')

const { rollup } = require('rollup')
const SRC = './src'
const DIST = './dist'


const clean =
exports.clean =
async function clean() {
    await del(`${DIST}/**`)
}

const buildEsm =
exports.buildEsm =
async function buildEsm() {
    await pipe(
        srcFromString(`${SRC}/parser.mjs`, parserSourceCode),
        beautify({ indent_size: 4, end_with_newline: true }),
        dest(`${DIST}/esm/`)
    )
    await pipe(
        src([ `${SRC}/*.mjs`, `${SRC}/*.d.ts` ]),
        dest(`${DIST}/esm/`)
    )
}

const buildCjs =
exports.buildCjs =
async function buildCjs() {
    const bundle = await rollup({
        input: `${DIST}/esm/filtrex.mjs`
    });

    await bundle.write({
        output: {
            dir: `${DIST}/cjs/`,
            format: 'cjs'
        }
    })
}

const buildBrowser =
exports.buildBrowser =
async function buildBrowser() {
    const bundle = await rollup({
        input: `${DIST}/esm/filtrex.mjs`
    });

    await bundle.write({
        output: {
            name: 'filtrex',
            dir: `${DIST}/browser/`,
            format: 'iife'
        }
    })

    await pipe(
        src(`${DIST}/browser/filtrex.js`),
        uglify(),
        rename(path => path.basename += '.min'),
        dest(`${DIST}/browser/`)
    )
}

const build =
exports.default =
exports.build =
async function build() {
    await clean()
    await buildEsm()
    await buildCjs()
    await buildBrowser()
}
